package com.mehdi8s.hastane.kisi;

import java.util.UUID;

public abstract class Kisi {
    private final String id;
    private final String ad;
    private final String soyad;

    protected Kisi(String ad, String soyad) {
        this(UUID.randomUUID().toString(), ad, soyad);
    }

    protected Kisi(String id, String ad, String soyad) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Kisi kimligi bos olamaz.");
        }
        if (ad == null || ad.isBlank()) {
            throw new IllegalArgumentException("Ad bos olamaz.");
        }
        if (soyad == null || soyad.isBlank()) {
            throw new IllegalArgumentException("Soyad bos olamaz.");
        }
        this.id = id.trim();
        this.ad = ad.trim();
        this.soyad = soyad.trim();
    }

    public abstract Rol rol();

    public String getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public String tamAd() {
        return ad + " " + soyad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Kisi kisi)) {
            return false;
        }
        return id.equals(kisi.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return rol() + " " + tamAd();
    }
}
