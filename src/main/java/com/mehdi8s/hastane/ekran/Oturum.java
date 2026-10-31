package com.mehdi8s.hastane.ekran;

import com.mehdi8s.hastane.kayit.SistemKaydi;
import com.mehdi8s.hastane.kisi.Kisi;
import com.mehdi8s.hastane.randevu.Randevu;
import com.mehdi8s.hastane.randevu.RandevuServisi;

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
        String metin = konsol.oku(istem);
        int sira;
        try {
            sira = Integer.parseInt(metin);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Sayi girilmeli.");
        }
        if (sira < 1 || sira > boyut) {
            throw new IllegalArgumentException("Gecersiz sira.");
        }
        return sira - 1;
    }
}
