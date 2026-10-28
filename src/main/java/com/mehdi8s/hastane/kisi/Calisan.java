package com.mehdi8s.hastane.kisi;

import com.mehdi8s.hastane.kurum.SaglikKurumu;

import java.util.Objects;

public abstract class Calisan extends Kisi {
    private final SaglikKurumu kurum;

    protected Calisan(String ad, String soyad, SaglikKurumu kurum) {
        super(ad, soyad);
        this.kurum = Objects.requireNonNull(kurum, "Kurum bos olamaz.");
    }

    protected Calisan(String id, String ad, String soyad, SaglikKurumu kurum) {
        super(id, ad, soyad);
        this.kurum = Objects.requireNonNull(kurum, "Kurum bos olamaz.");
    }

    public SaglikKurumu getKurum() {
        return kurum;
    }
}
