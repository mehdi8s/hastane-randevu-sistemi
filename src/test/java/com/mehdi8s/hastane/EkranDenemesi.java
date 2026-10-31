package com.mehdi8s.hastane;

import com.mehdi8s.hastane.ekran.GirisEkrani;
import com.mehdi8s.hastane.ekran.Konsol;
import com.mehdi8s.hastane.ekran.OrnekSistem;
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
