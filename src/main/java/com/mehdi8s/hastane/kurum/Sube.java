package com.mehdi8s.hastane.kurum;

import com.mehdi8s.hastane.hizmet.SaglikHizmeti;
import com.mehdi8s.hastane.zaman.ZamanAraligi;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public final class Sube {
    private final String id;
    private final String ad;
    private final String adres;
    private final List<Bolum> bolumler = new ArrayList<>();
    private final List<ZamanAraligi> calismaSaatleri = new ArrayList<>();
    private final List<SaglikHizmeti> hizmetler = new ArrayList<>();

    public Sube(String ad, String adres) {
        if (ad == null || ad.isBlank()) {
            throw new IllegalArgumentException("Sube adi bos olamaz.");
        }
        if (adres == null || adres.isBlank()) {
            throw new IllegalArgumentException("Sube adresi bos olamaz.");
        }
        this.id = UUID.randomUUID().toString();
        this.ad = ad.trim();
        this.adres = adres.trim();
    }

    public void bolumEkle(Bolum bolum) {
        bolumler.add(Objects.requireNonNull(bolum, "Bolum bos olamaz."));
    }

    public void calismaSaatiEkle(ZamanAraligi saat) {
        calismaSaatleri.add(Objects.requireNonNull(saat, "Calisma saati bos olamaz."));
    }

    public void hizmetEkle(SaglikHizmeti hizmet) {
        hizmetler.add(Objects.requireNonNull(hizmet, "Hizmet bos olamaz."));
    }

    public String getId() {
        return id;
    }

    public String getAd() {
        return ad;
    }

    public String getAdres() {
        return adres;
    }

    public List<Bolum> getBolumler() {
        return List.copyOf(bolumler);
    }

    public List<ZamanAraligi> getCalismaSaatleri() {
        return List.copyOf(calismaSaatleri);
    }

    public List<SaglikHizmeti> getHizmetler() {
        return List.copyOf(hizmetler);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Sube sube)) {
            return false;
        }
        return id.equals(sube.id);
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
