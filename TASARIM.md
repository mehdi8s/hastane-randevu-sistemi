# Tasarım notu

## Amaç

Hastane ve klinik randevu sistemini nesnelere bölmek. Aynı sınıflar tek şubeli bir kliniği de, çok şubeli bir hastaneyi de taşır. Dil Java 17’dir. Erişim belirteçleri, soyut sınıf ve çok biçimlilik sunumda doğrudan gösterilebilir.

## Sınıflar

Dört grup vardır.

**Kurum.** `SaglikKurumu` soyut sınıftır. `Hastane` istediği kadar `Sube` alır. `Klinik` yalnızca bir şube kabul eder. Bu kural `yeniSubeKabulEder` metodunun iki farklı gövdesidir. Her şubenin bölümleri, çalışma saatleri ve hizmetleri vardır.

**Hizmet.** `SaglikHizmeti` süre ve ücret tutar. `GenelMuayene`, `Radyoloji` ve `Laboratuvar` kendi tür adını döndürür. Ücret ve süre sonradan değişmesin diye randevu bunları oluşturulduğu anda kopyalar.

**Kişi.** `Kisi` soyut sınıftır ve `rol()` her alt sınıfta sabittir: hasta, doktor, sağlık personeli, yönetici. Doktor ve personel bir şubeye bağlıdır. Yönetici kurumun tamamından sorumludur. Doktor yalnızca kendi şubesinde tanımlı hizmeti verebilir. Uygunluk aralığı, şubenin çalışma saatinin içinde olmak zorundadır.

**Randevu.** Yeni kayıt `ONAY_BEKLIYOR` durumundadır. Geçerli geçişler:

- onay bekliyor → onaylandı, reddedildi veya iptal
- onaylandı → tamamlandı veya iptal

Reddedilen, tamamlanan ve iptal edilen kayıt yeniden onaylanamaz.

Ortak zaman kavramı `ZamanAraligi` içindedir. Şube çalışma saati ve doktor uygunluğu aynı sınıftır. Aralık aynı güne ait değilse çakışmaz. Bir aralık diğerini ancak aynı günde ve sınırların içindeyse kapsar.

## Bilinçli olarak sonraki adıma bırakılanlar

Randevu oluştururken çakışma kontrolü henüz yoktur. `Randevu` kurucusu doktorun hizmetini ve şubesini doğrular; saatin uygunluğu ve başka randevuyla çakışması bir uygulama servisinde toplanacaktır. Giriş, yetki kontrolünün servis katmanında kullanılması, kayıtların dosyada saklanması ve ekran da bu modelin üstüne eklenecektir.

Sınıf adları ASCII yazılmıştır (`SaglikKurumu`). Sunumda bunlar Sağlık Kurumu olarak anlatılır. Türkçe karakterli dosya adından kaçınmak derlemeyi sade tutar.
