package com.mehdi8s.hastane.kurum;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public abstract class SaglikKurumu {
    private final String id;
    private final String ad;
    private final List<Sube> subeler = new ArrayList<>();

    protected SaglikKurumu(String ad) {
        this(UUID.randomUUID().toString(), ad);
    }

    protected SaglikKurumu(String id, String ad) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Kurum kimligi bos olamaz.");
        }
        if (ad == null || ad.isBlank()) {
            throw new IllegalArgumentException("Kurum adi bos olamaz.");
        }
        this.id = id.trim();
        this.ad = ad.trim();
    }

    public final void subeEkle(Sube sube) {
        Objects.requireNonNull(sube, "Sube bos olamaz.");
        if (!yeniSubeKabulEder(subeler.size())) {
            throw new IllegalStateException(kurumTuru() + " yeni sube kabul etmiyor: " + ad);
        }
        subeler.add(sube);
    }

    protected abstract boolean yeniSubeKabulEder(int mevcutSubeSayisi);

    public abstract String kurumTuru();

    public String getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    public List<Sube> getSubeler() {
        return List.copyOf(subeler);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SaglikKurumu kurum)) {
            return false;
        }
        return id.equals(kurum.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
