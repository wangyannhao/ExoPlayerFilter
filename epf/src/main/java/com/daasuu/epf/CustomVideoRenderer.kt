package com.daasuu.epf

import android.content.Context
import android.util.Log
import androidx.annotation.RequiresApi
import com.google.android.exoplayer2.mediacodec.MediaCodecAdapter
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer

class CustomVideoRenderer(context: Context?, mediaCodecSelector: MediaCodecSelector?) :
    MediaCodecVideoRenderer(
        context!!, mediaCodecSelector!!
    ) {
    @RequiresApi(21)
    override fun renderOutputBufferV21(
        codec: MediaCodecAdapter,
        index: Int,
        presentationTimeUs: Long,
        releaseTimeNs: Long
    ) {
        Log.w(
            TAG,
            "index = $index, presentationTimeUs = $presentationTimeUs, releaseTimeNs = $releaseTimeNs"
        )
        super.renderOutputBufferV21(codec, index, presentationTimeUs, releaseTimeNs)
    }

    companion object {
        private val TAG = CustomVideoRenderer::class.java.simpleName
    }
}
