# Hastane / Klinik Randevu Sistemi

Sakarya Üniversitesi bilgisayar mühendisliği yüksek lisans dersi için nesneye dayalı randevu sistemi. Tek klinik de, birden fazla şubesi olan hastane de aynı modelle kurulur.

Alan modeli kurum, şube, hizmet, kişi rolleri ve randevu durumunu taşır. Randevu servisi uygunluk saatini, çakışmayı ve onay yetkisini uygular. Dosya deposu bu kayıtları program kapanınca da tutar. Ekran sonraki adımdadır. Tasarım kararları `TASARIM.md` dosyasındadır.

## Derleme

Java 17 veya üzeri gerekir. Proje klasöründe:

```powershell
New-Item -ItemType Directory -Force out | Out-Null
javac --release 17 -encoding UTF-8 -d out (Get-ChildItem -Recurse -Filter *.java src | ForEach-Object FullName)
java -cp out com.mehdi8s.hastane.ModelDenemesi
```

Beklenen çıktı:

```
Randevu servisi denemesi gecti.
Dosya deposu denemesi gecti.
Model denemesi gecti.
```

`pom.xml` Maven içindir. Bu makinede Maven kurulu olmak zorunda değildir.
