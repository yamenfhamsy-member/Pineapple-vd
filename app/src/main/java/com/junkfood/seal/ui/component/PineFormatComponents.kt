package com.junkfood.seal.ui.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.rounded.Audiotrack
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.junkfood.seal.R
import com.junkfood.seal.ui.common.AsyncImageImpl
import com.junkfood.seal.util.Format
import com.junkfood.seal.util.toFileSizeText

val PineGold = Color(0xFFFFD700)
val PineGreen = Color(0xFF32CD32)
val PineBase = Color(0xFF121212)
val PineCard = Color(0xFF1E1E1E)
val PineTextMuted = Color(0xFFB0B0B0)

private val PineCardShape = RoundedCornerShape(14.dp)
private val PineButtonShape = RoundedCornerShape(16.dp)

data class PineFormatOption(val id: String, val title: String, val sizeBytes: Double?, val format: Format)

@androidx.compose.runtime.Composable
fun PineFormatOption.sizeLabel(): String = sizeBytes.toFileSizeText()

@androidx.compose.runtime.Composable
fun buildAudioOptions(audioOnlyFormats: List<Format>, duration: Double): List<PineFormatOption> {
    val mp3Options =
        audioOnlyFormats.filter { it.ext?.contains("mp3", ignoreCase = true) == true }
    val restOptions = audioOnlyFormats.filter { it !in mp3Options }
    val ordered = mp3Options.take(1) + restOptions

    return ordered.mapIndexed { index, format ->
        val size = format.fileSize ?: format.fileSizeApprox ?: (format.abr?.times(duration * 125))
        PineFormatOption(
            id = format.formatId ?: "audio-$index",
            title = audioTitle(format.ext, index),
            sizeBytes = size,
            format = format,
        )
    }
}

@androidx.compose.runtime.Composable
fun buildVideoOptions(
    videoAudioFormats: List<Format>,
    videoOnlyFormats: List<Format>,
    duration: Double,
): List<PineFormatOption> {
    val combined = (videoAudioFormats + videoOnlyFormats).sortedByDescending { it.height ?: .0 }
    return combined.mapIndexed { index, format ->
        val size = format.fileSize ?: format.fileSizeApprox ?: format.tbr?.times(duration * 125)
        PineFormatOption(
            id = format.formatId ?: "video-$index",
            title = videoTitle(format.height, index),
            sizeBytes = size,
            format = format,
        )
    }
}

private fun audioTitle(ext: String?, index: Int): String =
    when {
        ext?.contains("mp3", ignoreCase = true) == true -> "Classic MP3"
        index == 0 -> "Quick"
        else -> "Audio ${index + 1}"
    }

private fun videoTitle(height: Double?, index: Int): String {
    val h = height?.toInt()
    return when {
        h != null && h <= 480 -> "Quick (${h}p)"
        h != null && h <= 720 -> "High quality (${h}p)"
        h != null -> "Quality (${h}p)"
        else -> "Video ${index + 1}"
    }
}

@Composable
fun FormatRadioButton(
    modifier: Modifier = Modifier,
    text: String,
    size: String,
    isSelected: Boolean,
    accent: Color = PineGold,
    showTrailingIcon: Boolean = false,
    onSelect: () -> Unit,
) {
    val borderColor by
        animateColorAsState(
            if (isSelected) accent else Color(0xFF2E2E2E),
            animationSpec = tween(140),
            label = "",
        )
    val containerColor by
        animateColorAsState(
            if (isSelected) accent.copy(alpha = 0.12f) else PineCard,
            animationSpec = tween(140),
            label = "",
        )

    Row(
        modifier =
            modifier.fillMaxWidth()
                .padding(vertical = 4.dp)
                .clip(PineCardShape)
                .background(containerColor)
                .border(1.dp, borderColor, PineCardShape)
                .clickable { onSelect() }
                .padding(horizontal = 12.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(modifier = Modifier.weight(1f), verticalAlignment = Alignment.CenterVertically) {
            PineRadioDot(selected = isSelected, accent = accent)
            Spacer(Modifier.width(10.dp))
            Text(
                text = text,
                color = Color.White,
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.weight(1f),
            )
            if (showTrailingIcon) {
                Spacer(Modifier.width(6.dp))
                Icon(
                    imageVector =
                        if (accent == PineGreen) Icons.Rounded.PlayArrow else Icons.Rounded.Audiotrack,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(18.dp),
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        Text(text = size, color = PineTextMuted, fontSize = 12.sp)
    }
}

@Composable
private fun PineRadioDot(selected: Boolean, accent: Color) {
    val fill by animateColorAsState(if (selected) accent else Color.Transparent, tween(140), label = "")
    val ring by animateColorAsState(if (selected) accent else Color(0xFF4A4A4A), tween(140), label = "")
    Box(
        modifier = Modifier.size(18.dp).clip(CircleShape).background(fill).border(2.dp, ring, CircleShape),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier = Modifier.size(6.dp).clip(CircleShape).background(PineBase),
                contentAlignment = Alignment.Center,
            )
        }
    }
}

@Composable
fun PineVideoInfoCard(
    modifier: Modifier = Modifier,
    thumbnailUrl: String?,
    platform: String,
    url: String,
    title: String,
    durationText: String,
) {
    Surface(modifier = modifier.fillMaxWidth(), shape = PineCardShape, color = PineCard) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier =
                    Modifier.size(width = 132.dp, height = 78.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF242424))
            ) {
                if (thumbnailUrl != null) {
                    AsyncImageImpl(
                        model = thumbnailUrl,
                        contentDescription = stringResource(R.string.thumbnail),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                    )
                }
                if (durationText.isNotBlank()) {
                    Surface(
                        modifier = Modifier.align(Alignment.BottomEnd).padding(3.dp),
                        color = Color.Black.copy(alpha = 0.7f),
                        shape = RoundedCornerShape(4.dp),
                    ) {
                        Text(
                            text = durationText,
                            modifier = Modifier.padding(horizontal = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                        )
                    }
                }
            }
            Spacer(Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = platform,
                    style = MaterialTheme.typography.labelMedium,
                    color = PineGold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = url,
                    style = MaterialTheme.typography.bodySmall,
                    color = PineTextMuted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    color = Color.White,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}

@Composable
fun PineSectionHeader(text: String, accent: Color = PineGold) {
    Row(modifier = Modifier.padding(top = 16.dp, bottom = 6.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector =
                if (accent == PineGreen) Icons.Rounded.PlayArrow else Icons.Rounded.Audiotrack,
            contentDescription = null,
            tint = accent,
            modifier = Modifier.size(18.dp),
        )
        Spacer(Modifier.width(6.dp))
        Text(text = text, style = MaterialTheme.typography.titleSmall, color = accent)
    }
}

@Composable
fun StickyDownloadBar(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit,
) {
    Surface(modifier = modifier.fillMaxWidth(), color = PineBase) {
        Box(modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)) {
            Button(
                onClick = onClick,
                enabled = enabled && !isLoading,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = PineButtonShape,
                colors =
                    ButtonDefaults.buttonColors(
                        containerColor = PineGreen,
                        contentColor = Color.White,
                        disabledContainerColor = Color(0xFF2A2A2A),
                        disabledContentColor = Color(0xFF6A6A6A),
                    ),
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = Color.White,
                        strokeWidth = 2.dp,
                    )
                    Spacer(Modifier.width(10.dp))
                } else {
                    Icon(
                        imageVector = Icons.Filled.Download,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                    )
                    Spacer(Modifier.width(10.dp))
                }
                Text(
                    text = stringResource(R.string.download),
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}
