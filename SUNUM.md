# Sunum notu

Süre: yaklaşık 5 dakika. Ekran, sınıf yapısını göstermek içindir.

## Ne gösterilecek

1. `SaglikKurumu` soyut sınıftır. `Hastane` ikinci şubeyi alır, `Klinik` almaz. Aynı `subeEkle` çağrısı iki farklı gövde çalıştırır.
2. `Kisi.rol()` her alt sınıfta sabittir. Giriş ekranı nesnenin sınıfına göre menü açar.
3. Randevu yalnızca `RandevuServisi` üzerinden doğar. Uygunluk, şube saati ve çakışma orada reddedilir.
4. Durum geçişi `Randevu` içindedir: onay bekleyen kayıt tamamlanamaz.

## Sınıf diyagramı

Sunumda bu iki ağacı göstermek yeter. Ayrıntı `TASARIM.md` dosyasındadır.

```mermaid
classDiagram
    SaglikKurumu <|-- Hastane
    SaglikKurumu <|-- Klinik
    SaglikKurumu o-- Sube
    SaglikHizmeti <|-- GenelMuayene
    SaglikHizmeti <|-- Radyoloji
    SaglikHizmeti <|-- Laboratuvar
    Kisi <|-- Hasta
    Kisi <|-- Calisan
    Calisan <|-- Doktor
    Calisan <|-- SaglikPersoneli
    Calisan <|-- Yonetici
    RandevuServisi o-- Randevu
    Randevu --> Hasta
    Randevu --> Doktor
    Oturum <|-- HastaOturumu
    Oturum <|-- YetkiliOturum
    YetkiliOturum <|-- DoktorOturumu
    YetkiliOturum <|-- YoneticiOturumu
```

`Hastane` ile `Klinik` farkı `yeniSubeKabulEder` gövdesindedir. Ekran farkı `OturumAcici` içindeki `instanceof` ayrımıdır.

## Ekran sırası

Program klasöründe `sistem.kayit` yoksa sunum dünyası kurulur. Varsa dosyayı silip yeniden açın.

```powershell
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
java -cp out com.mehdi8s.hastane.ekran.Uygulama
```

1. Selin Arslan: randevuları duruma göre listele. `1` onay bekleyen Özge’yi, `2` onaylı Ali’yi gösterir.
2. Kemal Aslan: Özge’nin randevusunu onayla. İkinci yönetici aynı kurumda yetkilidir.
3. Leyla Koç: şube eklemeyi deneyin. Klinik ikinci şubeyi reddeder.
4. Özge Çelik: `2026-11-02 09:20` için yeni randevu. `09:00` Ayşe’de doludur.

## Sınıf adları

Kod ASCII yazar (`SaglikKurumu`). Sözlü anlatımda Sağlık Kurumu, Şube, Randevu denir.
