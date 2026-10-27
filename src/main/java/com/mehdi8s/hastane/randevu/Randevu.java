package com.mehdi8s.hastane.randevu;

import com.mehdi8s.hastane.hizmet.SaglikHizmeti;
import com.mehdi8s.hastane.kisi.Doktor;
import com.mehdi8s.hastane.kisi.Hasta;
import com.mehdi8s.hastane.kurum.Sube;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public final class Randevu {
    private final String id;
    private final Hasta hasta;
    private final Doktor doktor;
    private final Sube sube;
    private final String hizmetAdi;
    private final String hizmetTuru;
    private final int sureDakika;
    private final BigDecimal ucret;
    private final LocalDateTime baslangic;
    private RandevuDurumu durum;

    public Randevu(Hasta hasta, Doktor doktor, Sube sube, SaglikHizmeti hizmet, LocalDateTime baslangic) {
        this.hasta = Objects.requireNonNull(hasta, "Hasta bos olamaz.");
        this.doktor = Objects.requireNonNull(doktor, "Doktor bos olamaz.");
        this.sube = Objects.requireNonNull(sube, "Sube bos olamaz.");
        Objects.requireNonNull(hizmet, "Hizmet bos olamaz.");
        this.baslangic = Objects.requireNonNull(baslangic, "Baslangic bos olamaz.");
        if (!doktor.getSube().equals(sube)) {
            throw new IllegalArgumentException("Randevu, doktorun calistigi subede olmali.");
        }
        if (!doktor.getHizmetler().contains(hizmet)) {
            throw new IllegalArgumentException("Doktor bu hizmeti vermiyor.");
        }
        this.id = UUID.randomUUID().toString();
        this.hizmetAdi = hizmet.getAd();
        this.hizmetTuru = hizmet.hizmetTuru();
        this.sureDakika = hizmet.getSureDakika();
        this.ucret = hizmet.getUcret();
        this.durum = RandevuDurumu.ONAY_BEKLIYOR;
    }

    public void onayla() {
        gec(RandevuDurumu.ONAYLANDI, RandevuDurumu.ONAY_BEKLIYOR);
    }

    public void reddet() {
        gec(RandevuDurumu.REDDEDILDI, RandevuDurumu.ONAY_BEKLIYOR);
    }

    public void tamamla() {
        gec(RandevuDurumu.TAMAMLANDI, RandevuDurumu.ONAYLANDI);
    }

    public void iptalEt() {
        gec(RandevuDurumu.IPTAL, RandevuDurumu.ONAY_BEKLIYOR, RandevuDurumu.ONAYLANDI);
    }

    public LocalDateTime bitis() {
        return baslangic.plusMinutes(sureDakika);
    }

    public boolean zamanCakisiyor(LocalDateTime digerBaslangic, LocalDateTime digerBitis) {
        Objects.requireNonNull(digerBaslangic, "Baslangic bos olamaz.");
        Objects.requireNonNull(digerBitis, "Bitis bos olamaz.");
        return baslangic.isBefore(digerBitis) && digerBaslangic.isBefore(bitis());
    }

    public boolean yerKaplar() {
        return durum == RandevuDurumu.ONAY_BEKLIYOR
                || durum == RandevuDurumu.ONAYLANDI
                || durum == RandevuDurumu.TAMAMLANDI;
    }

    public String getId() {
        return id;
    }

    public Hasta getHasta() {
        return hasta;
    }

    public Doktor getDoktor() {
        return doktor;
    }

    public Sube getSube() {
        return sube;
    }

    public String getHizmetAdi() {
        return hizmetAdi;
    }

    public String getHizmetTuru() {
        return hizmetTuru;
    }

    public int getSureDakika() {
        return sureDakika;
    }

    public BigDecimal getUcret() {
        return ucret;
    }

    public LocalDateTime getBaslangic() {
        return baslangic;
    }

    public RandevuDurumu getDurum() {
        return durum;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Randevu randevu)) {
            return false;
        }
        return id.equals(randevu.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    private void gec(RandevuDurumu hedef, RandevuDurumu... beklenen) {
        for (RandevuDurumu aday : beklenen) {
            if (durum == aday) {
                durum = hedef;
                return;
            }
        }
        throw new IllegalStateException(durum + " durumundan " + hedef + " durumuna gecilemez.");
    }
}
