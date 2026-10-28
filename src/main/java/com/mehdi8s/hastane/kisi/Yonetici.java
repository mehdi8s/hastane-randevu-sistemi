package com.mehdi8s.hastane.kisi;

import com.mehdi8s.hastane.kurum.SaglikKurumu;

public final class Yonetici extends Calisan {
    public Yonetici(String ad, String soyad, SaglikKurumu kurum) {
        super(ad, soyad, kurum);
    }

    private Yonetici(String id, String ad, String soyad, SaglikKurumu kurum) {
        super(id, ad, soyad, kurum);
    }

    public static Yonetici yukle(String id, String ad, String soyad, SaglikKurumu kurum) {
        return new Yonetici(id, ad, soyad, kurum);
    }

    @Override
    public Rol rol() {
        return Rol.YONETICI;
    }
}
