package com.mehdi8s.hastane.kisi;

import com.mehdi8s.hastane.kurum.SaglikKurumu;
import com.mehdi8s.hastane.kurum.Sube;

import java.util.Objects;

public final class SaglikPersoneli extends Calisan {
    private final Sube sube;
    private final String gorev;

    public SaglikPersoneli(String ad, String soyad, SaglikKurumu kurum, Sube sube, String gorev) {
        super(ad, soyad, kurum);
        this.sube = Objects.requireNonNull(sube, "Sube bos olamaz.");
        if (gorev == null || gorev.isBlank()) {
            throw new IllegalArgumentException("Gorev bos olamaz.");
        }
        if (!kurum.getSubeler().contains(sube)) {
            throw new IllegalArgumentException("Personelin subesi bu kuruma ait olmali.");
        }
        this.gorev = gorev.trim();
    }

    public Sube getSube() {
        return sube;
    }

    public String getGorev() {
        return gorev;
    }

    @Override
    public Rol rol() {
        return Rol.SAGLIK_PERSONELI;
    }
}
