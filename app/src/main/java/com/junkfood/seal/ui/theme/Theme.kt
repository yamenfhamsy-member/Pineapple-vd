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

private val BotanicalBlack = Color(0xFF0B120E)
private val BotanicalLowest = Color(0xFF060A08)
private val BotanicalContainer = Color(0xFF101713)
private val BotanicalHigh = Color(0xFF151D18)
private val BotanicalHighest = Color(0xFF1B241E)

private val ElectricYellow = Color(0xFFF5A623)
private val OnElectricYellow = Color(0xFF1F1400)
private val ElectricYellowContainerDark = Color(0xFF6B4300)
private val OnElectricYellowContainerDark = Color(0xFFFFE0AE)
private val ElectricYellowLight = Color(0xFF7A5200)
private val ElectricYellowContainerLight = Color(0xFFFFDDB5)
private val OnElectricYellowContainerLight = Color(0xFF291800)

private val LeafAccentDark = Color(0xFF93D89A)
private val OnLeafAccentDark = Color(0xFF0B2512)
private val LeafContainerDark = Color(0xFF1C4E28)
private val OnLeafContainerDark = Color(0xFFB9F0BE)
private val LeafAccentLight = Color(0xFF2E6B34)
private val LeafContainerLight = Color(0xFFB2F1B8)
private val OnLeafContainerLight = Color(0xFF07270F)

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
                    primary = ElectricYellow,
                    onPrimary = OnElectricYellow,
                    primaryContainer = ElectricYellowContainerDark,
                    onPrimaryContainer = OnElectricYellowContainerDark,
                    tertiary = LeafAccentDark,
                    onTertiary = OnLeafAccentDark,
                    tertiaryContainer = LeafContainerDark,
                    onTertiaryContainer = OnLeafContainerDark,
                    surface = BotanicalBlack,
                    background = BotanicalBlack,
                    surfaceDim = BotanicalBlack,
                    surfaceContainerLowest = BotanicalLowest,
                    surfaceContainerLow = BotanicalBlack,
                    surfaceContainer = BotanicalContainer,
                    surfaceContainerHigh = BotanicalHigh,
                    surfaceContainerHighest = BotanicalHighest,
                    surfaceVariant = BotanicalContainer,
                )
            else
                copy(
                    primary = ElectricYellowLight,
                    onPrimary = Color.White,
                    primaryContainer = ElectricYellowContainerLight,
                    onPrimaryContainer = OnElectricYellowContainerLight,
                    tertiary = LeafAccentLight,
                    onTertiary = Color.White,
                    tertiaryContainer = LeafContainerLight,
                    onTertiaryContainer = OnLeafContainerLight,
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
