package com.mehdi8s.hastane.ekran;

import com.mehdi8s.hastane.kayit.SistemKaydi;
import com.mehdi8s.hastane.kisi.Kisi;

import java.util.ArrayList;
import java.util.List;

public final class GirisEkrani {
    private final SistemKaydi kayit;
    private final OturumAcici oturumAcici;

    public GirisEkrani(SistemKaydi kayit, KayitSaklayici saklayici) {
        this.kayit = kayit;
        this.oturumAcici = new OturumAcici(kayit, saklayici);
    }

    public void calistir(Konsol konsol) {
        konsol.yaz("Hastane Randevu Sistemi");
        while (true) {
            List<Kisi> kisiler = kisiler();
            konsol.yaz("");
            if (kisiler.isEmpty()) {
                konsol.yaz("Kayitli kisi yok.");
                return;
            }
            for (int i = 0; i < kisiler.size(); i++) {
                Kisi kisi = kisiler.get(i);
                konsol.yaz((i + 1) + ") " + kisi.tamAd() + " (" + kisi.rol() + ")");
            }
            konsol.yaz("0) Cikis");
            String secim = konsol.oku("Secim");
            if ("0".equals(secim)) {
                return;
            }
            int sira = sira(secim, kisiler.size());
            if (sira < 0) {
                konsol.yaz("Gecersiz secim.");
                continue;
            }
            oturumAcici.ac(kisiler.get(sira)).calistir(konsol);
        }
    }

    private List<Kisi> kisiler() {
        List<Kisi> hepsi = new ArrayList<>();
        hepsi.addAll(kayit.getHastalar());
        hepsi.addAll(kayit.getDoktorlar());
        hepsi.addAll(kayit.getPersoneller());
        hepsi.addAll(kayit.getYoneticiler());
        return hepsi;
    }

    private int sira(String metin, int boyut) {
        try {
            int deger = Integer.parseInt(metin);
            if (deger >= 1 && deger <= boyut) {
                return deger - 1;
            }
        } catch (NumberFormatException ex) {
            return -1;
        }
        return -1;
    }
}
