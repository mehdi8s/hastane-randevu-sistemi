package com.mehdi8s.hastane.hizmet;

import java.math.BigDecimal;

public final class Laboratuvar extends SaglikHizmeti {
    public Laboratuvar(String ad, int sureDakika, BigDecimal ucret) {
        super(ad, sureDakika, ucret);
    }

    private Laboratuvar(String id, String ad, int sureDakika, BigDecimal ucret) {
        super(id, ad, sureDakika, ucret);
    }

    public static Laboratuvar yukle(String id, String ad, int sureDakika, BigDecimal ucret) {
        return new Laboratuvar(id, ad, sureDakika, ucret);
    }

    @Override
    public String hizmetTuru() {
        return "Laboratuvar";
    }
}
