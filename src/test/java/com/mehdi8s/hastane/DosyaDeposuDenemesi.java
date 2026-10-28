package com.mehdi8s.hastane;

import com.mehdi8s.hastane.hizmet.GenelMuayene;
import com.mehdi8s.hastane.hizmet.Radyoloji;
import com.mehdi8s.hastane.kayit.DosyaDeposu;
import com.mehdi8s.hastane.kayit.SistemKaydi;
import com.mehdi8s.hastane.kisi.Doktor;
import com.mehdi8s.hastane.kisi.Hasta;
import com.mehdi8s.hastane.kisi.SaglikPersoneli;
import com.mehdi8s.hastane.kisi.Uzmanlik;
import com.mehdi8s.hastane.kisi.Yonetici;
import com.mehdi8s.hastane.kurum.Bolum;
import com.mehdi8s.hastane.kurum.Hastane;
import com.mehdi8s.hastane.kurum.Klinik;
import com.mehdi8s.hastane.kurum.SaglikKurumu;
import com.mehdi8s.hastane.kurum.Sube;
import com.mehdi8s.hastane.randevu.Randevu;
import com.mehdi8s.hastane.randevu.RandevuDurumu;
import com.mehdi8s.hastane.zaman.ZamanAraligi;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class DosyaDeposuDenemesi {
    public static void main(String[] args) {
        try {
            kayitKapanincaAyniKalir();
            bozukDosyaReddedilir();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        System.out.println("Dosya deposu denemesi gecti.");
    }

    private static void kayitKapanincaAyniKalir() throws IOException {
        Hastane hastane = new Hastane("Şehir Hastanesi");
        Sube merkez = new Sube("Merkez", "Sakarya / Serdivan");
        Sube ilce = new Sube("İlçe", "Adapazarı");
        hastane.subeEkle(merkez);
        hastane.subeEkle(ilce);
        merkez.bolumEkle(new Bolum("Dahiliye"));
        merkez.calismaSaatiEkle(new ZamanAraligi(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)));
        GenelMuayene muayene = new GenelMuayene("Dahiliye Muayenesi", 20, new BigDecimal("750.50"));
        Radyoloji rontgen = new Radyoloji("Akciğer Grafisi", 15, new BigDecimal("400.00"));
        merkez.hizmetEkle(muayene);
        merkez.hizmetEkle(rontgen);

        Doktor doktor = new Doktor("Ayşe", "Yılmaz", hastane, merkez, new Uzmanlik("Dahiliye"));
        doktor.hizmetEkle(muayene);
        doktor.uygunlukEkle(new ZamanAraligi(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)));
        Hasta hasta = new Hasta("Özge", "Çelik");
        Yonetici yonetici = new Yonetici("Selin", "Arslan", hastane);
        SaglikPersoneli personel = new SaglikPersoneli("Ece", "Akın", hastane, merkez, "Başhemşire");

        Klinik klinik = new Klinik("Mahalle Kliniği");
        klinik.subeEkle(new Sube("Tek Şube", "Serdivan"));

        SistemKaydi kayit = new SistemKaydi();
        kayit.kurumEkle(hastane);
        kayit.kurumEkle(klinik);
        kayit.hastaEkle(hasta);
        kayit.doktorEkle(doktor);
        kayit.yoneticiEkle(yonetici);
        kayit.personelEkle(personel);

        Randevu iptal = kayit.getRandevuServisi().olustur(hasta, doktor, muayene, LocalDateTime.of(2026, 11, 2, 9, 0));
        kayit.getRandevuServisi().iptalEt(hasta, iptal);
        Randevu onayli = kayit.getRandevuServisi().olustur(hasta, doktor, muayene, LocalDateTime.of(2026, 11, 2, 10, 0));
        kayit.getRandevuServisi().onayla(doktor, onayli);

        Path klasor = Files.createTempDirectory("hastane-kayit");
        Path dosya = klasor.resolve("sistem.kayit");
        DosyaDeposu depo = new DosyaDeposu();
        depo.kaydet(kayit, dosya);
        SistemKaydi yuklenen = depo.yukle(dosya);
        Path ikinci = klasor.resolve("ikinci.kayit");
        depo.kaydet(yuklenen, ikinci);
        kontrol(Files.readString(dosya, StandardCharsets.UTF_8).equals(Files.readString(ikinci, StandardCharsets.UTF_8)),
                "Ikinci kayit ayni dosyayi uretmeli.");

        SaglikKurumu yuklenenHastane = bul(yuklenen, hastane.getId());
        kontrol(yuklenenHastane instanceof Hastane, "Hastane turu korunmali.");
        kontrol(yuklenenHastane.getAd().equals("Şehir Hastanesi"), "Kurum adi korunmali.");
        kontrol(yuklenenHastane.getSubeler().size() == 2, "Iki sube korunmali.");
        Sube yuklenenMerkez = yuklenenHastane.getSubeler().get(0);
        kontrol(yuklenenMerkez.getAdres().equals("Sakarya / Serdivan"), "Adres korunmali.");
        kontrol(yuklenenMerkez.getBolumler().size() == 1, "Bolum korunmali.");
        kontrol(yuklenenMerkez.getHizmetler().size() == 2, "Iki hizmet korunmali.");
        kontrol(yuklenenMerkez.getHizmetler().get(1).hizmetTuru().equals("Radyoloji"), "Radyoloji turu korunmali.");

        Doktor yuklenenDoktor = yuklenen.getDoktorlar().get(0);
        kontrol(yuklenenDoktor.getId().equals(doktor.getId()), "Doktor kimligi korunmali.");
        kontrol(yuklenenDoktor.getAd().equals("Ayşe"), "Doktor adi korunmali.");
        kontrol(yuklenenDoktor.getHizmetler().size() == 1, "Doktorun hizmeti korunmali.");
        kontrol(yuklenenDoktor.getUygunluklar().size() == 1, "Doktorun uygunlugu korunmali.");
        kontrol(yuklenen.getHastalar().get(0).tamAd().equals("Özge Çelik"), "Hasta adi korunmali.");
        kontrol(yuklenen.getPersoneller().get(0).getGorev().equals("Başhemşire"), "Personel gorevi korunmali.");
        kontrol(yuklenen.getYoneticiler().get(0).getKurum().equals(yuklenenHastane), "Yonetici ayni kuruma baglanmali.");

        Klinik yuklenenKlinik = (Klinik) bul(yuklenen, klinik.getId());
        kontrol(hataVerir(() -> yuklenenKlinik.subeEkle(new Sube("İkinci", "Serdivan"))),
                "Yuklenen klinik ikinci subeyi reddetmeli.");

        Randevu yuklenenIptal = yuklenen.getRandevuServisi().getRandevular().get(0);
        Randevu yuklenenOnay = yuklenen.getRandevuServisi().getRandevular().get(1);
        kontrol(yuklenenIptal.getId().equals(iptal.getId()) && yuklenenIptal.getDurum() == RandevuDurumu.IPTAL,
                "Iptal durumu korunmali.");
        kontrol(yuklenenOnay.getDurum() == RandevuDurumu.ONAYLANDI, "Onay durumu korunmali.");
        kontrol(yuklenenOnay.getUcret().compareTo(new BigDecimal("750.50")) == 0, "Ucret korunmali.");
        kontrol(yuklenenOnay.getSureDakika() == 20, "Sure korunmali.");

        Hasta yeniHasta = new Hasta("Ali", "Demir");
        Randevu bosalan = yuklenen.getRandevuServisi().olustur(
                yeniHasta, yuklenenDoktor, yuklenenDoktor.getHizmetler().get(0), LocalDateTime.of(2026, 11, 2, 9, 0));
        kontrol(bosalan.getDurum() == RandevuDurumu.ONAY_BEKLIYOR, "Iptal edilen saat yeniden verilmeli.");
        kontrol(hataVerir(() -> yuklenen.getRandevuServisi().olustur(
                new Hasta("Veli", "Demir"), yuklenenDoktor, yuklenenDoktor.getHizmetler().get(0),
                LocalDateTime.of(2026, 11, 2, 10, 0))),
                "Onayli saat dolu kalmali.");
    }

    private static void bozukDosyaReddedilir() throws IOException {
        Path dosya = Files.createTempDirectory("hastane-bozuk").resolve("bozuk.kayit");
        Files.writeString(dosya, "SURUM\t1\nDOKTOR\tx\tAyse\tYilmaz\tyok\tyok\tDahiliye\n", StandardCharsets.UTF_8);
        try {
            new DosyaDeposu().yukle(dosya);
            throw new AssertionError("Bozuk dosya reddedilmeli.");
        } catch (IllegalArgumentException ex) {
            kontrol(ex.getMessage().contains("Kurum bulunamadi"), ex.getMessage());
        }
    }

    private static SaglikKurumu bul(SistemKaydi kayit, String id) {
        return kayit.getKurumlar().stream()
                .filter(kurum -> kurum.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Kurum yuklenemedi."));
    }

    private static void kontrol(boolean kosul, String mesaj) {
        if (!kosul) {
            throw new AssertionError(mesaj);
        }
    }

    private static boolean hataVerir(Runnable islem) {
        try {
            islem.run();
            return false;
        } catch (RuntimeException ex) {
            return true;
        }
    }

    private DosyaDeposuDenemesi() {
    }
}
