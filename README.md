# 🎓 ماای‌یونی (MyUni) - دستیار هوشمند دانشجویی

<p align="center">
  <img src="app/src/main/res/mipmap-xxhdpi/ic_launcher.webp" width="120" alt="MyUni Logo" />
</p>

<p align="center">
  <b>اپلیکیشن مدیریت برنامه درسی، یادآور کلاس‌ها، سلف و امور دانشگاهی</b>
  <br />
  یک دستیار جامع و مدرن اندرویدی برای دانشجویان جهت مدیریت برنامه هفتگی، یادآور کلاس‌ها و غذا، و استخراج برنامه‌ها از سامانه گلستان.
</p>

<p align="center">
  <a href="https://github.com/ShayanEndless/MyUni/releases/latest">
    <img src="https://img.shields.io/badge/📥_دانلود_مستقیم_فایل_APK-v1.0.0-2EA44F?style=for-the-badge&logo=android&logoColor=white" alt="Download APK" />
  </a>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Kotlin-1.9+-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Jetpack_Compose-Material_3-4285F4?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Supabase-Backend-3ECF8E?style=for-the-badge&logo=supabase&logoColor=white" />
  <img src="https://img.shields.io/badge/Min_SDK-24_(Android_7.0)-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
</p>

---

## 📱 دانلود برنامه (Download App)

برای دانلود مستقیم آخرین نسخه فایل نصب اندروید (APK)، روی لینک زیر کلیک کنید:

➡️ **[دانلود آخرین نسخه APK برنامه (v1.0.0)](https://github.com/ShayanEndless/MyUni/releases/tag/v1.0.0)**

---

## 🌟 امکانات اصلی (Key Features)

- 📅 **مدیریت برنامه هفتگی و کلاس‌ها:**
  - استخراج و پارس خودکار برنامه درسی از فایل‌های PDF و گزارش‌های سامانه گلستان.
  - نمایش شیک و منظم زمان‌بندی کلاس‌ها بر اساس روزهای هفته.

- ⏰ **سیستم یادآور و هشدارهای هوشمند (Alarms & Notifications):**
  - **یادآور کلاس‌ها:** هشدار قبل از شروع هر کلاس.
  - **یادآور سلف و غذا:** هشدار رزرو و دریافت غذای دانشگاه (`FoodAlarmReceiver`).
  - **یادآور ددلاین‌ها:** ثبت و یادآوری تکالیف و پروژه‌های درسی (`DeadlineAlarmReceiver`).
  - **پشتیبانی از Boot Receiver:** تنظیم مجدد خودکار آلارم‌ها پس از روشن شدن گوشی.

- ☁️ **اتصال به بک‌اند Supabase:**
  - ثبت و مدیریت پروفایل کاربری.
  - سیستم دریافت بازخورد و نظرات کاربران (Feedback).
  - چک کردن خودکار آپدیت‌های جدید برنامه (`AppUpdateHelper`).
  - پنل مدیریت (Admin Panel).


## 🛠 تکنولوژی‌ها و کتابخانه‌ها (Tech Stack)

| بخش | تکنولوژی / کتابخانه |
| :--- | :--- |
| **زبان توسعه** | [Kotlin](https://kotlinlang.org/) |
| **رابط کاربری (UI)** | [Jetpack Compose](https://developer.android.com/jetpack/compose) + Material 3 |
| **بک‌اند & پایگاه‌داده** | [Supabase](https://supabase.com/) (Postgrest, Storage) |
| **پردازش فایل PDF** | [PdfBox Android](https://github.com/TomRous3/PdfBox-Android) |
| **پردازش پس‌زمینه** | Android WorkManager & AlarmManager |
| **ارتباطات شبکه** | Ktor Client & Kotlinx Serialization |

---


---

## 📄 لیسانس (License)
این پروژه جهت استفاده و یادگیری عمومی قرار گرفته است.
