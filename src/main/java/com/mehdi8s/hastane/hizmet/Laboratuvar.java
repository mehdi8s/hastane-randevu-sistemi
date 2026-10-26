package com.mehdi8s.hastane.hizmet;

import java.math.BigDecimal;

public final class Laboratuvar extends SaglikHizmeti {
    public Laboratuvar(String ad, int sureDakika, BigDecimal ucret) {
        super(ad, sureDakika, ucret);
    }

    @Override
    public String hizmetTuru() {
        return "Laboratuvar";
    }
}
