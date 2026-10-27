package com.mehdi8s.hastane;

import com.mehdi8s.hastane.hizmet.GenelMuayene;
import com.mehdi8s.hastane.kisi.Doktor;
import com.mehdi8s.hastane.kisi.Hasta;
import com.mehdi8s.hastane.kisi.SaglikPersoneli;
import com.mehdi8s.hastane.kisi.Uzmanlik;
import com.mehdi8s.hastane.kisi.Yonetici;
import com.mehdi8s.hastane.kurum.Hastane;
import com.mehdi8s.hastane.kurum.Sube;
import com.mehdi8s.hastane.randevu.Randevu;
import com.mehdi8s.hastane.randevu.RandevuDurumu;
import com.mehdi8s.hastane.randevu.RandevuServisi;
import com.mehdi8s.hastane.zaman.ZamanAraligi;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;

public final class RandevuServisiDenemesi {
    public static void main(String[] args) {
        uygunSaatKabulEdilir();
        uygunlugunBitisindeKabulEdilir();
        uygunlukDisiReddedilir();
        cakisanReddedilirBitisikKabulEdilir();
        iptalVeRedSaatiBosaltir();
        ayniHastaBaskaDoktordaCakisamaz();
        onayYetkisiDoktordaVeYoneticidedir();
        iptalYetkisiRandevununHastasindadir();
        tamamlananSaatKapaliKalir();
        System.out.println("Randevu servisi denemesi gecti.");
    }

    private static void uygunSaatKabulEdilir() {
        Sahne sahne = sahne();
        Randevu randevu = sahne.servis.olustur(sahne.hasta, sahne.doktor, sahne.muayene, saat(9, 0));
        kontrol(randevu.getDurum() == RandevuDurumu.ONAY_BEKLIYOR, "Servisten cikan randevu onay beklemeli.");
        kontrol(sahne.servis.getRandevular().size() == 1, "Randevu serviste saklanmali.");
    }

    private static void uygunlugunBitisindeKabulEdilir() {
        Sahne sahne = sahne();
        Randevu randevu = sahne.servis.olustur(sahne.hasta, sahne.doktor, sahne.muayene, saat(11, 40));
        kontrol(randevu.bitis().equals(saat(12, 0)), "Yirmi dakikalik randevu uygunlugun bitisinde kapanmali.");
    }

    private static void uygunlukDisiReddedilir() {
        Sahne sahne = sahne();
        kontrol(hataVerir(() -> sahne.servis.olustur(sahne.hasta, sahne.doktor, sahne.muayene, saat(11, 50))),
                "Uygunlugun disina tasan randevu reddedilmeli.");
        kontrol(hataVerir(() -> sahne.servis.olustur(sahne.hasta, sahne.doktor, sahne.muayene, saat(8, 50))),
                "Uygunluktan once baslayan randevu reddedilmeli.");
        LocalDateTime sali = LocalDateTime.of(2026, 11, 3, 9, 0);
        kontrol(hataVerir(() -> sahne.servis.olustur(sahne.hasta, sahne.doktor, sahne.muayene, sali)),
                "Doktorun calismadigi gun reddedilmeli.");
    }

    private static void cakisanReddedilirBitisikKabulEdilir() {
        Sahne sahne = sahne();
        sahne.servis.olustur(sahne.hasta, sahne.doktor, sahne.muayene, saat(9, 0));
        Hasta digerHasta = new Hasta("Ali", "Celik");
        kontrol(hataVerir(() -> sahne.servis.olustur(digerHasta, sahne.doktor, sahne.muayene, saat(9, 10))),
                "Ust uste binen randevu reddedilmeli.");
        Randevu bitisik = sahne.servis.olustur(digerHasta, sahne.doktor, sahne.muayene, saat(9, 20));
        kontrol(bitisik.getBaslangic().equals(saat(9, 20)), "Bitisik randevu kabul edilmeli.");
    }

    private static void iptalVeRedSaatiBosaltir() {
        Sahne sahne = sahne();
        Randevu iptal = sahne.servis.olustur(sahne.hasta, sahne.doktor, sahne.muayene, saat(9, 0));
        sahne.servis.iptalEt(sahne.hasta, iptal);
        Hasta sonraGelen = new Hasta("Ali", "Celik");
        sahne.servis.olustur(sonraGelen, sahne.doktor, sahne.muayene, saat(9, 0));

        Randevu red = sahne.servis.olustur(sahne.hasta, sahne.doktor, sahne.muayene, saat(10, 0));
        sahne.servis.reddet(sahne.yonetici, red);
        sahne.servis.olustur(sonraGelen, sahne.doktor, sahne.muayene, saat(10, 0));
    }

    private static void ayniHastaBaskaDoktordaCakisamaz() {
        Sahne sahne = sahne();
        sahne.servis.olustur(sahne.hasta, sahne.doktor, sahne.muayene, saat(9, 0));
        kontrol(hataVerir(() -> sahne.servis.olustur(sahne.hasta, sahne.digerDoktor, sahne.muayene, saat(9, 0))),
                "Hastanin cakisan ikinci randevusu reddedilmeli.");
        Hasta baskaHasta = new Hasta("Ali", "Celik");
        sahne.servis.olustur(baskaHasta, sahne.digerDoktor, sahne.muayene, saat(9, 0));
    }

    private static void onayYetkisiDoktordaVeYoneticidedir() {
        Sahne sahne = sahne();
        Randevu randevu = sahne.servis.olustur(sahne.hasta, sahne.doktor, sahne.muayene, saat(9, 0));
        kontrol(hataVerir(() -> sahne.servis.onayla(sahne.hasta, randevu)), "Hasta randevu onaylayamamali.");
        kontrol(hataVerir(() -> sahne.servis.onayla(sahne.digerDoktor, randevu)),
                "Baska doktor randevuyu onaylayamamali.");
        SaglikPersoneli personel = new SaglikPersoneli("Ece", "Akin", sahne.hastane, sahne.sube, "Hemsire");
        kontrol(hataVerir(() -> sahne.servis.onayla(personel, randevu)), "Saglik personeli onaylayamamali.");

        Hastane baskaKurum = new Hastane("Ilce Hastanesi");
        Yonetici disYonetici = new Yonetici("Kemal", "Aslan", baskaKurum);
        kontrol(hataVerir(() -> sahne.servis.onayla(disYonetici, randevu)),
                "Baska kurumun yoneticisi onaylayamamali.");

        Randevu disari = new Randevu(sahne.hasta, sahne.doktor, sahne.sube, sahne.muayene, saat(10, 0));
        kontrol(hataVerir(() -> sahne.servis.onayla(sahne.doktor, disari)),
                "Serviste olmayan randevu onaylanamamali.");

        sahne.servis.onayla(sahne.doktor, randevu);
        kontrol(randevu.getDurum() == RandevuDurumu.ONAYLANDI, "Ilgili doktor onaylayabilmeli.");

        Randevu ikinci = sahne.servis.olustur(new Hasta("Ali", "Celik"), sahne.doktor, sahne.muayene, saat(10, 0));
        sahne.servis.onayla(sahne.yonetici, ikinci);
        kontrol(ikinci.getDurum() == RandevuDurumu.ONAYLANDI, "Kurum yoneticisi onaylayabilmeli.");
    }

    private static void iptalYetkisiRandevununHastasindadir() {
        Sahne sahne = sahne();
        Randevu randevu = sahne.servis.olustur(sahne.hasta, sahne.doktor, sahne.muayene, saat(9, 0));
        Hasta baskasi = new Hasta("Ali", "Celik");
        kontrol(hataVerir(() -> sahne.servis.iptalEt(baskasi, randevu)), "Baska hasta randevuyu iptal edememeli.");
        sahne.servis.iptalEt(sahne.hasta, randevu);
        kontrol(randevu.getDurum() == RandevuDurumu.IPTAL, "Randevunun hastasi iptal edebilmeli.");
    }

    private static void tamamlananSaatKapaliKalir() {
        Sahne sahne = sahne();
        Randevu randevu = sahne.servis.olustur(sahne.hasta, sahne.doktor, sahne.muayene, saat(9, 0));
        kontrol(hataVerir(() -> sahne.servis.tamamla(sahne.hasta, randevu)), "Hasta randevuyu tamamlayamamali.");
        sahne.servis.onayla(sahne.doktor, randevu);
        sahne.servis.tamamla(sahne.doktor, randevu);
        kontrol(hataVerir(() -> sahne.servis.olustur(new Hasta("Ali", "Celik"), sahne.doktor, sahne.muayene, saat(9, 0))),
                "Tamamlanmis randevunun saati yeniden verilmemeli.");
    }

    private static Sahne sahne() {
        Hastane hastane = new Hastane("Sehir Hastanesi");
        Sube sube = new Sube("Merkez", "Sakarya");
        hastane.subeEkle(sube);
        sube.calismaSaatiEkle(new ZamanAraligi(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(17, 0)));
        GenelMuayene muayene = new GenelMuayene("Dahiliye Muayenesi", 20, new BigDecimal("750.00"));
        sube.hizmetEkle(muayene);

        Doktor doktor = doktorKur(hastane, sube, muayene, "Ayse", "Yilmaz");
        Doktor digerDoktor = doktorKur(hastane, sube, muayene, "Can", "Kaya");
        return new Sahne(
                hastane,
                sube,
                muayene,
                doktor,
                digerDoktor,
                new Hasta("Mehmet", "Demir"),
                new Yonetici("Selin", "Arslan", hastane));
    }

    private static Doktor doktorKur(Hastane hastane, Sube sube, GenelMuayene muayene, String ad, String soyad) {
        Doktor doktor = new Doktor(ad, soyad, hastane, sube, new Uzmanlik("Dahiliye"));
        doktor.hizmetEkle(muayene);
        doktor.uygunlukEkle(new ZamanAraligi(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(12, 0)));
        return doktor;
    }

    private static LocalDateTime saat(int saat, int dakika) {
        return LocalDateTime.of(2026, 11, 2, saat, dakika);
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

    private RandevuServisiDenemesi() {
    }

    private static final class Sahne {
        private final Hastane hastane;
        private final Sube sube;
        private final GenelMuayene muayene;
        private final Doktor doktor;
        private final Doktor digerDoktor;
        private final Hasta hasta;
        private final Yonetici yonetici;
        private final RandevuServisi servis = new RandevuServisi();

        private Sahne(
                Hastane hastane,
                Sube sube,
                GenelMuayene muayene,
                Doktor doktor,
                Doktor digerDoktor,
                Hasta hasta,
                Yonetici yonetici) {
            this.hastane = hastane;
            this.sube = sube;
            this.muayene = muayene;
            this.doktor = doktor;
            this.digerDoktor = digerDoktor;
            this.hasta = hasta;
            this.yonetici = yonetici;
        }
    }
}
