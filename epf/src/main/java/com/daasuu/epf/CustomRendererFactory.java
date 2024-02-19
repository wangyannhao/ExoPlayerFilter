package com.daasuu.epf;

import android.content.Context;
import android.os.Handler;

import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.Renderer;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.video.VideoRendererEventListener;

import java.util.ArrayList;

public class CustomRendererFactory extends DefaultRenderersFactory {

    public CustomRendererFactory(Context context) {
        super(context);
    }

    @Override
    protected void buildVideoRenderers(
            Context context,
            @ExtensionRendererMode int extensionRendererMode,
            MediaCodecSelector mediaCodecSelector,
            boolean enableDecoderFallback,
            Handler eventHandler,
            VideoRendererEventListener eventListener,
            long allowedVideoJoiningTimeMs,
            ArrayList<Renderer> out) {
        // Add custom video renderer
        out.add(new CustomVideoRenderer(context, mediaCodecSelector));

        // You can also add default or other custom video renderers if needed
    }
}
