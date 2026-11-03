package com.mehdi8s.hastane;

import com.mehdi8s.hastane.ekran.GirisEkrani;
import com.mehdi8s.hastane.ekran.Konsol;
import com.mehdi8s.hastane.ekran.OrnekSistem;
import com.mehdi8s.hastane.ekran.SunumSenaryosu;
import com.mehdi8s.hastane.kurum.Klinik;
import com.mehdi8s.hastane.kurum.Sube;
import com.mehdi8s.hastane.kayit.DosyaDeposu;
import com.mehdi8s.hastane.kayit.SistemKaydi;
import com.mehdi8s.hastane.randevu.Randevu;
import com.mehdi8s.hastane.randevu.RandevuDurumu;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public final class EkranDenemesi {
    public static void main(String[] args) {
        try {
            ornekKayitDosyadaDurur();
            rollerKendiMenuleriniKullanir();
            yoneticiKurumuTanimlar();
            yoneticiKisiKaydeder();
            yoneticiIkinciYoneticiKaydeder();
            sunumSenaryosuHazirDurur();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
        System.out.println("Ekran denemesi gecti.");
    }

    private static void ornekKayitDosyadaDurur() throws IOException {
        Path dosya = Files.createTempDirectory("ekran-kayit").resolve("sistem.kayit");
        DosyaDeposu depo = new DosyaDeposu();
        depo.kaydet(OrnekSistem.olustur(), dosya);
        SistemKaydi yuklenen = depo.yukle(dosya);
        kontrol(yuklenen.getHastalar().get(0).tamAd().equals("Özge Çelik"), "Ornek hasta yuklenmeli.");
        kontrol(yuklenen.getDoktorlar().size() == 1, "Ornek doktor yuklenmeli.");
    }

    private static void rollerKendiMenuleriniKullanir() {
        SistemKaydi kayit = OrnekSistem.olustur();
        int[] kayitSayisi = {0};
        SenaryoKonsolu konsol = new SenaryoKonsolu(
                "x",
                "1",
                "1", "1", "1", "2026-11-02 09:00",
                "1", "1", "1", "2026-11-02 09:00",
                "1", "1", "1", "2026-11-02 09:20",
                "2",
                "0",
                "3",
                "1",
                "2",
                "0",
                "2",
                "2", "1",
                "0",
                "4",
                "3", "2",
                "0",
                "1",
                "3", "1",
                "0",
                "0");
        new GirisEkrani(kayit, () -> kayitSayisi[0]++).calistir(konsol);
        kontrol(konsol.kalan() == 0, "Senaryo girdisi artmis veya eksik kaldi.");

        String metin = konsol.metin();
        kontrol(metin.contains("Gecersiz secim."), "Gecersiz secim bildirilmeli.");
        kontrol(metin.contains("baska bir randevusu"), "Cakisma ekranda gorunmeli.");
        kontrol(metin.contains("Tamam: ONAYLANDI"), "Doktor onayi ekranda gorunmeli.");
        kontrol(metin.contains("Tamam: REDDEDILDI"), "Yonetici reddi ekranda gorunmeli.");
        kontrol(metin.contains("Randevu iptal edildi."), "Hasta iptali ekranda gorunmeli.");

        List<Randevu> randevular = kayit.getRandevuServisi().getRandevular();
        kontrol(randevular.size() == 2, "Yalnizca gecerli talepler saklanmali.");
        kontrol(randevular.get(0).getDurum() == RandevuDurumu.IPTAL, "Onaylanan randevu hasta tarafindan iptal edilmeli.");
        kontrol(randevular.get(1).getDurum() == RandevuDurumu.REDDEDILDI, "Ikinci randevu yonetici tarafindan reddedilmeli.");
        kontrol(kayitSayisi[0] == 5, "Yalnizca durumu degisen islemler kaydedilmeli.");
    }

    private static void yoneticiKurumuTanimlar() {
        SistemKaydi kayit = OrnekSistem.olustur();
        int[] kayitSayisi = {0};
        SenaryoKonsolu konsol = new SenaryoKonsolu(
                "4",
                "6", "Ilce", "Adapazari",
                "8", "1", "2", "09:00", "17:00",
                "7", "1", "Radyoloji",
                "9", "1", "3", "Kan tahlili", "10", "200.00",
                "10", "1", "2",
                "11", "1", "3", "09:00", "12:00",
                "11", "1", "2", "09:00", "12:00",
                "0",
                "0");
        new GirisEkrani(kayit, () -> kayitSayisi[0]++).calistir(konsol);
        kontrol(konsol.kalan() == 0, "Tanim senaryosu artmis veya eksik kaldi.");
        kontrol(konsol.metin().contains("subenin calisma saati icinde olmali"),
                "Sube saati disindaki uygunluk ekranda reddedilmeli.");
        kontrol(kayit.getKurumlar().get(0).getSubeler().size() == 2, "Hastane ikinci subeyi almali.");
        kontrol(kayit.getKurumlar().get(0).getSubeler().get(0).getBolumler().size() == 2,
                "Yeni bolum subeye eklenmeli.");
        kontrol(kayit.getKurumlar().get(0).getSubeler().get(0).getHizmetler().size() == 2,
                "Laboratuvar hizmeti subeye eklenmeli.");
        kontrol(kayit.getDoktorlar().get(0).getHizmetler().size() == 2, "Yeni hizmet doktora eklenmeli.");
        kontrol(kayit.getDoktorlar().get(0).getUygunluklar().size() == 2, "Sali uygunlugu eklenmeli.");
        kontrol(kayitSayisi[0] == 6, "Basarisiz uygunluk kaydedilmemeli.");
    }

    private static void yoneticiKisiKaydeder() {
        SistemKaydi kayit = OrnekSistem.olustur();
        int[] kayitSayisi = {0};
        SenaryoKonsolu konsol = new SenaryoKonsolu(
                "4",
                "12", "", "Demir",
                "12", "Ali", "Demir",
                "13", "Can", "Kaya", "1", "Kardiyoloji",
                "14", "Deniz", "Koc", "1", "Hemsire",
                "0",
                "2",
                "2",
                "0",
                "0");
        new GirisEkrani(kayit, () -> kayitSayisi[0]++).calistir(konsol);
        kontrol(konsol.kalan() == 0, "Kisi senaryosu artmis veya eksik kaldi.");
        kontrol(konsol.metin().contains("Ad bos olamaz."), "Bos hasta adi reddedilmeli.");
        kontrol(konsol.metin().contains("Ali Demir (HASTA)"), "Yeni hasta girise dusmeli.");
        kontrol(kayit.getHastalar().size() == 2, "Hasta sisteme eklenmeli.");
        kontrol(kayit.getDoktorlar().size() == 2, "Doktor sisteme eklenmeli.");
        kontrol(kayit.getDoktorlar().get(1).getUzmanlik().getAd().equals("Kardiyoloji"),
                "Doktorun uzmanligi korunmali.");
        kontrol(kayit.getPersoneller().size() == 2, "Personel sisteme eklenmeli.");
        kontrol(kayit.getPersoneller().get(1).getGorev().equals("Hemsire"), "Personel gorevi korunmali.");
        kontrol(kayit.getDoktorlar().get(1).getKurum().equals(kayit.getKurumlar().get(0)),
                "Yeni doktor yonetici kurumuna baglanmali.");
        kontrol(kayitSayisi[0] == 3, "Bos ad kaydedilmemeli.");
    }

    private static void yoneticiIkinciYoneticiKaydeder() {
        SistemKaydi kayit = OrnekSistem.olustur();
        int[] kayitSayisi = {0};
        SenaryoKonsolu konsol = new SenaryoKonsolu(
                "4",
                "15", "", "Aslan",
                "15", "Kemal", "Aslan",
                "0",
                "5",
                "1",
                "0",
                "0");
        new GirisEkrani(kayit, () -> kayitSayisi[0]++).calistir(konsol);
        kontrol(konsol.kalan() == 0, "Yonetici senaryosu artmis veya eksik kaldi.");
        kontrol(konsol.metin().contains("Ad bos olamaz."), "Bos yonetici adi reddedilmeli.");
        kontrol(konsol.metin().contains("Kemal Aslan (YONETICI)"), "Ikinci yonetici girise dusmeli.");
        kontrol(kayit.getYoneticiler().size() == 2, "Ikinci yonetici sisteme eklenmeli.");
        kontrol(kayit.getYoneticiler().get(1).getKurum().equals(kayit.getKurumlar().get(0)),
                "Yeni yonetici ayni kuruma baglanmali.");
        kontrol(kayitSayisi[0] == 1, "Bos yonetici adi kaydedilmemeli.");
    }

    private static void sunumSenaryosuHazirDurur() throws IOException {
        SistemKaydi kayit = SunumSenaryosu.olustur();
        kontrol(kayit.getKurumlar().size() == 2, "Sunumda hastane ve klinik olmali.");
        kontrol(kayit.getKurumlar().get(0).getSubeler().size() == 2, "Hastanenin iki subesi olmali.");
        Klinik klinik = (Klinik) kayit.getKurumlar().get(1);
        kontrol(hataVerir(() -> klinik.subeEkle(new Sube("Ikinci", "Serdivan"))),
                "Klinik ikinci subeyi reddetmeli.");
        kontrol(kayit.getYoneticiler().size() == 3, "Hastanede iki, klinikte bir yonetici olmali.");
        kontrol(kayit.getRandevuServisi().getRandevular().get(0).getDurum() == RandevuDurumu.ONAY_BEKLIYOR,
                "Ilk randevu onay beklemeli.");
        kontrol(kayit.getRandevuServisi().getRandevular().get(1).getDurum() == RandevuDurumu.ONAYLANDI,
                "Ikinci randevu onaylanmis olmali.");

        Path dosya = Files.createTempDirectory("sunum-kayit").resolve("sistem.kayit");
        DosyaDeposu depo = new DosyaDeposu();
        depo.kaydet(kayit, dosya);
        SistemKaydi yuklenen = depo.yukle(dosya);
        kontrol(yuklenen.getKurumlar().get(0).getSubeler().size() == 2, "Iki sube dosyadan donmeli.");
        kontrol(yuklenen.getRandevuServisi().getRandevular().get(1).getDurum() == RandevuDurumu.ONAYLANDI,
                "Onayli randevu dosyadan donmeli.");

        SenaryoKonsolu konsol = new SenaryoKonsolu("7", "2", "1", "0", "0");
        new GirisEkrani(yuklenen, () -> {
        }).calistir(konsol);
        kontrol(konsol.kalan() == 0, "Sunum senaryosu artmis veya eksik kaldi.");
        kontrol(yuklenen.getRandevuServisi().getRandevular().get(0).getDurum() == RandevuDurumu.ONAYLANDI,
                "Hastanedeki ikinci yonetici onayi verebilmeli.");
    }

    private static boolean hataVerir(Runnable islem) {
        try {
            islem.run();
            return false;
        } catch (RuntimeException ex) {
            return true;
        }
    }

    private static void kontrol(boolean kosul, String mesaj) {
        if (!kosul) {
            throw new AssertionError(mesaj);
        }
    }

    private EkranDenemesi() {
    }

    private static final class SenaryoKonsolu implements Konsol {
        private final Queue<String> girdiler;
        private final List<String> satirlar = new ArrayList<>();

        private SenaryoKonsolu(String... girdiler) {
            this.girdiler = new ArrayDeque<>(Arrays.asList(girdiler));
        }

        @Override
        public void yaz(String metin) {
            satirlar.add(metin);
        }

        @Override
        public String oku(String istem) {
            yaz(istem + ":");
            if (girdiler.isEmpty()) {
                throw new AssertionError("Girdi bitti.\n" + metin());
            }
            String cevap = girdiler.remove();
            yaz("> " + cevap);
            return cevap;
        }

        private int kalan() {
            return girdiler.size();
        }

        private String metin() {
            return String.join("\n", satirlar);
        }
    }
}
