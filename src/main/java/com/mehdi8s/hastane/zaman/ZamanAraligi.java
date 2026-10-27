package com.mehdi8s.hastane.zaman;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;

public final class ZamanAraligi {
    private final DayOfWeek gun;
    private final LocalTime baslangic;
    private final LocalTime bitis;

    public ZamanAraligi(DayOfWeek gun, LocalTime baslangic, LocalTime bitis) {
        this.gun = Objects.requireNonNull(gun, "Gun bos olamaz.");
        this.baslangic = Objects.requireNonNull(baslangic, "Baslangic bos olamaz.");
        this.bitis = Objects.requireNonNull(bitis, "Bitis bos olamaz.");
        if (!baslangic.isBefore(bitis)) {
            throw new IllegalArgumentException("Baslangic saati bitisten once olmali.");
        }
    }

    public DayOfWeek getGun() {
        return gun;
    }

    public LocalTime getBaslangic() {
        return baslangic;
    }

    public LocalTime getBitis() {
        return bitis;
    }

    public boolean kapsar(ZamanAraligi diger) {
        Objects.requireNonNull(diger, "Zaman araligi bos olamaz.");
        return gun == diger.gun
                && !diger.baslangic.isBefore(baslangic)
                && !diger.bitis.isAfter(bitis);
    }

    public boolean cakisiyor(ZamanAraligi diger) {
        Objects.requireNonNull(diger, "Zaman araligi bos olamaz.");
        return gun == diger.gun
                && baslangic.isBefore(diger.bitis)
                && diger.baslangic.isBefore(bitis);
    }

    public boolean tarihSaatiIcerir(LocalDateTime zaman) {
        Objects.requireNonNull(zaman, "Zaman bos olamaz.");
        LocalTime saat = zaman.toLocalTime();
        return zaman.getDayOfWeek() == gun && !saat.isBefore(baslangic) && saat.isBefore(bitis);
    }

    public boolean sureyiKapsar(LocalDateTime aralikBaslangic, LocalDateTime aralikBitis) {
        Objects.requireNonNull(aralikBaslangic, "Baslangic bos olamaz.");
        Objects.requireNonNull(aralikBitis, "Bitis bos olamaz.");
        if (!aralikBaslangic.toLocalDate().equals(aralikBitis.toLocalDate())) {
            return false;
        }
        if (aralikBaslangic.getDayOfWeek() != gun || !aralikBaslangic.isBefore(aralikBitis)) {
            return false;
        }
        LocalTime bas = aralikBaslangic.toLocalTime();
        LocalTime son = aralikBitis.toLocalTime();
        return !bas.isBefore(baslangic) && !son.isAfter(bitis);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ZamanAraligi diger)) {
            return false;
        }
        return gun == diger.gun && baslangic.equals(diger.baslangic) && bitis.equals(diger.bitis);
    }

    @Override
    public int hashCode() {
        return Objects.hash(gun, baslangic, bitis);
    }

    @Override
    public String toString() {
        return gun + " " + baslangic + "-" + bitis;
    }
}
