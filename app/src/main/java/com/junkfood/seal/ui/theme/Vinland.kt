package com.junkfood.seal.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

/**
 * Vinland Design System (VDS) v1.0.0 tokens, adapted from
 * `vinland-design-system/tokens.md` to Material3 [ColorScheme] roles.
 *
 * Rules honored: OLED dark-first canvas, 1-step surfaces, purpose-driven
 * accents (blood = destructive only, wheat = brand/links/active), radii ≤ 4px
 * (see [Shapes]), no decorative gradients.
 */
object Vinland {
    // Dark-first surfaces & borders
    val Canvas = Color(0xFF08090B)
    val Iron = Color(0xFF111318)
    val Steel = Color(0xFF181B22)
    val Raised = Color(0xFF1F232E)
    val BorderBlade = Color(0xFF242833)
    val BorderSharp = Color(0xFF363B49)

    // Dark-first text
    val Bone = Color(0xFFE6E4DD)
    val Ash = Color(0xFF828997)
    val Muted = Color(0xFF4B5160)

    // Accents (preserved across themes)
    val Blood = Color(0xFF9E1B22)
    val BloodHover = Color(0xFFBF222B)
    val BloodText = Color(0xFFE86A72)
    val Wheat = Color(0xFFC89B53)
    val WheatHover = Color(0xFFDCAF66)

    // Semantic
    val Success = Color(0xFF5BA87B)
    val SuccessText = Color(0xFF8FD3A6)
    val Info = Color(0xFF7A9BB8)
    val InfoText = Color(0xFF9DBDDA)

    // Light (Parchment Oath) map — tokens.md §1.5
    val Parchment = Color(0xFFF2EFE7)
    val PaperWhite = Color(0xFFFFFFFF)
    val SandSteel = Color(0xFFE9E6DC)
    val SandBorder = Color(0xFFD1CCBD)
    val SandSharp = Color(0xFFB3AB94)
    val InkText = Color(0xFF14161B)
    val SlateText = Color(0xFF5A6070)
    val SandMuted = Color(0xFF8A8FA0)
    val WheatDark = Color(0xFF8A682E)
}

fun vinlandDarkColorScheme(): ColorScheme =
    darkColorScheme(
        primary = Vinland.Wheat,
        onPrimary = Vinland.Canvas,
        primaryContainer = Vinland.Steel,
        onPrimaryContainer = Vinland.WheatHover,
        secondary = Vinland.Ash,
        onSecondary = Vinland.Canvas,
        secondaryContainer = Vinland.Steel,
        onSecondaryContainer = Vinland.Bone,
        tertiary = Vinland.Wheat,
        onTertiary = Vinland.Canvas,
        tertiaryContainer = Vinland.Steel,
        onTertiaryContainer = Vinland.WheatHover,
        background = Vinland.Canvas,
        onBackground = Vinland.Bone,
        surface = Vinland.Canvas,
        onSurface = Vinland.Bone,
        surfaceVariant = Vinland.Steel,
        onSurfaceVariant = Vinland.Ash,
        surfaceTint = Vinland.Wheat,
        inverseSurface = Vinland.Bone,
        inverseOnSurface = Vinland.Canvas,
        inversePrimary = Vinland.WheatDark,
        surfaceDim = Vinland.Canvas,
        surfaceBright = Vinland.Raised,
        surfaceContainerLowest = Vinland.Canvas,
        surfaceContainerLow = Vinland.Canvas,
        surfaceContainer = Vinland.Iron,
        surfaceContainerHigh = Vinland.Steel,
        surfaceContainerHighest = Vinland.Raised,
        outline = Vinland.BorderBlade,
        outlineVariant = Vinland.BorderSharp,
        scrim = Color.Black,
        error = Vinland.BloodHover,
        onError = Color.White,
        errorContainer = Vinland.Steel,
        onErrorContainer = Vinland.BloodText,
    )

fun vinlandLightColorScheme(): ColorScheme =
    lightColorScheme(
        primary = Vinland.WheatDark,
        onPrimary = Color.White,
        primaryContainer = Vinland.SandSteel,
        onPrimaryContainer = Vinland.WheatDark,
        secondary = Vinland.SlateText,
        onSecondary = Color.White,
        secondaryContainer = Vinland.SandSteel,
        onSecondaryContainer = Vinland.InkText,
        tertiary = Vinland.WheatDark,
        onTertiary = Color.White,
        tertiaryContainer = Vinland.SandSteel,
        onTertiaryContainer = Vinland.WheatDark,
        background = Vinland.Parchment,
        onBackground = Vinland.InkText,
        surface = Vinland.Parchment,
        onSurface = Vinland.InkText,
        surfaceVariant = Vinland.SandSteel,
        onSurfaceVariant = Vinland.SlateText,
        surfaceTint = Vinland.WheatDark,
        inverseSurface = Vinland.InkText,
        inverseOnSurface = Vinland.Parchment,
        inversePrimary = Vinland.Wheat,
        surfaceDim = Vinland.SandSteel,
        surfaceBright = Vinland.PaperWhite,
        surfaceContainerLowest = Vinland.PaperWhite,
        surfaceContainerLow = Vinland.Parchment,
        surfaceContainer = Vinland.PaperWhite,
        surfaceContainerHigh = Vinland.SandSteel,
        surfaceContainerHighest = Vinland.SandSteel,
        outline = Vinland.SandBorder,
        outlineVariant = Vinland.SandSharp,
        scrim = Color.Black,
        error = Vinland.Blood,
        onError = Color.White,
        errorContainer = Vinland.SandSteel,
        onErrorContainer = Vinland.Blood,
    )
