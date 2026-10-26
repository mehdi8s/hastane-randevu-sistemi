package com.mehdi8s.hastane.kurum;

public final class Hastane extends SaglikKurumu {
    public Hastane(String ad) {
        super(ad);
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
