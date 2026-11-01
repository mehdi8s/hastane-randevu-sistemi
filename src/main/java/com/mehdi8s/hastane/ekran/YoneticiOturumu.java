package com.mehdi8s.hastane.ekran;

import com.mehdi8s.hastane.hizmet.GenelMuayene;
import com.mehdi8s.hastane.hizmet.Laboratuvar;
import com.mehdi8s.hastane.hizmet.Radyoloji;
import com.mehdi8s.hastane.hizmet.SaglikHizmeti;
import com.mehdi8s.hastane.kayit.SistemKaydi;
import com.mehdi8s.hastane.kisi.Doktor;
import com.mehdi8s.hastane.kisi.Yonetici;
import com.mehdi8s.hastane.kurum.Bolum;
import com.mehdi8s.hastane.kurum.SaglikKurumu;
import com.mehdi8s.hastane.kurum.Sube;
import com.mehdi8s.hastane.randevu.Randevu;
import com.mehdi8s.hastane.zaman.ZamanAraligi;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public final class YoneticiOturumu extends YetkiliOturum {
    public YoneticiOturumu(Yonetici yonetici, SistemKaydi kayit, KayitSaklayici saklayici) {
        super(yonetici, kayit, saklayici);
    }

    @Override
    protected List<Randevu> gorevRandevulari() {
        return servis().getRandevular().stream()
                .filter(randevu -> randevu.getDoktor().getKurum().equals(kurum()))
                .toList();
    }

    @Override
    protected void komutlariYaz(Konsol konsol) {
        super.komutlariYaz(konsol);
        konsol.yaz("6) Sube ekle");
        konsol.yaz("7) Bolum ekle");
        konsol.yaz("8) Calisma saati ekle");
        konsol.yaz("9) Hizmet ekle");
        konsol.yaz("10) Doktora hizmet ekle");
        konsol.yaz("11) Doktora uygunluk ekle");
    }

    @Override
    protected boolean komutCalistir(String secim, Konsol konsol) {
        if (super.komutCalistir(secim, konsol)) {
            return true;
        }
        return switch (secim) {
            case "6" -> {
                subeEkle(konsol);
                yield true;
            }
            case "7" -> {
                bolumEkle(konsol);
                yield true;
            }
            case "8" -> {
                calismaSaatiEkle(konsol);
                yield true;
            }
            case "9" -> {
                hizmetEkle(konsol);
                yield true;
            }
            case "10" -> {
                doktoraHizmetEkle(konsol);
                yield true;
            }
            case "11" -> {
                uygunlukEkle(konsol);
                yield true;
            }
            default -> false;
        };
    }

    private void subeEkle(Konsol konsol) {
        String ad = doluOku(konsol, "Sube adi");
        String adres = doluOku(konsol, "Adres");
        kurum().subeEkle(new Sube(ad, adres));
        sakla();
        konsol.yaz("Sube eklendi: " + ad);
    }

    private void bolumEkle(Konsol konsol) {
        Sube sube = subeSec(konsol);
        String ad = doluOku(konsol, "Bolum adi");
        sube.bolumEkle(new Bolum(ad));
        sakla();
        konsol.yaz("Bolum eklendi: " + ad);
    }

    private void calismaSaatiEkle(Konsol konsol) {
        Sube sube = subeSec(konsol);
        sube.calismaSaatiEkle(aralikOku(konsol));
        sakla();
        konsol.yaz("Calisma saati eklendi.");
    }

    private void hizmetEkle(Konsol konsol) {
        Sube sube = subeSec(konsol);
        konsol.yaz("1) Genel Muayene");
        konsol.yaz("2) Radyoloji");
        konsol.yaz("3) Laboratuvar");
        int tur = sayiOku(konsol, "Hizmet turu");
        String ad = doluOku(konsol, "Hizmet adi");
        int sure = sayiOku(konsol, "Sure (dk)");
        BigDecimal ucret = ucretOku(konsol);
        SaglikHizmeti hizmet = switch (tur) {
            case 1 -> new GenelMuayene(ad, sure, ucret);
            case 2 -> new Radyoloji(ad, sure, ucret);
            case 3 -> new Laboratuvar(ad, sure, ucret);
            default -> throw new IllegalArgumentException("Gecersiz hizmet turu.");
        };
        sube.hizmetEkle(hizmet);
        sakla();
        konsol.yaz("Hizmet eklendi: " + hizmet);
    }

    private void doktoraHizmetEkle(Konsol konsol) {
        Doktor doktor = doktorSec(konsol);
        SaglikHizmeti hizmet = listedanSec(
                konsol,
                doktor.getSube().getHizmetler(),
                "Bu subede hizmet yok.",
                "Hizmet");
        doktor.hizmetEkle(hizmet);
        sakla();
        konsol.yaz("Hizmet doktora eklendi: " + hizmet);
    }

    private void uygunlukEkle(Konsol konsol) {
        Doktor doktor = doktorSec(konsol);
        doktor.uygunlukEkle(aralikOku(konsol));
        sakla();
        konsol.yaz("Uygunluk eklendi.");
    }

    private Sube subeSec(Konsol konsol) {
        return listedanSec(konsol, kurum().getSubeler(), "Kurumda sube yok.", "Sube");
    }

    private Doktor doktorSec(Konsol konsol) {
        List<Doktor> doktorlar = kayit().getDoktorlar().stream()
                .filter(doktor -> doktor.getKurum().equals(kurum()))
                .toList();
        return listedanSec(konsol, doktorlar, "Kurumda doktor yok.", "Doktor");
    }

    private ZamanAraligi aralikOku(Konsol konsol) {
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

    private BigDecimal ucretOku(Konsol konsol) {
        String metin = konsol.oku("Ucret");
        try {
            return new BigDecimal(metin);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Ucret sayi olmali.");
        }
    }

    private SaglikKurumu kurum() {
        return ((Yonetici) kisi()).getKurum();
    }
}
