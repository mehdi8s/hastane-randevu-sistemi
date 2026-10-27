package com.mehdi8s.hastane.randevu;

import com.mehdi8s.hastane.hizmet.SaglikHizmeti;
import com.mehdi8s.hastane.kisi.Doktor;
import com.mehdi8s.hastane.kisi.Hasta;
import com.mehdi8s.hastane.kisi.Kisi;
import com.mehdi8s.hastane.kisi.Yonetici;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class RandevuServisi {
    private final List<Randevu> randevular = new ArrayList<>();

    public Randevu olustur(Hasta hasta, Doktor doktor, SaglikHizmeti hizmet, LocalDateTime baslangic) {
        Objects.requireNonNull(hasta, "Hasta bos olamaz.");
        Objects.requireNonNull(doktor, "Doktor bos olamaz.");
        Objects.requireNonNull(hizmet, "Hizmet bos olamaz.");
        Objects.requireNonNull(baslangic, "Baslangic bos olamaz.");

        LocalDateTime bitis = baslangic.plusMinutes(hizmet.getSureDakika());
        boolean doktorUygun = doktor.getUygunluklar().stream()
                .anyMatch(saat -> saat.sureyiKapsar(baslangic, bitis));
        if (!doktorUygun) {
            throw new IllegalArgumentException("Randevu doktorun uygunluk saatinin disinda.");
        }
        boolean subeAcik = doktor.getSube().getCalismaSaatleri().stream()
                .anyMatch(saat -> saat.sureyiKapsar(baslangic, bitis));
        if (!subeAcik) {
            throw new IllegalArgumentException("Randevu subenin calisma saatinin disinda.");
        }
        cakismaKontrol(hasta, doktor, baslangic, bitis);

        Randevu randevu = new Randevu(hasta, doktor, doktor.getSube(), hizmet, baslangic);
        randevular.add(randevu);
        return randevu;
    }

    public void onayla(Kisi yetkili, Randevu randevu) {
        kayitKontrol(randevu);
        doktorVeyaYonetici(yetkili, randevu, "Randevuyu yalnizca ilgili doktor veya kurum yoneticisi onaylayabilir.");
        randevu.onayla();
    }

    public void reddet(Kisi yetkili, Randevu randevu) {
        kayitKontrol(randevu);
        doktorVeyaYonetici(yetkili, randevu, "Randevuyu yalnizca ilgili doktor veya kurum yoneticisi reddedebilir.");
        randevu.reddet();
    }

    public void tamamla(Kisi yetkili, Randevu randevu) {
        kayitKontrol(randevu);
        doktorVeyaYonetici(yetkili, randevu, "Randevuyu yalnizca ilgili doktor veya kurum yoneticisi tamamlayabilir.");
        randevu.tamamla();
    }

    public void iptalEt(Kisi yetkili, Randevu randevu) {
        kayitKontrol(randevu);
        Objects.requireNonNull(yetkili, "Yetkili bos olamaz.");
        if (!yetkili.equals(randevu.getHasta()) && !doktorVeyaYoneticiMi(yetkili, randevu)) {
            throw new IllegalArgumentException(
                    "Randevuyu yalnizca hasta, ilgili doktor veya kurum yoneticisi iptal edebilir.");
        }
        randevu.iptalEt();
    }

    public List<Randevu> getRandevular() {
        return List.copyOf(randevular);
    }

    private void cakismaKontrol(Hasta hasta, Doktor doktor, LocalDateTime baslangic, LocalDateTime bitis) {
        for (Randevu kayit : randevular) {
            if (!kayit.yerKaplar() || !kayit.zamanCakisiyor(baslangic, bitis)) {
                continue;
            }
            if (kayit.getDoktor().equals(doktor)) {
                throw new IllegalArgumentException("Doktorun bu saatte baska bir randevusu var.");
            }
            if (kayit.getHasta().equals(hasta)) {
                throw new IllegalArgumentException("Hastanin bu saatte baska bir randevusu var.");
            }
        }
    }

    private void kayitKontrol(Randevu randevu) {
        Objects.requireNonNull(randevu, "Randevu bos olamaz.");
        if (!randevular.contains(randevu)) {
            throw new IllegalArgumentException("Randevu bu serviste kayitli degil.");
        }
    }

    private void doktorVeyaYonetici(Kisi yetkili, Randevu randevu, String mesaj) {
        Objects.requireNonNull(yetkili, "Yetkili bos olamaz.");
        if (!doktorVeyaYoneticiMi(yetkili, randevu)) {
            throw new IllegalArgumentException(mesaj);
        }
    }

    private boolean doktorVeyaYoneticiMi(Kisi yetkili, Randevu randevu) {
        if (yetkili.equals(randevu.getDoktor())) {
            return true;
        }
        return yetkili instanceof Yonetici yonetici && yonetici.getKurum().equals(randevu.getDoktor().getKurum());
    }
}
