package com.mehdi8s.hastane.kurum;

public final class Hastane extends SaglikKurumu {
    public Hastane(String ad) {
        super(ad);
    }

    private Hastane(String id, String ad) {
        super(id, ad);
    }

    public static Hastane yukle(String id, String ad) {
        return new Hastane(id, ad);
    }

    @Override
    protected boolean yeniSubeKabulEder(int mevcutSubeSayisi) {
        return true;
    }

    @Override
    public String kurumTuru() {
        return "Hastane";
    }
}
