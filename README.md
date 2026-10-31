# Hastane / Klinik Randevu Sistemi

Sakarya Üniversitesi bilgisayar mühendisliği yüksek lisans dersi için nesneye dayalı randevu sistemi. Tek klinik de, birden fazla şubesi olan hastane de aynı modelle kurulur.

Alan modeli kurum, şube, hizmet, kişi rolleri ve randevu durumunu taşır. Randevu servisi uygunluk saatini, çakışmayı ve onay yetkisini uygular. Dosya deposu bu kayıtları program kapanınca da tutar. Giriş ekranı kişiyi rolüne göre menüye alır. Tasarım kararları `TASARIM.md` dosyasındadır.

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
Ekran denemesi gecti.
Model denemesi gecti.
```

## Ekran

Örnek kayıt yoksa `sistem.kayit` oluşturulur. Varsa dosyadaki kayıt açılır. Her işlemden sonra dosya yeniden yazılır.

```powershell
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
java -cp out com.mehdi8s.hastane.ekran.Uygulama
```

Listeden kişi seçilir. Hasta randevu ister ve iptal eder. Doktor ile yönetici onaylar, reddeder veya tamamlar. Sağlık personeli yalnızca kendi şubesindeki randevuları görür. Doktorun uygun olduğu ilk örnek saat `2026-11-02 09:00` biçimindedir.

`pom.xml` Maven içindir. Bu makinede Maven kurulu olmak zorunda değildir.
