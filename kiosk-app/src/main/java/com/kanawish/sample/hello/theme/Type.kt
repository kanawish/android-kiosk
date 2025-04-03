package com.kanawish.sample.hello.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.PlatformTextStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.kanawish.sample.hello.R

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
     */
)

val AppTypography = Typography(
    // Display styles (largest, most prominent text)
    displayLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.switzer_variable)),
        fontSize = 60.sp,
        fontWeight = FontWeight.SemiBold,
        platformStyle = PlatformTextStyle(includeFontPadding = false),
        lineHeight = 60.sp
    ),
    displayMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.switzer_variable)),
        fontSize = 48.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 48.sp
    ),
    displaySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.switzer_variable)),
        fontSize = 36.sp,
        fontWeight = FontWeight.SemiBold,
        lineHeight = 44.sp
    ),

    // Headline styles (secondary level of prominence)
    headlineLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.switzer_variable)),
        fontSize = 20.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 28.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.switzer_variable)),
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 24.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.switzer_variable)),
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        lineHeight = 22.sp
    ),

    // Title styles (smaller than headlines, used for section headers)
    titleLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.cascadia_code)),
        fontSize = 32.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 40.sp
    ),
    titleMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.cascadia_code)),
        fontSize = 28.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 36.sp
    ),
    titleSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.cascadia_code)),
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 32.sp
    ),

    // Body styles
    bodyLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.switzer_variable)),
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.switzer_variable)),
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 20.sp
    ),
    bodySmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.switzer_variable)),
        fontSize = 12.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 16.sp
    ),

    // Label styles
    labelLarge = TextStyle(
        fontFamily = FontFamily(Font(R.font.switzer_variable)),
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 24.sp
    ),
    labelMedium = TextStyle(
        fontFamily = FontFamily(Font(R.font.switzer_variable)),
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 20.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily(Font(R.font.switzer_variable)),
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        lineHeight = 16.sp
    )
)
