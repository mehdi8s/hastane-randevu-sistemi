package com.mehdi8s.hastane.hizmet;

import java.math.BigDecimal;

public final class Radyoloji extends SaglikHizmeti {
    public Radyoloji(String ad, int sureDakika, BigDecimal ucret) {
        super(ad, sureDakika, ucret);
    }

    @Override
    public String hizmetTuru() {
        return "Radyoloji";
    }
}
