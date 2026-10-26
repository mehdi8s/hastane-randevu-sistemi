package com.mehdi8s.hastane.kisi;

import com.mehdi8s.hastane.hizmet.SaglikHizmeti;
import com.mehdi8s.hastane.kurum.SaglikKurumu;
import com.mehdi8s.hastane.kurum.Sube;
import com.mehdi8s.hastane.zaman.ZamanAraligi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class Doktor extends Calisan {
    private final Sube sube;
    private final Uzmanlik uzmanlik;
    private final List<SaglikHizmeti> hizmetler = new ArrayList<>();
    private final List<ZamanAraligi> uygunluklar = new ArrayList<>();

    public Doktor(String ad, String soyad, SaglikKurumu kurum, Sube sube, Uzmanlik uzmanlik) {
        super(ad, soyad, kurum);
        this.sube = Objects.requireNonNull(sube, "Sube bos olamaz.");
        this.uzmanlik = Objects.requireNonNull(uzmanlik, "Uzmanlik bos olamaz.");
        if (!kurum.getSubeler().contains(sube)) {
            throw new IllegalArgumentException("Doktorun subesi bu kuruma ait olmali.");
        }
    }

    public void hizmetEkle(SaglikHizmeti hizmet) {
        Objects.requireNonNull(hizmet, "Hizmet bos olamaz.");
        if (!sube.getHizmetler().contains(hizmet)) {
            throw new IllegalArgumentException("Hizmet bu subede tanimli degil.");
        }
        hizmetler.add(hizmet);
    }

    public void uygunlukEkle(ZamanAraligi uygunluk) {
        Objects.requireNonNull(uygunluk, "Uygunluk bos olamaz.");
        boolean subeAcik = sube.getCalismaSaatleri().stream().anyMatch(saat -> saat.kapsar(uygunluk));
        if (!subeAcik) {
            throw new IllegalArgumentException("Uygunluk, subenin calisma saati icinde olmali.");
        }
        uygunluklar.add(uygunluk);
    }

    public Sube getSube() {
        return sube;
    }

    public Uzmanlik getUzmanlik() {
        return uzmanlik;
    }

    public List<SaglikHizmeti> getHizmetler() {
        return List.copyOf(hizmetler);
    }

    public List<ZamanAraligi> getUygunluklar() {
        return List.copyOf(uygunluklar);
    }

    @Override
    public Rol rol() {
        return Rol.DOKTOR;
    }
}
