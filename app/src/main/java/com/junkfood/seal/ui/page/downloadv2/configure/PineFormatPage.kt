package com.junkfood.seal.ui.page.downloadv2.configure

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.junkfood.seal.R
import com.junkfood.seal.download.DownloaderV2
import com.junkfood.seal.download.TaskFactory
import com.junkfood.seal.ui.component.FormatRadioButton
import com.junkfood.seal.ui.component.PineFormatOption
import com.junkfood.seal.ui.component.PineGreen
import com.junkfood.seal.ui.component.PineGold
import com.junkfood.seal.ui.component.PineSectionHeader
import com.junkfood.seal.ui.component.PineVideoInfoCard
import com.junkfood.seal.ui.component.PineappleHero3D
import com.junkfood.seal.ui.component.StickyDownloadBar
import com.junkfood.seal.ui.component.buildAudioOptions
import com.junkfood.seal.ui.component.buildVideoOptions
import com.junkfood.seal.ui.component.sizeLabel
import com.junkfood.seal.util.Format
import com.junkfood.seal.util.PreferenceUtil.getBoolean
import com.junkfood.seal.util.PreferenceUtil.getString
import com.junkfood.seal.util.SUBTITLE
import com.junkfood.seal.util.SUBTITLE_LANGUAGE
import com.junkfood.seal.util.VideoClip
import com.junkfood.seal.util.VideoInfo
import com.junkfood.seal.util.toDurationText
import org.koin.compose.koinInject

private const val NO_FORMAT_SELECTED = ""

private val PineScreenPadding = 16.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PineFormatPage(
    modifier: Modifier = Modifier,
    videoInfo: VideoInfo,
    downloader: DownloaderV2 = koinInject(),
    onNavigateBack: () -> Unit = {},
) {
    val formats = videoInfo.formats
    if (formats.isNullOrEmpty()) return

    val duration = videoInfo.duration ?: .0
    val downloadSubtitle = SUBTITLE.getBoolean()
    val subtitleLanguageRegex = SUBTITLE_LANGUAGE.getString()

    val audioOnlyFormats = formats.filter { it.isAudioOnly() }.reversed()
    val videoAudioFormats = formats.filter { it.containsVideo() && it.containsAudio() }.reversed()
    val videoOnlyFormats = formats.filter { it.isVideoOnly() }.reversed()

    val audioOptions = buildAudioOptions(audioOnlyFormats, duration)
    val videoOptions = buildVideoOptions(videoAudioFormats, videoOnlyFormats, duration)

    var selectedAudioId by remember { mutableStateOf(NO_FORMAT_SELECTED) }
    var selectedVideoId by remember { mutableStateOf(NO_FORMAT_SELECTED) }
    var isSubmitting by remember { mutableStateOf(false) }

    val selectedFormats: List<Format> =
        remember(selectedAudioId, selectedVideoId) {
            listOfNotNull(
                audioOptions.firstOrNull { it.id == selectedAudioId }?.format,
                videoOptions.firstOrNull { it.id == selectedVideoId }?.format,
            )
        }

    val canDownload = selectedFormats.isNotEmpty() && !isSubmitting

    val platform =
        videoInfo.extractor?.takeIf { it.isNotBlank() }?.replaceFirstChar { it.uppercase() }
            ?: videoInfo.ext?.uppercase()?.ifBlank { null }
            ?: stringResource(R.string.video)

    val url = videoInfo.webpageUrl ?: videoInfo.originalUrl ?: videoInfo.fulltitle ?: videoInfo.id
    val durationText = videoInfo.duration?.toInt()?.toDurationText().orEmpty()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.format_selection),
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            )
        },
        bottomBar = {
            StickyDownloadBar(
                enabled = canDownload,
                isLoading = isSubmitting,
                onClick = {
                    isSubmitting = true
                    val selectedSubtitles =
                        if (downloadSubtitle) {
                            (videoInfo.subtitles.keys + videoInfo.automaticCaptions.keys)
                                .filterWithRegex(subtitleLanguageRegex)
                                .toList()
                        } else {
                            emptyList()
                        }
                    downloader.enqueue(
                        TaskFactory.createWithConfigurations(
                            videoInfo = videoInfo,
                            formatList = selectedFormats,
                            videoClips = emptyList<VideoClip>(),
                            splitByChapter = false,
                            newTitle = "",
                            selectedSubtitles = selectedSubtitles,
                            selectedAutoCaptions = emptyList(),
                        )
                    )
                    isSubmitting = false
                    onNavigateBack()
                },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(innerPadding),
            contentPadding = PaddingValues(bottom = 16.dp),
        ) {
            item(key = "hero") {
                PineappleHero3D(modifier = Modifier.fillMaxWidth(), height = 240)
            }

            item(key = "infocard") {
                PineVideoInfoCard(
                    modifier = Modifier.padding(horizontal = PineScreenPadding),
                    thumbnailUrl = videoInfo.thumbnail,
                    platform = platform,
                    url = url,
                    title = videoInfo.title,
                    durationText = durationText,
                )
            }

            if (audioOptions.isNotEmpty()) {
                item(key = "audio_header") {
                    Box(Modifier.padding(horizontal = PineScreenPadding)) {
                        PineSectionHeader(text = stringResource(R.string.audio), accent = PineGold)
                    }
                }
                items(audioOptions, key = { "audio_${it.id}" }) { option ->
                    AudioOptionRow(
                        modifier = Modifier.padding(horizontal = PineScreenPadding),
                        option = option,
                        isSelected = option.id == selectedAudioId,
                        onSelect = {
                            selectedAudioId =
                                if (selectedAudioId == option.id) NO_FORMAT_SELECTED else option.id
                        },
                    )
                }
            }

            if (videoOptions.isNotEmpty()) {
                item(key = "video_header") {
                    Box(Modifier.padding(horizontal = PineScreenPadding)) {
                        PineSectionHeader(text = stringResource(R.string.video), accent = PineGreen)
                    }
                }
                items(videoOptions, key = { "video_${it.id}" }) { option ->
                    FormatRadioButton(
                        modifier = Modifier.padding(horizontal = PineScreenPadding),
                        text = option.title,
                        size = option.sizeLabel(),
                        isSelected = option.id == selectedVideoId,
                        accent = PineGreen,
                        showTrailingIcon = true,
                        onSelect = {
                            selectedVideoId =
                                if (selectedVideoId == option.id) NO_FORMAT_SELECTED else option.id
                        },
                    )
                }
            }
        }
    }
}

@Composable
private fun AudioOptionRow(
    modifier: Modifier = Modifier,
    option: PineFormatOption,
    isSelected: Boolean,
    onSelect: () -> Unit,
) {
    FormatRadioButton(
        modifier = modifier,
        text = option.title,
        size = option.sizeLabel(),
        isSelected = isSelected,
        accent = PineGold,
        showTrailingIcon = true,
        onSelect = onSelect,
    )
}
