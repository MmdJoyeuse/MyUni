package com.example.myuni

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

object AppConfig {
    // ==========================================
    // 1. تنظیمات خطوط و جدول (Table Lines)
    // ==========================================
    val lineThickness: Dp = 0.5.dp       // ضخامت خطوط افقی جدول
    val lineAlpha: Float = 0.6f          // میزان پررنگی (شفافیت) خطوط افقی از 0.0 تا 1.0
    val vLineThickness: Dp = 0.5.dp      // ضخامت خطوط عمودی جدول
    val vLineAlpha: Float = 0.6f         // میزان پررنگی (شفافیت) خطوط عمودی از 0.0 تا 1.0

    // ==========================================
    // 2. تنظیمات فونت متون جدول (Table Fonts)
    // ==========================================
    val blockFontSize: TextUnit = 13.sp        // اندازه فونت متن داخل باکس‌های برنامه (بر حسب sp)
    val timeLabelFontSize: TextUnit = 11.sp    // اندازه فونت اعداد ساعت‌ها در ستون زمان (بر حسب sp)
    val timeTillFontSize: TextUnit = 14.sp      // اندازه فونت کلمه «تا» در ستون زمان (بر حسب sp)

    // ==========================================
    // 3. تنظیمات تراز و ارتفاع ساعت‌ها در ستون زمان
    // ==========================================
    val firstHourOffset: Dp = (-6).dp     // تنظیم ارتفاع ساعت اول (۸:۳۰ یا ۸:۰۰)
    val middleHourOffset: Dp = (-12).dp   // تنظیم ارتفاع ساعت‌های میانی (۹ تا ۱۷)
    val lastHourOffset: Dp = 4.dp         // تنظیم ارتفاع ساعت آخر (۱۸:۰۰)

    // ==========================================
    // 4. تنظیمات استایل و ظاهر دیالوگ‌ها (Dialog Styles)
    // ==========================================
    val dialogCornerRadius: Dp = 28.dp              // شعاع گردی گوشه‌های اصلی دیالوگ
    val dialogInnerCornerRadius: Dp = 14.dp         // شعاع گردی کارت‌ها و باکس‌های داخل دیالوگ
    val dialogButtonCornerRadius: Dp = 10.dp        // شعاع گردی دکمه‌های تایید و لغو دیالوگ
    val dialogTonalElevation: Dp = 6.dp             // ارتفاع و سایه (Tonal Elevation) دیالوگ

    val dialogPadding: Dp = 24.dp                   // پدینگ داخلی دیالوگ‌ها
    val dialogContentSpacing: Dp = 16.dp            // فاصله عمودی بین عنوان، متن و دکمه‌های دیالوگ
    val dialogButtonSpacing: Dp = 8.dp              // فاصله افقی بین دکمه‌های دیالوگ

    val dialogTitleFontSize: TextUnit = 17.sp       // اندازه فونت عنوان اصلی دیالوگ‌ها
    val dialogTitleFontWeight: FontWeight = FontWeight.Bold // ضخامت فونت عنوان دیالوگ‌ها
    val dialogSubtitleFontSize: TextUnit = 13.sp    // اندازه فونت عناوین فرعی و زیربخش‌های دیالوگ
    val dialogBodyFontSize: TextUnit = 12.sp        // اندازه فونت متن توضیحات و بدنه دیالوگ
    val dialogButtonFontSize: TextUnit = 14.sp      // اندازه فونت متن دکمه‌های دیالوگ

    val dialogCardContainerAlpha: Float = 0.35f     // شفافیت پس‌زمینه کارت‌های داخل دیالوگ
    val dialogCardBorderAlpha: Float = 0.4f        // شفافیت خط دور (Border) کارت‌های داخل دیالوگ

    // ==========================================
    // 5. تنظیمات نوار پایین برنامه (Bottom Navigation Bar)
    // ==========================================
    val bottomBarIconSize: Dp = 24.dp             // اندازه آیکون‌های پایین (کم و زیاد کردن سایز آیکون)
    val bottomBarTextSize: TextUnit = 12.sp        // اندازه فونت متون زیر آیکون (کم و زیاد کردن سایز نوشته)
    val bottomBarItemSpacing: Dp = (-4).dp        // فاصله آیکون تا نوشته (منفی‌تر = نزدیک‌تر، مثبت‌تر = دورتر)
    val bottomBarHeight: Dp = 64.dp               // ارتفاع کلی نوار پایین برنامه
    val bottomBarHorizontalPadding: Dp = 36.dp    // حاشیه افقی نوار پایین (عدد بزرگتر = کادر باریک‌تر و دکمه‌ها نزدیک‌تر)
    val bottomBarBottomPadding: Dp = 20.dp        // حاشیه از پایین صفحه (عدد بزرگتر = بالاتر رفتن نوار)
    val bottomBarHaloWidth: Dp = 78.dp            // عرض هاله (پوشش کامل آیکون و نوشته)
    val bottomBarHaloHeight: Dp = 54.dp           // ارتفاع هاله (بزرگتر عمودی و کاملاً مرکز کادر)
    val bottomBarHaloAlpha: Float = 0.18f         // میزان شفافیت پس‌زمینه هاله

    // ==========================================
    // 6. تنظیمات سرعت انیمیشن جابجایی تب‌ها (Tab Transition Speed)
    // ==========================================
    val tabTransitionDurationMillis: Int = 185    // مدت زمان انیمیشن جابجایی صفحات (میلی‌ثانیه) - عدد کمتر = سرعت بیشتر

    // ==========================================
    // 7. تنظیمات کارت‌های بخش ارتباطات و مسئولین (Contacts Section)
    // ==========================================
    val contactTitleFontSize: TextUnit = 12.5.sp  // اندازه فونت منصب/عنوان مسئولین (بر حسب sp)
    val contactNameFontSize: TextUnit = 14.5.sp   // اندازه فونت نام و مشخصات مسئولین (بر حسب sp)

    // ==========================================
    // 8. تنظیمات اختصاصی سکشن‌های داشبورد (Dashboard Sections Layout)
    // ==========================================
    val dashboardTopPadding: Dp = 10.dp           // فاصله از بالای صفحه داشبورد

    // --- سکشن ۱: کادر و اساتید ---
    val staffSectionTopSpacing: Dp = 0.dp         // فاصله بالای سکشن کادر و اساتید
    val staffSectionHeight: Dp = 132.dp           // ارتفاع محتوای داخلی کادر و اساتید (نمایش هم‌زمان ۴ گزینه / ۲ ردیف)
    val staffSectionBottomSpacing: Dp = 0.dp     // فاصله پایین سکشن کادر و اساتید

    // --- سکشن ۱.۵: مکان‌ها ---
    val locationsSectionTopSpacing: Dp = 10.dp     // فاصله بالای سکشن مکان‌ها
    val locationsSectionBottomSpacing: Dp = 0.dp  // فاصله پایین سکشن مکان‌ها

    // --- سکشن ۲: کانال‌ها و گروه‌ها ---
    val channelsSectionTopSpacing: Dp = 10.dp      // فاصله بالای سکشن کانال‌ها و گروه‌ها
    val channelsSectionBottomSpacing: Dp = 0.dp  // فاصله پایین سکشن کانال‌ها و گروه‌ها

    // --- سکشن ۳: سامانه‌های دانشگاه ---
    val sitesSectionTopSpacing: Dp = 10.dp         // فاصله بالای سکشن سامانه‌های دانشگاه
    val sitesSectionBottomSpacing: Dp = 16.dp     // فاصله پایین سکشن سامانه‌های دانشگاه

    // --- تنظیمات مختصات مکان‌ها (Location Coordinates) ---
    val universityLat: Double = 31.977018649131935
    val universityLng: Double = 51.81245693370846
    val mensDormLat: Double = 32.03576250943657
    val mensDormLng: Double = 51.87957995440486
    val womensDormLat: Double = 32.01356399999999
    val womensDormLng: Double = 51.873874099999995

    // ==========================================
    // 9. تنظیمات کادر، اسکیل و پوزیشن عکس‌های راهنمای استخراج از گلستان (Golestan Guide Image Settings)
    // ==========================================
    val golestanGuideBoxWidth: Dp = 240.dp        // عرض کادر عکس (عدد به dp - مثلاً 340.dp، اگر 0.dp بذارید عرض کامل می‌شود)
    val golestanGuideBoxMinWidth: Dp = 260.dp     // حداقل عرض کادر عکس راهنما (بر حسب dp)
    val golestanGuideBoxMaxWidth: Dp = 380.dp     // حداکثر عرض کادر عکس راهنما (بر حسب dp)
    val golestanGuideBoxMinHeight: Dp = 280.dp    // حداقل ارتفاع کادر عکس راهنما (بر حسب dp)
    val golestanGuideBoxMaxHeight: Dp = 420.dp    // حداکثر ارتفاع کادر عکس راهنما (بر حسب dp)
    val golestanGuideBoxCornerRadius: Dp = 14.dp  // شعاع گردی کادر عکس راهنما (بر حسب dp)
    val golestanGuideBoxBorderWidth: Dp = 1.5.dp  // ضخامت خط دور کادر عکس راهنما (بر حسب dp)
    val golestanGuideBoxBorderAlpha: Float = 0.4f // میزان شفافیت خط دور کادر (از 0.0 تا 1.0)
    val golestanGuideBoxBgAlpha: Float = 0.3f     // میزان شفافیت پس‌زمینه کادر (از 0.0 تا 1.0)

    val golestanGuideImageScale: Float = 1.0f     // میزان اسکیل (زوم) اولیه عکس راهنما (1.0 = حالت عادی)
    val golestanGuideMinScale: Float = 1.0f       // حداقل اسکیل (زوم) عکس
    val golestanGuideMaxScale: Float = 5.0f       // حداکثر اسکیل (زوم) عکس
    val golestanGuideInitialOffsetX: Float = 0.0f // جابجایی (پوزیشن) افقی عکس (X)
    val golestanGuideInitialOffsetY: Float = 0.0f // جابجایی (پوزیشن) عمودی عکس (Y)

    // ==========================================
    // 5. متون و عناوین دیالوگ‌های برنامه (Dialog Texts)
    // ==========================================
    object DialogTexts {
        // --- دکمه‌های عمومی ---
        var confirm = "تایید"
        var cancel = "لغو"
        var dismiss = "متوجه شدم"
        var close = "بستن"
        var delete = "حذف"
        var save = "ذخیره"
        var edit = "ویرایش"

        // --- دیالوگ تنظیمات جدول ---
        var tableSettingsTitle = "تنظیمات برنامه درسی"
        var tableModeSectionHeader = "حالت جدول و وضعیت هفته"
        var tableModeGolestan = "ساعات سایت"
        var tableModeHolding = "حالت زوج و فرد"
        var weekParityToggleText = "تغییر وضعیت هفته جاری (زوج/فرد)"
        var activeDaysHeader = "روزهای فعال در جدول"
        var activeHoursHeader = "⏰ ساعت‌های فعال در جدول"
        var toolsSectionHeader = "ابزارها و مدیریت دروس"
        var pdfExtractButton = "استخراج برنامه از فایل PDF"
        var clearAllCoursesButton = "حذف همه دروس جدول"

        // --- دیالوگ حذف همه دروس ---
        var clearConfirmTitle = "حذف همه دروس"
        var clearConfirmMessage = "آیا از پاک کردن تمامی دروس جدول اطمینان دارید؟ این عمل غیرقابل بازگشت است."
        var clearConfirmActionButton = "بله، حذف کن"

        // --- دیالوگ ویرایش/راهنمای دروس ---
        var editConfirmTitle = "ویرایش برنامه"
        var editConfirmMessage = "برای تغییر یا ویرایش دروس، روی درس مورد نظر در جدول کلیک کنید یا از گزینه تنظیمات استفاده نمایید."

        // --- دیالوگ انتخاب روش افزودن درس ---
        var addChoiceTitle = "روش افزودن درس"
        var addChoiceGolestanOption = "ورود مستقیم از سایت گلستان"
        var addChoiceManualOption = "افزودن دستی درس"

        // --- دیالوگ خوش‌آمدگویی اولین بار ---
        var welcomeTitle = "به برنامه خوش آمدید!"
        var welcomeMessage = "برای شروع، می‌توانید برنامه درسی خود را مستقیم از سایت گلستان دریافت کنید یا دروس را دستی وارد کنید."
        var welcomeGolestanButton = "دریافت از گلستان"
        var welcomeManualButton = "ورود دستی"

        // --- دیالوگ راهنمای گلستان ---
        var golestanGuideTitle = "راهنمای ورود از گلستان"
        var golestanGuideMessage = "لطفاً در صفحه باز شده وارد حساب کاربری خود شوید و کد گزارش ۷۸ یا ۴۲۳ را مشاهده کنید تا برنامه به صورت خودکار دریافت شود."

        // --- دیالوگ افزودن/ویرایش ددلاین ---
        var deadlineAddTitle = "افزودن ددلاین جدید"
        var deadlineEditTitle = "ویرایش ددلاین"

        // --- دیالوگ آپدیت برنامه ---
        var updateAvailableTitleFormat = "نسخه جدید موجود است! (%s)"
        var updateReleaseNotesHeader = "تغییرات نسخه جدید:"
        var updateDefaultMessage = "نسخه جدید برنامه آماده دانلود می‌باشد."
        var updateConfirmButton = "دانلود و نصب"
        var updateDismissButton = "بعداً"

        // --- دیالوگ نظرات و پشتیبانی ---
        var feedbackTitle = "ارسال نظر و پشتیبانی"
        var feedbackEmptyMessage = "هنوز پیامی ارسال نکرده‌اید.\nهرگونه نظر، پیشنهاد یا مشکلی دارید بنویسید!"
        var feedbackPlaceholder = "پیام خود را بنویسید..."

        // --- دیالوگ‌های PDF ---
        var pdfResultTitle = "متن استخراج‌شده از PDF"
        var pdfProcessButton = "پردازش و تبدیل به جدول"
        var pdfReverseLinesButton = "معکوس کردن خطوط (رفع بهم‌ریختگی)"
        var pdfLoadingTitle = "در حال خواندن PDF..."
        var pdfLoadingSubtitle = "لطفا چند لحظه شکیبا باشید"
        var parsedCoursesTitle = "دروس تشخیص‌داده‌شده از PDF"
        var parsedCoursesEmptyMessage = "هیچ درسی به صورت خودکار از الگوی فایل شناسایی نشد.\nمی‌توانید به صورت دستی با دکمه زیر دروس خود را اضافه کنید:"
        var parsedCoursesApplyButton = "افزودن دروس انتخاب‌شده"


    }
}
