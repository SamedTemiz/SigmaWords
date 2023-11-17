package com.samedtemiz.sigmawords.presentation.main.profile

import com.samedtemiz.sigmawords.R


enum class InformationItems(
    val icon: Int,
    val title: String,
    val description: String
) {
    SigmaNedir(
        icon = R.drawable.sigma_fill,
        title = "Sigma Metodolojisi Nedir?",
        description = "İş dünyasında bilinen **\"6 Sigma Kuralı\"** adlı metodoloji, aşağıdaki adımları içermektedir:\n" +
                "\n" +
                "1. **Tanımlama**\n" +
                "2. **Ölçme**\n" +
                "3. **Analiz Etme**\n" +
                "4. **İyileştirme**\n" +
                "5. **Kontrol Etme**\n" +
                "\n" +
                "Bu tanıma dayanarak, bir öğrenme süreci metodolojisi geliştirilmiştir. Temelde, İngilizce kelimelerin **tekrar edilerek öğrenilmesi** amaçlanmaktadır.\n"
    ),

    Algoritma(
        icon = R.drawable.connection,
        title = "Uygulama Algoritması",
        description = "Öğrenim algoritması, sigma metodolojisinden esinlenilerek oluşturulmuştur. Kullanıcılar, günlük testlere katılarak yeni kelimeler öğrenmeyi amaçlamaktadır.\n" +
                "\n" +
                "Algoritma şu şekildedir: Kullanıcı örneğin, 14 Kasım 2023 tarihinde bir kelimeyi doğru cevaplarsa, bu kelime sigma 1 seviyesine yükselecek ve sonraki gösterim tarihi (sigma tarihi) bir gün sonrasına güncellenecektir. Bu düzen, 6 farklı sigma seviyesi ve tarihini içerir. Bunlar;\n" +
                "\n" +
                "- Seviye 1: +1 Gün\n" +
                "- Seviye 2: +3 Gün\n" +
                "- Seviye 3: +7 Gün\n" +
                "- Seviye 4: +15 Gün\n" +
                "- Seviye 5: +21 Gün\n" +
                "- Seviye 6: +30 Gün\n" +
                "\n" +
                "Eğer kullanıcı, kelimeyi tüm sigma seviyelerinde doğru bilirse, bu kelime başarılı olarak işaretlenir ve kelime havuzundan çıkarılır.\n"
    ),

    Kaynaklar(
        icon = R.drawable.document,
        title = "Kaynaklar",
        description = "### Görsel Kaynaklar\n" +
                "- [Lottie Files](https://lottiefiles.com)\n" +
                "- [Flaticon](https://www.flaticon.com)\n" +
                "\n" +
                "### İngilizce Kelimeler\n" +
                "- [American Oxford 3000 by CEFR Level (PDF)](https://www.oxfordlearnersdictionaries.com/external/pdf/wordlists/oxford-3000-5000/American_Oxford_3000_by_CEFR_level.pdf)\n" +
                "- [American Oxford 5000 by CEFR Level (PDF)](https://www.oxfordlearnersdictionaries.com/external/pdf/wordlists/oxford-3000-5000/American_Oxford_5000_by_CEFR_level.pdf)\n" +
                "- [Oxford Kelime Listeleri (A1, A2, B1, B2, C1)](https://idealmuhendis.blogspot.com/2020/05/oxford-kelime-listeleri-a1-a2-b1-b2-c1.html)\n"
    ),

    Gelistirici(
        icon = R.drawable.about,
        title = "Geliştirici Hakkında",
        description = "### GitHub Profil" +
                "\n[SamedTemiz](https://github.com/SamedTemiz)"
    ),
}