package com.mehdi8s.hastane.ekran;

import com.mehdi8s.hastane.hizmet.GenelMuayene;
import com.mehdi8s.hastane.kayit.SistemKaydi;
import com.mehdi8s.hastane.kisi.Doktor;
import com.mehdi8s.hastane.kisi.Hasta;
import com.mehdi8s.hastane.kisi.SaglikPersoneli;
import com.mehdi8s.hastane.kisi.Uzmanlik;
import com.mehdi8s.hastane.kisi.Yonetici;
import com.mehdi8s.hastane.kurum.Bolum;
import com.mehdi8s.hastane.kurum.Hastane;
import com.mehdi8s.hastane.kurum.Sube;
import com.mehdi8s.hastane.zaman.ZamanAraligi;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalTime;

public final class OrnekSistem {
    private OrnekSistem() {
    }

    public static SistemKaydi olustur() {
        Hastane hastane = new Hastane("Şehir Hastanesi");
        Sube merkez = new Sube("Merkez", "Sakarya");
        hastane.subeEkle(merkez);
        merkez.bolumEkle(new Bolum("Dahiliye"));
        merkez.calismaSaatiEkle(new ZamanAraligi(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)));
        GenelMuayene muayene = new GenelMuayene("Dahiliye Muayenesi", 20, new BigDecimal("750.50"));
        merkez.hizmetEkle(muayene);

        Doktor doktor = new Doktor("Ayşe", "Yılmaz", hastane, merkez, new Uzmanlik("Dahiliye"));
        doktor.hizmetEkle(muayene);
        doktor.uygunlukEkle(new ZamanAraligi(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)));

        SistemKaydi kayit = new SistemKaydi();
        kayit.kurumEkle(hastane);
        kayit.hastaEkle(new Hasta("Özge", "Çelik"));
        kayit.doktorEkle(doktor);
        kayit.personelEkle(new SaglikPersoneli("Ece", "Akın", hastane, merkez, "Başhemşire"));
        kayit.yoneticiEkle(new Yonetici("Selin", "Arslan", hastane));
        return kayit;
    }
}
