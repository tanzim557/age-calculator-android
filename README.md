# 📱 Age Calculator - Android App Documentation

A modern, clean, and fully functional **Age Calculator** Android application built with **Kotlin**, **Jetpack Compose (Material 3)**, and **MVVM Architecture**.

---

## 🌟 Features (মূল সুবিধাসমূহ)

1. **Exact Age Calculation (নিখুঁত বয়স গণনা)**:
   - Calculates age in **Years, Months, and Days** (e.g., `24 Years, 5 Months, 12 Days`).
   - Uses `java.time.Period` for accurate date math taking into account variable month lengths and leap years (February 29th).

2. **Next Birthday Countdown (পরবর্তী জন্মদিনের কাউন্টডাউন)**:
   - Displays exact days remaining until the next birthday.
   - Shows the exact day of the week (e.g., `Friday`, `Monday`) on which the next birthday falls.
   - Displays a celebratory `🎂 Happy Birthday! 🎉` banner if today is the user's birthday.

3. **Detailed Lifetime Statistics (জীবনকালের মোট পরিসংখ্যান)**:
   - **Total Days Lived** (মোট দিন)
   - **Total Weeks Lived** (মোট সপ্তাহ)
   - **Total Months Lived** (মোট মাস)
   - **Total Hours Lived** (মোট ঘণ্টা)

4. **Input Validation & Edge Cases (ভুল ইনপুট প্রতিরোধ)**:
   - Future date selection prevention (both in Date Picker calendar and runtime checks).
   - Instant **Reset** button to clear previous calculations.

5. **Modern Material 3 UI/UX (আধুনিক ডিজাইন)**:
   - Smooth animated transitions for calculation results.
   - Elevated cards, clean typography, and responsive Light/Dark theme support.

---

## 🛠️ Tech Stack & Dependencies (প্রযুক্তি ও লাইব্রেরি)

| Technology | Purpose |
| :--- | :--- |
| **Language** | Kotlin 2.0+ |
| **UI Framework** | Jetpack Compose with Material 3 |
| **Architecture** | MVVM (Model-View-ViewModel) + Unidirectional Data Flow (UDF) |
| **State Management** | Kotlin Coroutines & `StateFlow` |
| **Date & Time API** | `java.time` (`LocalDate`, `Period`, `ChronoUnit`) |
| **Compatibility** | Java 8+ Desugaring (`desugar_jdk_libs`) for Android API 24+ support |
| **Minimum SDK** | API 24 (Android 7.0 Nougat) |
| **Target SDK** | API 35 (Android 15) |

---

## 🏗️ Architecture & Data Flow (প্রজেক্ট আর্কিটেকচার)

The app follows the **MVVM** pattern with Unidirectional Data Flow:

```text
       ┌──────────────────────────────┐
       │   AgeCalculatorScreen (UI)   │
       └──────────────┬───────────────┘
                      │ (User Events: Pick Date, Click Calculate, Reset)
                      ▼
       ┌──────────────────────────────┐
       │    AgeCalculatorViewModel    │ ◄─── Business Logic & Date Math
       └──────────────┬───────────────┘
                      │ (Emits AgeCalculatorUiState via StateFlow)
                      ▼
       ┌──────────────────────────────┐
       │   UI Re-renders with State   │
       └──────────────────────────────┘
```

---

## 📁 Project Structure (ফাইল পরিচিতি)

```text
app/src/main/java/com/example/agecalculator/
│
├── data/
│   └── AgeCalculatorModel.kt       # AgeResult and AgeCalculatorUiState data classes
│
├── viewmodel/
│   └── AgeCalculatorViewModel.kt   # Calculation logic, leap-year handling, StateFlow
│
├── ui/
│   ├── theme/
│   │   ├── Color.kt               # Indigo, Teal, Coral Material 3 color palette
│   │   ├── Theme.kt               # Dynamic Light & Dark Mode theme configuration
│   │   └── Type.kt                # Typography definitions
│   │
│   ├── components/
│   │   ├── DatePickerModal.kt     # Material 3 DatePickerDialog with date restrictions
│   │   ├── HeroAgeCard.kt         # Main Age card displaying Years, Months, Days badges
│   │   └── StatCards.kt           # 2x2 Grid for Total Days, Weeks, Months, Hours & Next Birthday
│   │
│   └── AgeCalculatorScreen.kt     # Scaffold Screen with animated visibility and action buttons
│
└── MainActivity.kt                # Application entry point with Edge-to-Edge display
```

---

## 🧮 Calculation Logic Explained (কীভাবে হিসাব করা হয়)

### 1. Exact Age (Years, Months, Days)
```kotlin
val period = Period.between(birthDate, today)
val years = period.years
val months = period.months
val days = period.days
```

### 2. Lifetime Units
```kotlin
val totalDays = ChronoUnit.DAYS.between(birthDate, today)
val totalMonths = ChronoUnit.MONTHS.between(birthDate, today)
val totalWeeks = totalDays / 7
val totalHours = totalDays * 24
```

### 3. Next Birthday Calculation
```kotlin
var nextBdayYear = today.year
var nextBirthdayDate = safeLocalDate(nextBdayYear, birthDate.monthValue, birthDate.dayOfMonth)

if (nextBirthdayDate.isBefore(today)) {
    nextBdayYear += 1
    nextBirthdayDate = safeLocalDate(nextBdayYear, birthDate.monthValue, birthDate.dayOfMonth)
}

val daysRemaining = ChronoUnit.DAYS.between(today, nextBirthdayDate)
val dayOfWeek = nextBirthdayDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.getDefault())
```

---

## 🚀 How to Open and Run the Project

1. Open **Android Studio**.
2. Click **File $\rightarrow$ Open...** and select the project directory:
   ```text
   C:\Users\LIMA\Desktop\mubassira
   ```
3. Allow Android Studio to complete the **Gradle Sync**.
4. Connect an Android Device (or start an Emulator).
5. Click **Run ▶ (Shift + F10)**.

---

## 📄 License
This project is open-source and available under the **MIT License**.
