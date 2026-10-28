package com.mehdi8s.hastane.kayit;

import com.mehdi8s.hastane.hizmet.GenelMuayene;
import com.mehdi8s.hastane.hizmet.Laboratuvar;
import com.mehdi8s.hastane.hizmet.Radyoloji;
import com.mehdi8s.hastane.hizmet.SaglikHizmeti;
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
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class DosyaDeposu {
    private static final String SURUM = "1";

    public void kaydet(SistemKaydi kayit, Path dosya) throws IOException {
        if (kayit == null) {
            throw new IllegalArgumentException("Sistem kaydi bos olamaz.");
        }
        if (dosya == null) {
            throw new IllegalArgumentException("Dosya bos olamaz.");
        }
        StringBuilder metin = new StringBuilder();
        satir(metin, "SURUM", SURUM);
        for (SaglikKurumu kurum : kayit.getKurumlar()) {
            satir(metin, kurum.kurumTuru().equals("Klinik") ? "KLINIK" : "HASTANE", kurum.getId(), kurum.getAd());
            for (Sube sube : kurum.getSubeler()) {
                satir(metin, "SUBE", sube.getId(), kurum.getId(), sube.getAd(), sube.getAdres());
                for (Bolum bolum : sube.getBolumler()) {
                    satir(metin, "BOLUM", bolum.getId(), sube.getId(), bolum.getAd());
                }
                for (ZamanAraligi saat : sube.getCalismaSaatleri()) {
                    satir(metin, "CALISMA", sube.getId(), saat.getGun().name(), saat.getBaslangic().toString(), saat.getBitis().toString());
                }
                for (SaglikHizmeti hizmet : sube.getHizmetler()) {
                    satir(metin, "HIZMET", hizmet.hizmetTuru(), hizmet.getId(), sube.getId(), hizmet.getAd(),
                            Integer.toString(hizmet.getSureDakika()), hizmet.getUcret().toPlainString());
                }
            }
        }
        for (Hasta hasta : kayit.getHastalar()) {
            satir(metin, "HASTA", hasta.getId(), hasta.getAd(), hasta.getSoyad());
        }
        for (Doktor doktor : kayit.getDoktorlar()) {
            satir(metin, "DOKTOR", doktor.getId(), doktor.getAd(), doktor.getSoyad(), doktor.getKurum().getId(),
                    doktor.getSube().getId(), doktor.getUzmanlik().getAd());
            for (SaglikHizmeti hizmet : doktor.getHizmetler()) {
                satir(metin, "DOKTOR_HIZMET", doktor.getId(), hizmet.getId());
            }
            for (ZamanAraligi uygunluk : doktor.getUygunluklar()) {
                satir(metin, "DOKTOR_UYGUNLUK", doktor.getId(), uygunluk.getGun().name(),
                        uygunluk.getBaslangic().toString(), uygunluk.getBitis().toString());
            }
        }
        for (SaglikPersoneli personel : kayit.getPersoneller()) {
            satir(metin, "PERSONEL", personel.getId(), personel.getAd(), personel.getSoyad(),
                    personel.getKurum().getId(), personel.getSube().getId(), personel.getGorev());
        }
        for (Yonetici yonetici : kayit.getYoneticiler()) {
            satir(metin, "YONETICI", yonetici.getId(), yonetici.getAd(), yonetici.getSoyad(), yonetici.getKurum().getId());
        }
        for (Randevu randevu : kayit.getRandevuServisi().getRandevular()) {
            if (!kayit.getHastalar().contains(randevu.getHasta()) || !kayit.getDoktorlar().contains(randevu.getDoktor())) {
                throw new IllegalArgumentException("Randevunun hasta ve doktoru sisteme kayitli olmali.");
            }
            satir(metin, "RANDEVU", randevu.getId(), randevu.getHasta().getId(), randevu.getDoktor().getId(),
                    randevu.getSube().getId(), randevu.getHizmetAdi(), randevu.getHizmetTuru(),
                    Integer.toString(randevu.getSureDakika()), randevu.getUcret().toPlainString(),
                    randevu.getBaslangic().toString(), randevu.getDurum().name());
        }
        if (dosya.getParent() != null) {
            Files.createDirectories(dosya.getParent());
        }
        Files.writeString(dosya, metin.toString(), StandardCharsets.UTF_8);
    }

    public SistemKaydi yukle(Path dosya) throws IOException {
        if (dosya == null) {
            throw new IllegalArgumentException("Dosya bos olamaz.");
        }
        Map<String, List<String[]>> gruplar = grupla(Files.readString(dosya, StandardCharsets.UTF_8));
        Map<String, SaglikKurumu> kurumlar = new LinkedHashMap<>();
        Map<String, Sube> subeler = new LinkedHashMap<>();
        Map<String, SaglikHizmeti> hizmetler = new LinkedHashMap<>();
        Map<String, Hasta> hastalar = new LinkedHashMap<>();
        Map<String, Doktor> doktorlar = new LinkedHashMap<>();
        SistemKaydi kayit = new SistemKaydi();

        for (String[] satir : gruplar.getOrDefault("HASTANE", List.of())) {
            bekle(satir, 3);
            Hastane hastane = Hastane.yukle(satir[1], satir[2]);
            koy(kurumlar, hastane.getId(), hastane, "Hastane");
            kayit.kurumEkle(hastane);
        }
        for (String[] satir : gruplar.getOrDefault("KLINIK", List.of())) {
            bekle(satir, 3);
            Klinik klinik = Klinik.yukle(satir[1], satir[2]);
            koy(kurumlar, klinik.getId(), klinik, "Klinik");
            kayit.kurumEkle(klinik);
        }
        for (String[] satir : gruplar.getOrDefault("SUBE", List.of())) {
            bekle(satir, 5);
            SaglikKurumu kurum = bul(kurumlar, satir[2], "Kurum");
            Sube sube = Sube.yukle(satir[1], satir[3], satir[4]);
            kurum.subeEkle(sube);
            koy(subeler, sube.getId(), sube, "Sube");
        }
        for (String[] satir : gruplar.getOrDefault("BOLUM", List.of())) {
            bekle(satir, 4);
            bul(subeler, satir[2], "Sube").bolumEkle(Bolum.yukle(satir[1], satir[3]));
        }
        for (String[] satir : gruplar.getOrDefault("CALISMA", List.of())) {
            bekle(satir, 5);
            bul(subeler, satir[1], "Sube").calismaSaatiEkle(zaman(satir[2], satir[3], satir[4]));
        }
        for (String[] satir : gruplar.getOrDefault("HIZMET", List.of())) {
            bekle(satir, 7);
            Sube sube = bul(subeler, satir[3], "Sube");
            SaglikHizmeti hizmet = hizmetYukle(satir[1], satir[2], satir[4], Integer.parseInt(satir[5]), new BigDecimal(satir[6]));
            sube.hizmetEkle(hizmet);
            koy(hizmetler, hizmet.getId(), hizmet, "Hizmet");
        }
        for (String[] satir : gruplar.getOrDefault("HASTA", List.of())) {
            bekle(satir, 4);
            Hasta hasta = Hasta.yukle(satir[1], satir[2], satir[3]);
            koy(hastalar, hasta.getId(), hasta, "Hasta");
            kayit.hastaEkle(hasta);
        }
        for (String[] satir : gruplar.getOrDefault("DOKTOR", List.of())) {
            bekle(satir, 7);
            Doktor doktor = Doktor.yukle(satir[1], satir[2], satir[3], bul(kurumlar, satir[4], "Kurum"),
                    bul(subeler, satir[5], "Sube"), new Uzmanlik(satir[6]));
            koy(doktorlar, doktor.getId(), doktor, "Doktor");
            kayit.doktorEkle(doktor);
        }
        for (String[] satir : gruplar.getOrDefault("DOKTOR_HIZMET", List.of())) {
            bekle(satir, 3);
            bul(doktorlar, satir[1], "Doktor").hizmetEkle(bul(hizmetler, satir[2], "Hizmet"));
        }
        for (String[] satir : gruplar.getOrDefault("DOKTOR_UYGUNLUK", List.of())) {
            bekle(satir, 5);
            bul(doktorlar, satir[1], "Doktor").uygunlukEkle(zaman(satir[2], satir[3], satir[4]));
        }
        for (String[] satir : gruplar.getOrDefault("PERSONEL", List.of())) {
            bekle(satir, 7);
            kayit.personelEkle(SaglikPersoneli.yukle(satir[1], satir[2], satir[3],
                    bul(kurumlar, satir[4], "Kurum"), bul(subeler, satir[5], "Sube"), satir[6]));
        }
        for (String[] satir : gruplar.getOrDefault("YONETICI", List.of())) {
            bekle(satir, 5);
            kayit.yoneticiEkle(Yonetici.yukle(satir[1], satir[2], satir[3], bul(kurumlar, satir[4], "Kurum")));
        }
        for (String[] satir : gruplar.getOrDefault("RANDEVU", List.of())) {
            bekle(satir, 11);
            Randevu randevu = Randevu.yukle(
                    satir[1],
                    bul(hastalar, satir[2], "Hasta"),
                    bul(doktorlar, satir[3], "Doktor"),
                    bul(subeler, satir[4], "Sube"),
                    satir[5],
                    satir[6],
                    Integer.parseInt(satir[7]),
                    new BigDecimal(satir[8]),
                    LocalDateTime.parse(satir[9]),
                    RandevuDurumu.valueOf(satir[10]));
            kayit.getRandevuServisi().yukle(randevu);
        }
        return kayit;
    }

    private static Map<String, List<String[]>> grupla(String metin) {
        Map<String, List<String[]>> gruplar = new LinkedHashMap<>();
        boolean surumGoruldu = false;
        for (String satir : metin.split("\n", -1)) {
            if (satir.isBlank()) {
                continue;
            }
            String[] alan = cozSatir(satir);
            if (alan[0].equals("SURUM")) {
                if (surumGoruldu || alan.length != 2 || !SURUM.equals(alan[1])) {
                    throw new IllegalArgumentException("Desteklenmeyen kayit surumu.");
                }
                surumGoruldu = true;
                continue;
            }
            if (!bilinen(alan[0])) {
                throw new IllegalArgumentException("Bilinmeyen kayit satiri: " + alan[0]);
            }
            gruplar.computeIfAbsent(alan[0], anahtar -> new ArrayList<>()).add(alan);
        }
        if (!surumGoruldu) {
            throw new IllegalArgumentException("Kayit surumu yok.");
        }
        return gruplar;
    }

    private static boolean bilinen(String tur) {
        return tur.equals("HASTANE") || tur.equals("KLINIK") || tur.equals("SUBE") || tur.equals("BOLUM")
                || tur.equals("CALISMA") || tur.equals("HIZMET") || tur.equals("HASTA") || tur.equals("DOKTOR")
                || tur.equals("DOKTOR_HIZMET") || tur.equals("DOKTOR_UYGUNLUK") || tur.equals("PERSONEL")
                || tur.equals("YONETICI") || tur.equals("RANDEVU");
    }

    private static SaglikHizmeti hizmetYukle(String tur, String id, String ad, int sure, BigDecimal ucret) {
        return switch (tur) {
            case "Genel Muayene" -> GenelMuayene.yukle(id, ad, sure, ucret);
            case "Radyoloji" -> Radyoloji.yukle(id, ad, sure, ucret);
            case "Laboratuvar" -> Laboratuvar.yukle(id, ad, sure, ucret);
            default -> throw new IllegalArgumentException("Bilinmeyen hizmet turu: " + tur);
        };
    }

    private static ZamanAraligi zaman(String gun, String baslangic, String bitis) {
        return new ZamanAraligi(DayOfWeek.valueOf(gun), LocalTime.parse(baslangic), LocalTime.parse(bitis));
    }

    private static void bekle(String[] satir, int uzunluk) {
        if (satir.length != uzunluk) {
            throw new IllegalArgumentException(satir[0] + " satiri eksik.");
        }
    }

    private static <T> T bul(Map<String, T> harita, String id, String ad) {
        T kayit = harita.get(id);
        if (kayit == null) {
            throw new IllegalArgumentException(ad + " bulunamadi: " + id);
        }
        return kayit;
    }

    private static <T> void koy(Map<String, T> harita, String id, T kayit, String ad) {
        if (harita.put(id, kayit) != null) {
            throw new IllegalArgumentException(ad + " kimligi tekrar ediyor: " + id);
        }
    }

    private static void satir(StringBuilder metin, String... alanlar) {
        for (int i = 0; i < alanlar.length; i++) {
            if (i > 0) {
                metin.append('\t');
            }
            metin.append(kacis(alanlar[i]));
        }
        metin.append('\n');
    }

    private static String kacis(String deger) {
        if (deger == null) {
            throw new IllegalArgumentException("Kayit alani bos olamaz.");
        }
        return deger.replace("\\", "\\\\").replace("\t", "\\t").replace("\n", "\\n").replace("\r", "\\r");
    }

    private static String[] cozSatir(String satir) {
        String[] ham = satir.split("\t", -1);
        String[] alan = new String[ham.length];
        for (int i = 0; i < ham.length; i++) {
            alan[i] = coz(ham[i]);
        }
        return alan;
    }

    private static String coz(String deger) {
        StringBuilder metin = new StringBuilder();
        for (int i = 0; i < deger.length(); i++) {
            char karakter = deger.charAt(i);
            if (karakter != '\\') {
                metin.append(karakter);
                continue;
            }
            if (i + 1 >= deger.length()) {
                throw new IllegalArgumentException("Kayit kacisi eksik.");
            }
            char sonraki = deger.charAt(++i);
            switch (sonraki) {
                case '\\' -> metin.append('\\');
                case 't' -> metin.append('\t');
                case 'n' -> metin.append('\n');
                case 'r' -> metin.append('\r');
                default -> throw new IllegalArgumentException("Bilinmeyen kayit kacisi.");
            }
        }
        return metin.toString();
    }
}
