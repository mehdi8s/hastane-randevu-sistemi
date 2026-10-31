package com.mehdi8s.hastane.ekran;

import com.mehdi8s.hastane.kayit.SistemKaydi;
import com.mehdi8s.hastane.kisi.Doktor;
import com.mehdi8s.hastane.kisi.Hasta;
import com.mehdi8s.hastane.kisi.Kisi;
import com.mehdi8s.hastane.kisi.SaglikPersoneli;
import com.mehdi8s.hastane.kisi.Yonetici;

public final class OturumAcici {
    private final SistemKaydi kayit;
    private final KayitSaklayici saklayici;

    public OturumAcici(SistemKaydi kayit, KayitSaklayici saklayici) {
        this.kayit = kayit;
        this.saklayici = saklayici;
    }

    public Oturum ac(Kisi kisi) {
        if (kisi instanceof Hasta hasta) {
            return new HastaOturumu(hasta, kayit, saklayici);
        }
        if (kisi instanceof Doktor doktor) {
            return new DoktorOturumu(doktor, kayit, saklayici);
        }
        if (kisi instanceof Yonetici yonetici) {
            return new YoneticiOturumu(yonetici, kayit, saklayici);
        }
        if (kisi instanceof SaglikPersoneli personel) {
            return new PersonelOturumu(personel, kayit, saklayici);
        }
        throw new IllegalArgumentException("Bu kisi icin ekran yok.");
    }
}
