package com.mehdi8s.hastane.hizmet;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

public abstract class SaglikHizmeti {
    private final String id;
    private final String ad;
    private final int sureDakika;
    private final BigDecimal ucret;

    protected SaglikHizmeti(String ad, int sureDakika, BigDecimal ucret) {
        if (ad == null || ad.isBlank()) {
            throw new IllegalArgumentException("Hizmet adi bos olamaz.");
        }
        if (sureDakika <= 0) {
            throw new IllegalArgumentException("Hizmet suresi sifirdan buyuk olmali.");
        }
        if (ucret == null || ucret.signum() < 0) {
            throw new IllegalArgumentException("Hizmet ucreti negatif olamaz.");
        }
        this.id = UUID.randomUUID().toString();
        this.ad = ad.trim();
        this.sureDakika = sureDakika;
        this.ucret = ucret;
    }

    public abstract String hizmetTuru();

    public String getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    public int getSureDakika() {
        return sureDakika;
    }

    public BigDecimal getUcret() {
        return ucret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SaglikHizmeti hizmet)) {
            return false;
        }
        return id.equals(hizmet.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return hizmetTuru() + ": " + ad;
    }
}
