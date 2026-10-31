package com.mehdi8s.hastane.ekran;

import com.mehdi8s.hastane.kayit.SistemKaydi;
import com.mehdi8s.hastane.kisi.Yonetici;
import com.mehdi8s.hastane.randevu.Randevu;

import java.util.List;

public final class YoneticiOturumu extends YetkiliOturum {
    public YoneticiOturumu(Yonetici yonetici, SistemKaydi kayit, KayitSaklayici saklayici) {
        super(yonetici, kayit, saklayici);
    }

    @Override
    protected List<Randevu> gorevRandevulari() {
        Yonetici yonetici = (Yonetici) kisi();
        return servis().getRandevular().stream()
                .filter(randevu -> randevu.getDoktor().getKurum().equals(yonetici.getKurum()))
                .toList();
    }
}
