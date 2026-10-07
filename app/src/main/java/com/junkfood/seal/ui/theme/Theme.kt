package com.junkfood.seal.ui.theme

import android.os.Build
import android.view.WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextDirection
import com.google.android.material.color.MaterialColors
import com.junkfood.seal.ui.common.LocalFixedColorRoles
import com.kyant.monet.LocalTonalPalettes
import com.kyant.monet.dynamicColorScheme

fun Color.applyOpacity(enabled: Boolean): Color {
    return if (enabled) this else this.copy(alpha = 0.62f)
}

@Composable
@ReadOnlyComposable
fun Color.harmonizeWith(other: Color) =
    Color(MaterialColors.harmonize(this.toArgb(), other.toArgb()))

@Composable
@ReadOnlyComposable
fun Color.harmonizeWithPrimary(): Color =
    this.harmonizeWith(other = MaterialTheme.colorScheme.primary)

private val PineGold = Color(0xFFFFD700)
private val OnPineGold = Color(0xFF1A1400)
private val PineGoldContainerDark = Color(0xFF4A3A00)
private val OnPineGoldContainerDark = Color(0xFFFFE97A)
private val PineGreen = Color(0xFF32CD32)
private val OnPineGreen = Color(0xFF04220A)
private val PineGreenContainerDark = Color(0xFF1B5B23)
private val OnPineGreenContainerDark = Color(0xFF9CF2A4)
private val PineGreenLight = Color(0xFF1E7A24)
private val PineGoldLight = Color(0xFF6B5400)
private val PineGoldContainerLight = Color(0xFFFFF0A8)
private val OnPineGoldContainerLight = Color(0xFF241C00)

private val PineBase = Color(0xFF121212)
private val PineElevated = Color(0xFF1A1A1A)
private val PineCard = Color(0xFF1E1E1E)
private val PineCardHigh = Color(0xFF242424)
private val PineCardHighest = Color(0xFF2C2C2C)
private val PineOutermost = Color(0xFF0C0C0C)

@Composable
fun SealTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    isHighContrastModeEnabled: Boolean = false,
    content: @Composable () -> Unit,
) {
    val view = LocalView.current

    LaunchedEffect(darkTheme) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (darkTheme) {
                view.windowInsetsController?.setSystemBarsAppearance(
                    0,
                    APPEARANCE_LIGHT_STATUS_BARS,
                )
            } else {
                view.windowInsetsController?.setSystemBarsAppearance(
                    APPEARANCE_LIGHT_STATUS_BARS,
                    APPEARANCE_LIGHT_STATUS_BARS,
                )
            }
        }
    }

    val colorScheme =
        dynamicColorScheme(!darkTheme).run {
            if (isHighContrastModeEnabled && darkTheme)
                copy(
                    surface = Color.Black,
                    background = Color.Black,
                    surfaceContainerLowest = Color.Black,
                    surfaceContainerLow = surfaceContainerLowest,
                    surfaceContainer = surfaceContainerLow,
                    surfaceContainerHigh = surfaceContainerLow,
                    surfaceContainerHighest = surfaceContainer,
                )
            else if (darkTheme)
                copy(
                    primary = PineGold,
                    onPrimary = OnPineGold,
                    primaryContainer = PineGoldContainerDark,
                    onPrimaryContainer = OnPineGoldContainerDark,
                    secondary = PineGreen,
                    onSecondary = OnPineGreen,
                    secondaryContainer = PineGreenContainerDark,
                    onSecondaryContainer = OnPineGreenContainerDark,
                    tertiary = PineGreen,
                    onTertiary = OnPineGreen,
                    tertiaryContainer = PineGreenContainerDark,
                    onTertiaryContainer = OnPineGreenContainerDark,
                    surface = PineBase,
                    background = PineBase,
                    surfaceDim = PineBase,
                    surfaceContainerLowest = PineOutermost,
                    surfaceContainerLow = PineBase,
                    surfaceContainer = PineCard,
                    surfaceContainerHigh = PineCardHigh,
                    surfaceContainerHighest = PineCardHighest,
                    surfaceVariant = PineElevated,
                    outline = Color(0xFF8A8A8A),
                    outlineVariant = Color(0xFF3A3A3A),
                    onSurface = Color.White,
                    onSurfaceVariant = Color(0xFFB0B0B0),
                )
            else
                copy(
                    primary = PineGoldLight,
                    onPrimary = Color.White,
                    primaryContainer = PineGoldContainerLight,
                    onPrimaryContainer = OnPineGoldContainerLight,
                    secondary = PineGreenLight,
                    onSecondary = Color.White,
                    tertiary = PineGreenLight,
                    onTertiary = Color.White,
                )
        }

    val textStyle =
        LocalTextStyle.current.copy(
            lineBreak = LineBreak.Paragraph,
            textDirection = TextDirection.Content,
        )

    val tonalPalettes = LocalTonalPalettes.current

    CompositionLocalProvider(
        LocalFixedColorRoles provides FixedColorRoles.fromTonalPalettes(tonalPalettes),
        LocalTextStyle provides textStyle,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            shapes = Shapes,
            content = content,
        )
    }
}

@Composable
@Deprecated("Use SealTheme instead", replaceWith = ReplaceWith("SealTheme(content)"))
fun PreviewThemeLight(content: @Composable () -> Unit) {
    SealTheme(darkTheme = false, content = content)
}
