package com.mehdi8s.hastane.kurum;

public final class Klinik extends SaglikKurumu {
    public Klinik(String ad) {
        super(ad);
    }

    private Klinik(String id, String ad) {
        super(id, ad);
    }

    public static Klinik yukle(String id, String ad) {
        return new Klinik(id, ad);
    }

    @Override
    protected boolean yeniSubeKabulEder(int mevcutSubeSayisi) {
        return mevcutSubeSayisi == 0;
    }

    @Override
    public String kurumTuru() {
        return "Klinik";
    }
}
