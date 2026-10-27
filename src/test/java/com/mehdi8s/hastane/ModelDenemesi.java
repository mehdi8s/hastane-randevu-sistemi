package com.mehdi8s.hastane;

import com.mehdi8s.hastane.hizmet.GenelMuayene;
import com.mehdi8s.hastane.kisi.Doktor;
import com.mehdi8s.hastane.kisi.Hasta;
import com.mehdi8s.hastane.kisi.Rol;
import com.mehdi8s.hastane.kisi.Uzmanlik;
import com.mehdi8s.hastane.kurum.Bolum;
import com.mehdi8s.hastane.kurum.Hastane;
import com.mehdi8s.hastane.kurum.Klinik;
import com.mehdi8s.hastane.kurum.Sube;
import com.mehdi8s.hastane.randevu.Randevu;
import com.mehdi8s.hastane.randevu.RandevuDurumu;
import com.mehdi8s.hastane.zaman.ZamanAraligi;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class ModelDenemesi {
    public static void main(String[] args) {
        Hastane hastane = new Hastane("Sehir Hastanesi");
        Sube merkez = new Sube("Merkez", "Sakarya");
        Sube ilce = new Sube("Ilce", "Adapazari");
        hastane.subeEkle(merkez);
        hastane.subeEkle(ilce);
        kontrol(hastane.getSubeler().size() == 2, "Hastane birden fazla sube tasimali.");

        Klinik klinik = new Klinik("Mahalle Klinigi");
        klinik.subeEkle(new Sube("Tek Sube", "Serdivan"));
        kontrol(hataVerir(() -> klinik.subeEkle(new Sube("Ikinci", "Serdivan"))),
                "Klinik ikinci subeyi reddetmeli.");

        merkez.bolumEkle(new Bolum("Dahiliye"));
        merkez.calismaSaatiEkle(new ZamanAraligi(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)));
        GenelMuayene muayene = new GenelMuayene("Dahiliye Muayenesi", 20, new BigDecimal("750.00"));
        merkez.hizmetEkle(muayene);

        Doktor doktor = new Doktor("Ayse", "Yilmaz", hastane, merkez, new Uzmanlik("Dahiliye"));
        doktor.hizmetEkle(muayene);
        doktor.uygunlukEkle(new ZamanAraligi(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)));
        kontrol(hataVerir(() -> doktor.uygunlukEkle(
                new ZamanAraligi(DayOfWeek.MONDAY, LocalTime.of(8, 0), LocalTime.of(10, 0)))),
                "Sube saati disindaki uygunluk reddedilmeli.");

        Hasta hasta = new Hasta("Mehmet", "Demir");
        kontrol(hasta.rol() == Rol.HASTA && doktor.rol() == Rol.DOKTOR, "Roller siniftan gelmeli.");

        Randevu randevu = new Randevu(
                hasta,
                doktor,
                merkez,
                muayene,
                LocalDateTime.of(2026, 11, 2, 9, 0));
        kontrol(randevu.getDurum() == RandevuDurumu.ONAY_BEKLIYOR, "Yeni randevu onay beklemeli.");
        kontrol(randevu.getSureDakika() == 20, "Sure randevuya kopyalanmali.");
        kontrol(randevu.bitis().equals(LocalDateTime.of(2026, 11, 2, 9, 20)), "Bitis suresine gore hesaplanmali.");
        kontrol(hataVerir(randevu::tamamla), "Onaysiz randevu tamamlanamamali.");
        randevu.onayla();
        kontrol(randevu.getDurum() == RandevuDurumu.ONAYLANDI, "Onay gecisi calismali.");
        kontrol(hataVerir(randevu::onayla), "Ikinci onay gecersiz olmali.");

        RandevuServisiDenemesi.main(args);
        System.out.println("Model denemesi gecti.");
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

    private ModelDenemesi() {
    }
}
