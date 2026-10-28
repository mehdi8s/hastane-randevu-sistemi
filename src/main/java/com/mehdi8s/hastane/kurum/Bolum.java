package com.mehdi8s.hastane.kurum;

import java.util.UUID;

public final class Bolum {
    private final String id;
    private final String ad;

    public Bolum(String ad) {
        this(UUID.randomUUID().toString(), ad);
    }

    private Bolum(String id, String ad) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Bolum kimligi bos olamaz.");
        }
        if (ad == null || ad.isBlank()) {
            throw new IllegalArgumentException("Bolum adi bos olamaz.");
        }
        this.id = id.trim();
        this.ad = ad.trim();
    }

    public static Bolum yukle(String id, String ad) {
        return new Bolum(id, ad);
    }

    public String getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bolum bolum)) {
            return false;
        }
        return id.equals(bolum.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return ad;
    }
}
