package com.junkfood.seal.ui.page.settings.appearance

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import com.junkfood.seal.R
import com.junkfood.seal.ui.common.LocalDarkTheme
import com.junkfood.seal.ui.component.BackButton
import com.junkfood.seal.ui.component.PreferenceSingleChoiceItem
import com.junkfood.seal.util.DarkThemePreference.Companion.OFF
import com.junkfood.seal.util.DarkThemePreference.Companion.ON
import com.junkfood.seal.util.PreferenceUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DarkThemePreferences(onNavigateBack: () -> Unit) {
    val scrollBehavior =
        TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
            rememberTopAppBarState(),
            canScroll = { true },
        )
    val darkThemePreference = LocalDarkTheme.current
    Scaffold(
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(modifier = Modifier, text = stringResource(id = R.string.dark_theme))
                },
                navigationIcon = { BackButton { onNavigateBack() } },
                scrollBehavior = scrollBehavior,
            )
        },
        content = {
            LazyColumn(modifier = Modifier, contentPadding = it) {
                item {
                    PreferenceSingleChoiceItem(
                        text = stringResource(R.string.on),
                        selected = darkThemePreference.darkThemeValue == ON,
                    ) {
                        PreferenceUtil.modifyDarkThemePreference(ON)
                    }
                }
                item {
                    PreferenceSingleChoiceItem(
                        text = stringResource(R.string.off),
                        selected = darkThemePreference.darkThemeValue == OFF,
                    ) {
                        PreferenceUtil.modifyDarkThemePreference(OFF)
                    }
                }
            }
        },
    )
}
