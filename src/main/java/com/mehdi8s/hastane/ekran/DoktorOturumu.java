package com.mehdi8s.hastane.ekran;

import com.mehdi8s.hastane.kayit.SistemKaydi;
import com.mehdi8s.hastane.kisi.Doktor;
import com.mehdi8s.hastane.randevu.Randevu;

import java.util.List;

public final class DoktorOturumu extends YetkiliOturum {
    public DoktorOturumu(Doktor doktor, SistemKaydi kayit, KayitSaklayici saklayici) {
        super(doktor, kayit, saklayici);
    }

    @Override
    protected List<Randevu> gorevRandevulari() {
        return servis().getRandevular().stream()
                .filter(randevu -> randevu.getDoktor().equals(kisi()))
                .toList();
    }
}
