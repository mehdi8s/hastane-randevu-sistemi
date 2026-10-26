package com.mehdi8s.hastane.hizmet;

import java.math.BigDecimal;

public final class GenelMuayene extends SaglikHizmeti {
    public GenelMuayene(String ad, int sureDakika, BigDecimal ucret) {
        super(ad, sureDakika, ucret);
    }

    @Override
    public String hizmetTuru() {
        return "Genel Muayene";
    }
}
