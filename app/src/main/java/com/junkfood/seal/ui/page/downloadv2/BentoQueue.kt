package com.junkfood.seal.ui.page.downloadv2

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentPaste
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.junkfood.seal.R
import com.junkfood.seal.download.Task
import com.junkfood.seal.download.Task.DownloadState.Canceled
import com.junkfood.seal.download.Task.DownloadState.Completed
import com.junkfood.seal.download.Task.DownloadState.Error
import com.junkfood.seal.download.Task.DownloadState.FetchingInfo
import com.junkfood.seal.download.Task.DownloadState.Idle
import com.junkfood.seal.download.Task.DownloadState.ReadyWithInfo
import com.junkfood.seal.download.Task.DownloadState.Running
import com.junkfood.seal.ui.common.AsyncImageImpl
import com.junkfood.seal.ui.common.LocalDarkTheme
import com.junkfood.seal.ui.component.GreenTonalPalettes
import com.junkfood.seal.util.findURLsFromString
import com.junkfood.seal.util.toDurationText
import java.util.Locale

private val BentoCardShape = RoundedCornerShape(14.dp)
private val BentoThumbShape = RoundedCornerShape(10.dp)
private val BentoStationShape = RoundedCornerShape(18.dp)
private val BentoLabelScrim = Color.Black.copy(alpha = 0.68f)

/**
 * Quick input station: a central field that auto-detects a video link from the clipboard with a
 * single action button (paste when empty, fetch when filled).
 */
@Composable
fun QuickInputStation(modifier: Modifier = Modifier, onSubmit: (String) -> Unit) {
    var text by rememberSaveable { mutableStateOf("") }
    val clipboardManager = LocalClipboardManager.current

    fun fillFromClipboard(): Boolean {
        val clip = clipboardManager.getText()?.toString() ?: return false
        val url = findURLsFromString(clip).firstOrNull() ?: return false
        text = url
        return true
    }

    fun submit() {
        val target = findURLsFromString(text).firstOrNull() ?: text.trim()
        if (target.isNotBlank()) onSubmit(target)
    }

    LaunchedEffect(Unit) { fillFromClipboard() }

    Surface(modifier = modifier.fillMaxWidth(), shape = BentoStationShape,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Row(
            modifier = Modifier.padding(start = 16.dp, end = 6.dp, top = 6.dp, bottom = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Filled.Link,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.width(8.dp))
            TextField(
                value = text,
                onValueChange = { text = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text(stringResource(R.string.quick_input_hint)) },
                singleLine = true,
                colors =
                    TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    ),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Go),
                keyboardActions = KeyboardActions(onGo = { submit() }),
            )
            FilledIconButton(
                onClick = {
                    if (text.isBlank()) {
                        fillFromClipboard()
                    } else {
                        submit()
                    }
                },
                modifier = Modifier.size(44.dp),
            ) {
                Icon(
                    imageVector =
                        if (text.isBlank()) Icons.Filled.ContentPaste else Icons.Filled.Download,
                    contentDescription = stringResource(R.string.quick_input_download),
                    modifier = Modifier.size(22.dp),
                )
            }
        }
    }
}

/**
 * Compact Bento queue row: smart thumbnail, title + status/speed line, a single action button, and
 * a thin progress bar fused to the bottom edge of the card.
 */
@Composable
fun BentoTaskRow(
    modifier: Modifier = Modifier,
    viewState: Task.ViewState,
    downloadState: Task.DownloadState,
    onActionPost: (UiAction) -> Unit,
    onButtonClick: () -> Unit,
) {
    Surface(
        modifier = modifier.fillMaxWidth().clip(BentoCardShape),
        shape = BentoCardShape,
        color = MaterialTheme.colorScheme.surfaceContainer,
        onClick = onButtonClick,
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier =
                        Modifier.size(68.dp)
                            .clip(BentoThumbShape)
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                ) {
                    viewState.thumbnailUrl?.let {
                        AsyncImageImpl(
                            model = it,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                    if (viewState.duration > 0) {
                        Surface(
                            modifier =
                                Modifier.align(Alignment.BottomEnd).padding(3.dp),
                            color = BentoLabelScrim,
                            shape = MaterialTheme.shapes.extraSmall,
                        ) {
                            Text(
                                text = viewState.duration.toDurationText(),
                                modifier = Modifier.padding(horizontal = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.White,
                            )
                        }
                    }
                }
                Column(modifier = Modifier.weight(1f).padding(horizontal = 12.dp)) {
                    Text(
                        text = viewState.title,
                        style = MaterialTheme.typography.titleSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Spacer(Modifier.height(2.dp))
                    BentoStatusLine(downloadState = downloadState)
                }
                BentoActionButton(downloadState = downloadState, onActionPost = onActionPost)
            }
            BentoProgressBar(downloadState = downloadState)
        }
    }
}

@Composable
private fun BentoStatusLine(downloadState: Task.DownloadState) {
    if (downloadState is Running) {
        val progress = downloadState.progress
        val text =
            if (progress >= 0) {
                val percent = "%.1f%%".format(Locale.US, progress * 100)
                val speed =
                    parseSpeedToMbps(downloadState.progressText)?.let {
                        " · " + "%.1f MB/s".format(Locale.US, it)
                    } ?: ""
                percent + speed
            } else {
                stringResource(R.string.status_downloading)
            }
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    } else {
        ListItemStateText(downloadState = downloadState)
    }
}

@Composable
private fun BentoActionButton(
    downloadState: Task.DownloadState,
    onActionPost: (UiAction) -> Unit,
) {
    when (downloadState) {
        is Error -> {
            IconButton(onClick = { onActionPost(UiAction.Resume) }) {
                Icon(
                    imageVector = Icons.Filled.RestartAlt,
                    contentDescription = stringResource(R.string.restart),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
        is Canceled -> {
            IconButton(onClick = { onActionPost(UiAction.Resume) }) {
                Icon(
                    imageVector = Icons.Filled.Download,
                    contentDescription = stringResource(R.string.restart),
                    modifier = Modifier.size(22.dp),
                )
            }
        }
        is Completed -> {
            IconButton(onClick = { onActionPost(UiAction.OpenFile(downloadState.filePath)) }) {
                Icon(
                    imageVector = Icons.Filled.PlayArrow,
                    contentDescription = stringResource(R.string.open_file),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp),
                )
            }
        }
        is FetchingInfo,
        is Idle,
        is ReadyWithInfo,
        is Running -> {
            IconButton(onClick = { onActionPost(UiAction.Cancel) }) {
                Icon(
                    imageVector = Icons.Filled.Pause,
                    contentDescription = stringResource(R.string.cancel),
                    modifier = Modifier.size(22.dp),
                )
            }
        }
    }
}

@Composable
private fun BentoProgressBar(downloadState: Task.DownloadState) {
    val trackColor = MaterialTheme.colorScheme.surfaceContainerHighest
    val barModifier = Modifier.fillMaxWidth().height(2.dp)
    when (downloadState) {
        is Running -> {
            val progress = downloadState.progress
            if (progress >= 0) {
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = barModifier,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = trackColor,
                )
            } else {
                LinearProgressIndicator(
                    modifier = barModifier,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = trackColor,
                )
            }
        }
        is FetchingInfo,
        is Idle,
        is ReadyWithInfo -> {
            LinearProgressIndicator(
                modifier = barModifier,
                color = MaterialTheme.colorScheme.primary,
                trackColor = trackColor,
            )
        }
        is Completed -> {
            val leaf =
                GreenTonalPalettes.accent1(
                    if (LocalDarkTheme.current.isDarkTheme()) 80.0 else 40.0
                )
            LinearProgressIndicator(
                progress = { 1f },
                modifier = barModifier,
                color = leaf,
                trackColor = trackColor,
            )
        }
        is Canceled,
        is Error -> {}
    }
}

private val SpeedRegex = Regex("""([\d.]+)\s*([KMGT]?i?B)/s""")

/** Parse a yt-dlp style speed token (e.g. "4.2 MB/s", "812.5KiB/s") into MB/s. */
fun parseSpeedToMbps(progressText: String): Float? {
    val match = SpeedRegex.find(progressText) ?: return null
    val value = match.groupValues[1].toFloatOrNull() ?: return null
    return when (match.groupValues[2]) {
        "B" -> value / 1048576f
        "KB" -> value / 1000f
        "KiB" -> value / 1024f
        "MB",
        "MiB" -> value
        "GB" -> value * 1000f
        "GiB" -> value * 1024f
        "TB" -> value * 1000000f
        "TiB" -> value * 1048576f
        else -> null
    }
}
