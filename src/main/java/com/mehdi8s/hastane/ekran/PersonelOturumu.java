package com.mehdi8s.hastane.ekran;

import com.mehdi8s.hastane.kayit.SistemKaydi;
import com.mehdi8s.hastane.kisi.SaglikPersoneli;

public final class PersonelOturumu extends Oturum {
    public PersonelOturumu(SaglikPersoneli personel, SistemKaydi kayit, KayitSaklayici saklayici) {
        super(personel, kayit, saklayici);
    }

    @Override
    protected void komutlariYaz(Konsol konsol) {
        konsol.yaz("1) Subedeki randevular");
    }

    @Override
    protected boolean komutCalistir(String secim, Konsol konsol) {
        if (!"1".equals(secim)) {
            return false;
        }
        SaglikPersoneli personel = (SaglikPersoneli) kisi();
        listeYaz(konsol, servis().getRandevular().stream()
                .filter(randevu -> randevu.getSube().equals(personel.getSube()))
                .toList());
        return true;
    }
}
