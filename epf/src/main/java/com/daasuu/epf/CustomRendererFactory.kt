package com.daasuu.epf

import android.content.Context
import android.os.Handler
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.Renderer
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector
import com.google.android.exoplayer2.video.VideoRendererEventListener

class CustomRendererFactory(context: Context?) : DefaultRenderersFactory(context!!) {
    override fun buildVideoRenderers(
        context: Context,
        extensionRendererMode: @ExtensionRendererMode Int,
        mediaCodecSelector: MediaCodecSelector,
        enableDecoderFallback: Boolean,
        eventHandler: Handler,
        eventListener: VideoRendererEventListener,
        allowedVideoJoiningTimeMs: Long,
        out: ArrayList<Renderer>
    ) {
        // Add custom video renderer
        out.add(CustomVideoRenderer(context, mediaCodecSelector))

        // You can also add default or other custom video renderers if needed
    }
}
