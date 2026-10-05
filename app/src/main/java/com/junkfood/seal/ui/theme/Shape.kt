package com.junkfood.seal.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

/**
 * Vinland Design System geometry: radius cap 0–4px, pills forbidden.
 * extraSmall/small (buttons, chips) = 4px max, medium/large/extraLarge
 * (cards, sheets, dialogs) = 4px surface.
 */
val Shapes =
    Shapes(
        extraSmall = RoundedCornerShape(4.dp),
        small = RoundedCornerShape(4.dp),
        medium = RoundedCornerShape(4.dp),
        large = RoundedCornerShape(4.dp),
        extraLarge = RoundedCornerShape(4.dp),
    )
