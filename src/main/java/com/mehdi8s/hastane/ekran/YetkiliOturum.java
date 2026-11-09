package com.mehdi8s.hastane.ekran;

import com.mehdi8s.hastane.kayit.SistemKaydi;
import com.mehdi8s.hastane.kisi.Kisi;
import com.mehdi8s.hastane.randevu.Randevu;

import java.util.List;
import java.util.function.Predicate;

public abstract class YetkiliOturum extends Oturum {
    protected YetkiliOturum(Kisi kisi, SistemKaydi kayit, KayitSaklayici saklayici) {
        super(kisi, kayit, saklayici);
    }

    @Override
    protected void komutlariYaz(Konsol konsol) {
        konsol.yaz("1) Randevulari listele");
        konsol.yaz("2) Onayla");
        konsol.yaz("3) Reddet");
        konsol.yaz("4) Tamamla");
        konsol.yaz("5) Iptal et");
    }

    @Override
    protected boolean komutCalistir(String secim, Konsol konsol) {
        return switch (secim) {
            case "1" -> {
                listeYaz(konsol, durumaGore(konsol, gorevRandevulari()));
                yield true;
            }
            case "2" -> uygula(konsol, Randevu::onayKabulEder, (randevu) -> servis().onayla(kisi(), randevu));
            case "3" -> uygula(konsol, Randevu::redKabulEder, (randevu) -> servis().reddet(kisi(), randevu));
            case "4" -> uygula(konsol, Randevu::tamamlaKabulEder, (randevu) -> servis().tamamla(kisi(), randevu));
            case "5" -> uygula(konsol, Randevu::iptalKabulEder, (randevu) -> servis().iptalEt(kisi(), randevu));
            default -> false;
        };
    }

    protected abstract List<Randevu> gorevRandevulari();

    private boolean uygula(Konsol konsol, Predicate<Randevu> uygun, Islem islem) {
        List<Randevu> liste = gorevRandevulari().stream().filter(uygun).toList();
        listeYaz(konsol, liste);
        if (liste.isEmpty()) {
            return true;
        }
        Randevu randevu = liste.get(siraOku(konsol, liste.size(), "Randevu"));
        islem.uygula(randevu);
        sakla();
        konsol.yaz("Tamam: " + randevu.getDurum());
        return true;
    }

    @FunctionalInterface
    private interface Islem {
        void uygula(Randevu randevu);
    }
}
