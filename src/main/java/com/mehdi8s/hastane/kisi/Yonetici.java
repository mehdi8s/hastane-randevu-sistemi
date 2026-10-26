package com.mehdi8s.hastane.kisi;

import com.mehdi8s.hastane.kurum.SaglikKurumu;

public final class Yonetici extends Calisan {
    public Yonetici(String ad, String soyad, SaglikKurumu kurum) {
        super(ad, soyad, kurum);
    }

    @Override
    public Rol rol() {
        return Rol.YONETICI;
    }
}
