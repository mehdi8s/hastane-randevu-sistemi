package com.mehdi8s.hastane.ekran;

import com.mehdi8s.hastane.hizmet.SaglikHizmeti;
import com.mehdi8s.hastane.kayit.SistemKaydi;
import com.mehdi8s.hastane.kisi.Doktor;
import com.mehdi8s.hastane.kisi.Hasta;
import com.mehdi8s.hastane.randevu.Randevu;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;

public final class HastaOturumu extends Oturum {
    public HastaOturumu(Hasta hasta, SistemKaydi kayit, KayitSaklayici saklayici) {
        super(hasta, kayit, saklayici);
    }

    @Override
    protected void komutlariYaz(Konsol konsol) {
        konsol.yaz("1) Randevu iste");
        konsol.yaz("2) Randevularim");
        konsol.yaz("3) Randevu iptal");
    }

    @Override
    protected boolean komutCalistir(String secim, Konsol konsol) {
        return switch (secim) {
            case "1" -> {
                randevuIste(konsol);
                yield true;
            }
            case "2" -> {
                listeYaz(konsol, kendiRandevulari());
                yield true;
            }
            case "3" -> {
                iptalEt(konsol);
                yield true;
            }
            default -> false;
        };
    }

    private void randevuIste(Konsol konsol) {
        List<Doktor> doktorlar = kayit().getDoktorlar();
        if (doktorlar.isEmpty()) {
            konsol.yaz("Kayitli doktor yok.");
            return;
        }
        for (int i = 0; i < doktorlar.size(); i++) {
            Doktor doktor = doktorlar.get(i);
            konsol.yaz((i + 1) + ") " + doktor.tamAd() + " - " + doktor.getUzmanlik().getAd());
        }
        Doktor doktor = doktorlar.get(siraOku(konsol, doktorlar.size(), "Doktor"));
        List<SaglikHizmeti> hizmetler = doktor.getHizmetler();
        if (hizmetler.isEmpty()) {
            konsol.yaz("Bu doktorun tanimli hizmeti yok.");
            return;
        }
        for (int i = 0; i < hizmetler.size(); i++) {
            SaglikHizmeti hizmet = hizmetler.get(i);
            konsol.yaz((i + 1) + ") " + hizmet + " (" + hizmet.getSureDakika() + " dk, " + hizmet.getUcret().toPlainString() + ")");
        }
        SaglikHizmeti hizmet = hizmetler.get(siraOku(konsol, hizmetler.size(), "Hizmet"));
        LocalDateTime baslangic = tarihOku(konsol);
        Randevu randevu = servis().olustur((Hasta) kisi(), doktor, hizmet, baslangic);
        sakla();
        konsol.yaz("Randevu olustu: " + randevu.getBaslangic() + " " + randevu.getDurum());
    }

    private void iptalEt(Konsol konsol) {
        List<Randevu> liste = kendiRandevulari();
        listeYaz(konsol, liste);
        if (liste.isEmpty()) {
            return;
        }
        Randevu randevu = liste.get(siraOku(konsol, liste.size(), "Randevu"));
        servis().iptalEt(kisi(), randevu);
        sakla();
        konsol.yaz("Randevu iptal edildi.");
    }

    private List<Randevu> kendiRandevulari() {
        return servis().getRandevular().stream()
                .filter(randevu -> randevu.getHasta().equals(kisi()))
                .toList();
    }

    private LocalDateTime tarihOku(Konsol konsol) {
        String metin = konsol.oku("Tarih (yyyy-MM-dd HH:mm)");
        try {
            return LocalDateTime.parse(metin.replace(' ', 'T'));
        } catch (DateTimeParseException ex) {
            throw new IllegalArgumentException("Tarih yyyy-MM-dd HH:mm olmali.");
        }
    }
}
