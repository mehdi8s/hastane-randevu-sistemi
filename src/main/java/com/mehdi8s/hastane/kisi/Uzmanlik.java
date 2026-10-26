package com.mehdi8s.hastane.kisi;

public final class Uzmanlik {
    private final String ad;

    public Uzmanlik(String ad) {
        if (ad == null || ad.isBlank()) {
            throw new IllegalArgumentException("Uzmanlik adi bos olamaz.");
        }
        this.ad = ad.trim();
    }

    public String getAd() {
        return ad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Uzmanlik uzmanlik)) {
            return false;
        }
        return ad.equals(uzmanlik.ad);
    }

    @Override
    public int hashCode() {
        return ad.hashCode();
    }

    @Override
    public String toString() {
        return ad;
    }
}
