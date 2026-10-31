package com.mehdi8s.hastane.ekran;

import com.mehdi8s.hastane.kayit.DosyaDeposu;
import com.mehdi8s.hastane.kayit.SistemKaydi;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public final class Uygulama {
    public static void main(String[] args) {
        Path dosya = args.length > 0 ? Path.of(args[0]) : Path.of("sistem.kayit");
        DosyaDeposu depo = new DosyaDeposu();
        SistemKaydi kayit = ac(depo, dosya);
        new GirisEkrani(kayit, () -> yaz(depo, kayit, dosya)).calistir(new TarayiciKonsol());
    }

    private static SistemKaydi ac(DosyaDeposu depo, Path dosya) {
        try {
            if (Files.exists(dosya)) {
                return depo.yukle(dosya);
            }
            SistemKaydi kayit = OrnekSistem.olustur();
            depo.kaydet(kayit, dosya);
            return kayit;
        } catch (IOException ex) {
            throw new IllegalStateException("Kayit acilamadi.");
        }
    }

    private static void yaz(DosyaDeposu depo, SistemKaydi kayit, Path dosya) {
        try {
            depo.kaydet(kayit, dosya);
        } catch (IOException ex) {
            throw new IllegalStateException("Kayit yazilamadi.");
        }
    }

    private Uygulama() {
    }
}
