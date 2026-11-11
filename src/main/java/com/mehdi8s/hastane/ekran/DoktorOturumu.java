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
    protected void komutlariYaz(Konsol konsol) {
        super.komutlariYaz(konsol);
        konsol.yaz("6) Uygunluk ekle");
    }

    @Override
    protected boolean komutCalistir(String secim, Konsol konsol) {
        if (super.komutCalistir(secim, konsol)) {
            return true;
        }
        if (!"6".equals(secim)) {
            return false;
        }
        doktor().uygunlukEkle(aralikOku(konsol));
        sakla();
        konsol.yaz("Uygunluk eklendi.");
        return true;
    }

    @Override
    protected List<Randevu> gorevRandevulari() {
        return servis().getRandevular().stream()
                .filter(randevu -> randevu.getDoktor().equals(kisi()))
                .toList();
    }

    private Doktor doktor() {
        return (Doktor) kisi();
    }
}
