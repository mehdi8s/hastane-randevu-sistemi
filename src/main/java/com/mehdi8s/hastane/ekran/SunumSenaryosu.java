package com.mehdi8s.hastane.ekran;

import com.mehdi8s.hastane.hizmet.GenelMuayene;
import com.mehdi8s.hastane.hizmet.Radyoloji;
import com.mehdi8s.hastane.kayit.SistemKaydi;
import com.mehdi8s.hastane.kisi.Doktor;
import com.mehdi8s.hastane.kisi.Hasta;
import com.mehdi8s.hastane.kisi.SaglikPersoneli;
import com.mehdi8s.hastane.kisi.Uzmanlik;
import com.mehdi8s.hastane.kisi.Yonetici;
import com.mehdi8s.hastane.kurum.Bolum;
import com.mehdi8s.hastane.kurum.Hastane;
import com.mehdi8s.hastane.kurum.Klinik;
import com.mehdi8s.hastane.kurum.Sube;
import com.mehdi8s.hastane.randevu.Randevu;
import com.mehdi8s.hastane.zaman.ZamanAraligi;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class SunumSenaryosu {
    private SunumSenaryosu() {
    }

    public static SistemKaydi olustur() {
        Hastane hastane = new Hastane("Şehir Hastanesi");
        Sube merkez = new Sube("Merkez", "Sakarya");
        Sube ilce = new Sube("İlçe", "Adapazarı");
        hastane.subeEkle(merkez);
        hastane.subeEkle(ilce);
        merkez.bolumEkle(new Bolum("Dahiliye"));
        ilce.bolumEkle(new Bolum("Radyoloji"));
        ZamanAraligi pazartesi = new ZamanAraligi(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0));
        merkez.calismaSaatiEkle(pazartesi);
        ilce.calismaSaatiEkle(pazartesi);
        GenelMuayene muayene = new GenelMuayene("Dahiliye Muayenesi", 20, new BigDecimal("750.50"));
        Radyoloji rontgen = new Radyoloji("Akciğer Grafisi", 15, new BigDecimal("400.00"));
        merkez.hizmetEkle(muayene);
        ilce.hizmetEkle(rontgen);

        Doktor dahiliye = new Doktor("Ayşe", "Yılmaz", hastane, merkez, new Uzmanlik("Dahiliye"));
        dahiliye.hizmetEkle(muayene);
        dahiliye.uygunlukEkle(new ZamanAraligi(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)));
        Doktor radyoloji = new Doktor("Can", "Kaya", hastane, ilce, new Uzmanlik("Radyoloji"));
        radyoloji.hizmetEkle(rontgen);
        radyoloji.uygunlukEkle(new ZamanAraligi(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)));

        Klinik klinik = new Klinik("Mahalle Kliniği");
        klinik.subeEkle(new Sube("Tek Şube", "Serdivan"));

        Hasta ozge = new Hasta("Özge", "Çelik");
        Hasta ali = new Hasta("Ali", "Demir");
        Yonetici selin = new Yonetici("Selin", "Arslan", hastane);

        SistemKaydi kayit = new SistemKaydi();
        kayit.kurumEkle(hastane);
        kayit.kurumEkle(klinik);
        kayit.hastaEkle(ozge);
        kayit.hastaEkle(ali);
        kayit.doktorEkle(dahiliye);
        kayit.doktorEkle(radyoloji);
        kayit.personelEkle(new SaglikPersoneli("Ece", "Akın", hastane, merkez, "Başhemşire"));
        kayit.yoneticiEkle(selin);
        kayit.yoneticiEkle(new Yonetici("Kemal", "Aslan", hastane));
        kayit.yoneticiEkle(new Yonetici("Leyla", "Koç", klinik));

        kayit.getRandevuServisi().olustur(ozge, dahiliye, muayene, LocalDateTime.of(2026, 11, 2, 9, 0));
        Randevu onayli = kayit.getRandevuServisi().olustur(ali, radyoloji, rontgen, LocalDateTime.of(2026, 11, 2, 9, 0));
        kayit.getRandevuServisi().onayla(selin, onayli);
        return kayit;
    }
}
