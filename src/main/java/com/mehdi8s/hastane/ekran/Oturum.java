package com.mehdi8s.hastane.ekran;

import com.mehdi8s.hastane.kayit.SistemKaydi;
import com.mehdi8s.hastane.kisi.Kisi;
import com.mehdi8s.hastane.randevu.Randevu;
import com.mehdi8s.hastane.randevu.RandevuDurumu;
import com.mehdi8s.hastane.randevu.RandevuServisi;
import com.mehdi8s.hastane.zaman.ZamanAraligi;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public abstract class Oturum {
    private final Kisi kisi;
    private final SistemKaydi kayit;
    private final KayitSaklayici saklayici;

    protected Oturum(Kisi kisi, SistemKaydi kayit, KayitSaklayici saklayici) {
        this.kisi = kisi;
        this.kayit = kayit;
        this.saklayici = saklayici;
    }

    public final void calistir(Konsol konsol) {
        while (true) {
            konsol.yaz("");
            konsol.yaz(kisi.tamAd() + " (" + kisi.rol() + ")");
            komutlariYaz(konsol);
            konsol.yaz("0) Cikis");
            String secim = konsol.oku("Secim");
            if ("0".equals(secim)) {
                return;
            }
            try {
                if (!komutCalistir(secim, konsol)) {
                    konsol.yaz("Gecersiz secim.");
                }
            } catch (RuntimeException ex) {
                konsol.yaz(ex.getMessage() == null ? "Islem yapilamadi." : ex.getMessage());
            }
        }
    }

    protected abstract void komutlariYaz(Konsol konsol);

    protected abstract boolean komutCalistir(String secim, Konsol konsol);

    protected final Kisi kisi() {
        return kisi;
    }

    protected final SistemKaydi kayit() {
        return kayit;
    }

    protected final RandevuServisi servis() {
        return kayit.getRandevuServisi();
    }

    protected final void sakla() {
        saklayici.kaydet();
    }

    protected final List<Randevu> durumaGore(Konsol konsol, List<Randevu> kaynak) {
        konsol.yaz("0) Tumu");
        RandevuDurumu[] durumlar = RandevuDurumu.values();
        for (int i = 0; i < durumlar.length; i++) {
            konsol.yaz((i + 1) + ") " + durumlar[i]);
        }
        int secim = sayiOku(konsol, "Durum");
        if (secim == 0) {
            return kaynak;
        }
        if (secim < 1 || secim > durumlar.length) {
            throw new IllegalArgumentException("Gecersiz durum.");
        }
        RandevuDurumu durum = durumlar[secim - 1];
        return kaynak.stream().filter(randevu -> randevu.getDurum() == durum).toList();
    }

    protected final void listeYaz(Konsol konsol, List<Randevu> liste) {
        if (liste.isEmpty()) {
            konsol.yaz("Kayit yok.");
            return;
        }
        for (int i = 0; i < liste.size(); i++) {
            Randevu randevu = liste.get(i);
            konsol.yaz((i + 1) + ") " + randevu.getBaslangic()
                    + " " + randevu.getHasta().tamAd()
                    + " / " + randevu.getDoktor().tamAd()
                    + " / " + randevu.getHizmetAdi()
                    + " / " + randevu.getDurum());
        }
    }

    protected final int siraOku(Konsol konsol, int boyut, String istem) {
        int sira = sayiOku(konsol, istem);
        if (sira < 1 || sira > boyut) {
            throw new IllegalArgumentException("Gecersiz sira.");
        }
        return sira - 1;
    }

    protected final int sayiOku(Konsol konsol, String istem) {
        String metin = konsol.oku(istem);
        try {
            return Integer.parseInt(metin);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Sayi girilmeli.");
        }
    }

    protected final String doluOku(Konsol konsol, String istem) {
        String metin = konsol.oku(istem);
        if (metin == null || metin.isBlank()) {
            throw new IllegalArgumentException(istem + " bos olamaz.");
        }
        return metin.trim();
    }

    protected final <T> T listedanSec(Konsol konsol, List<T> liste, String bosMesaj, String istem) {
        if (liste.isEmpty()) {
            throw new IllegalArgumentException(bosMesaj);
        }
        for (int i = 0; i < liste.size(); i++) {
            konsol.yaz((i + 1) + ") " + liste.get(i));
        }
        return liste.get(siraOku(konsol, liste.size(), istem));
    }

    protected final ZamanAraligi aralikOku(Konsol konsol) {
        konsol.yaz("Gun: 1 Pazartesi ... 7 Pazar");
        int gun = sayiOku(konsol, "Gun");
        if (gun < 1 || gun > 7) {
            throw new IllegalArgumentException("Gun 1 ile 7 arasinda olmali.");
        }
        return new ZamanAraligi(DayOfWeek.of(gun), saatOku(konsol, "Baslangic"), saatOku(konsol, "Bitis"));
    }

    private LocalTime saatOku(Konsol konsol, String istem) {
        String metin = konsol.oku(istem + " (HH:mm)");
        try {
            return LocalTime.parse(metin);
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Saat HH:mm olmali.");
        }
    }
}
