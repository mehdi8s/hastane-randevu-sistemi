package com.mehdi8s.hastane.kayit;

import com.mehdi8s.hastane.kisi.Doktor;
import com.mehdi8s.hastane.kisi.Hasta;
import com.mehdi8s.hastane.kisi.SaglikPersoneli;
import com.mehdi8s.hastane.kisi.Yonetici;
import com.mehdi8s.hastane.kurum.SaglikKurumu;
import com.mehdi8s.hastane.randevu.RandevuServisi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class SistemKaydi {
    private final List<SaglikKurumu> kurumlar = new ArrayList<>();
    private final List<Hasta> hastalar = new ArrayList<>();
    private final List<Doktor> doktorlar = new ArrayList<>();
    private final List<SaglikPersoneli> personeller = new ArrayList<>();
    private final List<Yonetici> yoneticiler = new ArrayList<>();
    private final RandevuServisi randevuServisi = new RandevuServisi();

    public void kurumEkle(SaglikKurumu kurum) {
        ekle(kurumlar, kurum, "Kurum");
    }

    public void hastaEkle(Hasta hasta) {
        ekle(hastalar, hasta, "Hasta");
    }

    public void doktorEkle(Doktor doktor) {
        Objects.requireNonNull(doktor, "Doktor bos olamaz.");
        if (!kurumlar.contains(doktor.getKurum())) {
            throw new IllegalArgumentException("Doktorun kurumu sisteme eklenmeli.");
        }
        ekle(doktorlar, doktor, "Doktor");
    }

    public void personelEkle(SaglikPersoneli personel) {
        Objects.requireNonNull(personel, "Personel bos olamaz.");
        if (!kurumlar.contains(personel.getKurum())) {
            throw new IllegalArgumentException("Personelin kurumu sisteme eklenmeli.");
        }
        ekle(personeller, personel, "Personel");
    }

    public void yoneticiEkle(Yonetici yonetici) {
        Objects.requireNonNull(yonetici, "Yonetici bos olamaz.");
        if (!kurumlar.contains(yonetici.getKurum())) {
            throw new IllegalArgumentException("Yoneticinin kurumu sisteme eklenmeli.");
        }
        ekle(yoneticiler, yonetici, "Yonetici");
    }

    public List<SaglikKurumu> getKurumlar() {
        return List.copyOf(kurumlar);
    }

    public List<Hasta> getHastalar() {
        return List.copyOf(hastalar);
    }

    public List<Doktor> getDoktorlar() {
        return List.copyOf(doktorlar);
    }

    public List<SaglikPersoneli> getPersoneller() {
        return List.copyOf(personeller);
    }

    public List<Yonetici> getYoneticiler() {
        return List.copyOf(yoneticiler);
    }

    public RandevuServisi getRandevuServisi() {
        return randevuServisi;
    }

    private static <T> void ekle(List<T> liste, T kayit, String ad) {
        Objects.requireNonNull(kayit, ad + " bos olamaz.");
        if (liste.contains(kayit)) {
            throw new IllegalArgumentException(ad + " zaten kayitli.");
        }
        liste.add(kayit);
    }
}
