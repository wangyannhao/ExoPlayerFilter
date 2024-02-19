package com.daasuu.epf;

import android.content.Context;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.android.exoplayer2.mediacodec.MediaCodecAdapter;
import com.google.android.exoplayer2.mediacodec.MediaCodecSelector;
import com.google.android.exoplayer2.video.MediaCodecVideoRenderer;

public class CustomVideoRenderer extends MediaCodecVideoRenderer {
    private static String TAG = CustomVideoRenderer.class.getSimpleName();

    public CustomVideoRenderer(Context context, MediaCodecSelector mediaCodecSelector) {
        super(context, mediaCodecSelector);
    }

    @Override
    @RequiresApi(21)
    protected void renderOutputBufferV21(MediaCodecAdapter codec, int index, long presentationTimeUs, long releaseTimeNs) {
        Log.w(TAG, "index = " + index + ", presentationTimeUs = " + presentationTimeUs + ", releaseTimeNs = " + releaseTimeNs);
        super.renderOutputBufferV21(codec, index, presentationTimeUs, releaseTimeNs);
    }
}
