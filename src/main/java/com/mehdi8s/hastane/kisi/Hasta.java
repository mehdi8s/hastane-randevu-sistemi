package com.mehdi8s.hastane.kisi;

public final class Hasta extends Kisi {
    public Hasta(String ad, String soyad) {
        super(ad, soyad);
    }

    @Override
    public Rol rol() {
        return Rol.HASTA;
    }
}
