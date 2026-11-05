# تحلیل ساختار سامانه گلستان دانشگاه اصفهان و معماری اپلیکیشن اندروید

این سند تحلیل فنی سامانه گلستان دانشگاه اصفهان (بر اساس درخواست کاربر و داده‌های فایل HAR) و معماری تمیز (Clean Architecture) پیاده‌سازی‌شده در اپلیکیشن **My Uni** را تشریح می‌کند.

---

## مرحله ۱ — بررسی ساختار سامانه و تحلیل ترافیک (Network Analysis)

بر اساس بررسی فایل HAR و ساختار سامانه گلستان (مبتنی بر تکنولوژی ASP.NET WebForms قدیمی):

1. **مکانیزم احراز هویت (Login):**
   - صفحه اصلی (`https://golestan.ui.ac.ir/forms/authenticateuser/main.htm`) شامل فریم‌ها و فرم‌های ورود است.
   - ورود به سامانه نیازمند ارسال درخواست POST به همراه پارامترهای ASP.NET نظیر `__VIEWSTATE`، `__EVENTVALIDATION`، نام کاربری و رمز عبور است.
   - با توجه به استفاده از مکانیزم‌های امنیتی، کدهای جاوا اسکریپت داخلی (`Commander.js`, `faci.js`) و مدیریت کوکی‌ها، بهترین و مطمئن‌ترین روش برای اپلیکیشن اندرویدی، استفاده از **معماری هیبرید (Hybrid Architecture)** است: استفاده از `WebView` امن برای مرحله احراز هویت اولیه و ست شدن Session Cookie (مثل `ASP.NET_SessionId`)، و سپس استخراج کوکی‌ها برای انجام درخواست‌های HTTP بعدی (OkHttp + Jsoup).

2. **شناسایی درخواست‌ها و Endpointها:**
   - پس از لاگین موفق، ناوبری سیستم از طریق URLهایی نظیر `/forms/f0240_process_authnav/nav.htm` و سپس `nav.aspx` انجام می‌شود.
   - درخواست‌های گزارش‌گیری (مثل گزارش ۷۸ یا انتخاب واحد/دروس) به فرم‌های پردازش گزارش ارسال می‌شوند:
     `https://golestan.ui.ac.ir/Forms/F0202_PROCESS_REP_FILTER/F0202_01_PROCESS_REP_FILTER_Dat.aspx`
   - این درخواست‌ها به صورت **POST** با پارامترهای ASP.NET و `XmlPriPrm` (مانند حاوی کدهای گزارش `id="4"` و `F="4051"`) ارسال شده و پاسخ HTML شامل جداول اطلاعاتی (`text/html; charset=utf-8`) را برمی‌گرداند.

3. **انتخاب Parser:**
   - برای استخراج داده‌ها از HTMLهای قدیمی و جدول‌بندی‌شده سامانه گلستان، کتابخانه **Jsoup** بهترین انتخاب است که به راحتی می‌تواند تگ‌ها، `<tr>`ها و `<td>`ها را بر اساس Selectorها یا متن سلول‌ها استخراج کند.

---

## مرحله ۲ — معماری اپلیکیشن (Clean Architecture + MVVM)

پروژه با ساختار ماژولار و تفکیک لایه‌ها پیاده‌سازی شده است:

```text
com.example.myuni/
├── data/
│   ├── remote/        # OkHttp client, Cookie Jar, Network services
│   ├── local/         # EncryptedSharedPreferences / DataStore for session & credentials
│   ├── parser/        # Jsoup HTML parsers (e.g., CourseScheduleParser)
│   └── repository/    # GolestanRepository implementation
├── domain/
│   ├── model/         # Kotlin Data Classes (Student, Course, Grade, ScheduleItem, Semester)
│   └── repository/    # Repository interfaces
└── presentation/
    ├── login/         # Login screen & LoginViewModel
    ├── dashboard/     # Dashboard screen & DashboardViewModel
    ├── courses/       # Courses screen
    ├── grades/        # Grades screen
    └── schedule/      # Schedule & calendar screen
```

---

## مرحله ۳ تا ۶ — مدل‌های داده، داشبورد و UI فارسی (RTL)

- **مدل‌های داده (Domain Models):**
  - `Student`: شامل نام، شماره دانشجویی، رشته، مقطع، دانشکده.
  - `Course`: شامل نام درس، کد درس، استاد، تعداد واحد، گروه، زمان و محل کلاس.
  - `Grade`: شامل نام درس، نمره، واحد، وضعیت قبولی، ترم.
  - `ScheduleItem`: آیتم‌های برنامه هفتگی.
- **داشبورد و UI:**
  - کاملاً فارسی، راست‌چین (RTL) با استفاده از Jetpack Compose و Material 3.
  - کارت‌های مجزا برای اطلاعات دانشجو، دروس ترم جاری، نمرات و برنامه هفتگی.

---

## مرحله ۷ و ۸ — مدیریت Session و Logging

- **مدیریت Session:**
  - ذخیره کوکی‌های نشست (Session Cookies) در فضای امن (`EncryptedSharedPreferences`).
  - بررسی اعتبار Session در زمان اجرای اپ؛ در صورت منقضی شدن (Expire شدن کوکی یا مواجهه با صفحه لاگین)، کاربر به صورت خودکار به صفحه ورود هدایت می‌شود.
  - مدیریت خطاهای شبکه و Timeoutها.
- **Logging و Debug:**
  - فعال‌سازی OkHttp HttpLoggingInterceptor (بدون ثبت اطلاعات حساس مانند رمز عبور) جهت نظارت دقیق بر Requestها، Status Codeها و Responseهای سامانه گلستان.

---

## راهنمای توسعه‌دهنده و نحوه افزودن بخش‌های جدید

1. **افزودن گزارش یا بخش جدید:**
   - آدرس Endpoint یا فرم مربوطه را در بخش `data/remote` اضافه کنید.
   - متد Parser متناظر را در `data/parser` با استفاده از Jsoup بنویسید تا HTML دریافتی را به Modelهای Domain تبدیل کند.
   - متد مربوطه را در `GolestanRepository` و سپس `DashboardViewModel` پیاده‌سازی کرده و در UI نمایش دهید.
