package com.mehdi8s.hastane.kisi;

public final class Hasta extends Kisi {
    public Hasta(String ad, String soyad) {
        super(ad, soyad);
    }

    private Hasta(String id, String ad, String soyad) {
        super(id, ad, soyad);
    }

    public static Hasta yukle(String id, String ad, String soyad) {
        return new Hasta(id, ad, soyad);
    }

    @Override
    public Rol rol() {
        return Rol.HASTA;
    }
}
