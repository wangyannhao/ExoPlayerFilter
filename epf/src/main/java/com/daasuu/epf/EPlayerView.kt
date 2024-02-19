package com.daasuu.epf

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.daasuu.epf.chooser.EConfigChooser
import com.daasuu.epf.contextfactory.EContextFactory
import com.daasuu.epf.filter.GlFilter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.video.VideoSize

/**
 * Created by sudamasayuki on 2017/05/16.
 */
class EPlayerView @JvmOverloads constructor(context: Context?, attrs: AttributeSet? = null) :
    GLSurfaceView(context, attrs), Player.Listener {
    private val renderer: EPlayerRenderer
    private var player: ExoPlayer? = null
    private var videoAspect = 1f
    private var playerScaleType = PlayerScaleType.RESIZE_FIT_WIDTH

    init {
        setEGLContextFactory(EContextFactory())
        setEGLConfigChooser(EConfigChooser())
        renderer = EPlayerRenderer(this)
        setRenderer(renderer)
    }

    fun setSimpleExoPlayer(player: ExoPlayer?): EPlayerView {
        if (this.player != null) {
            this.player!!.release()
            this.player = null
        }
        this.player = player
        this.player!!.addListener(this)
        renderer.setSimpleExoPlayer(player)
        return this
    }

    fun setGlFilter(glFilter: GlFilter?) {
        renderer.setGlFilter(glFilter)
    }

    fun setPlayerScaleType(playerScaleType: PlayerScaleType) {
        this.playerScaleType = playerScaleType
        requestLayout()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val measuredWidth = measuredWidth
        val measuredHeight = measuredHeight
        var viewWidth = measuredWidth
        var viewHeight = measuredHeight
        when (playerScaleType) {
            PlayerScaleType.RESIZE_FIT_WIDTH -> viewHeight = (measuredWidth / videoAspect).toInt()
            PlayerScaleType.RESIZE_FIT_HEIGHT -> viewWidth = (measuredHeight * videoAspect).toInt()
            else -> {}
        }

        // Log.d(TAG, "onMeasure viewWidth = " + viewWidth + " viewHeight = " + viewHeight);
        setMeasuredDimension(viewWidth, viewHeight)
    }

    override fun onPause() {
        super.onPause()
        renderer.release()
    }

    //////////////////////////////////////////////////////////////////////////
    // Player.Listener
    override fun onVideoSizeChanged(videoSize: VideoSize) {
        val width = videoSize.width
        val height = videoSize.height
        val pixelWidthHeightRatio = videoSize.pixelWidthHeightRatio
        val unappliedRotationDegrees = videoSize.unappliedRotationDegrees

        // Log.d(TAG, "width = " + width + " height = " + height + " unappliedRotationDegrees = " + unappliedRotationDegrees + " pixelWidthHeightRatio = " + pixelWidthHeightRatio);
        videoAspect = width.toFloat() / height * pixelWidthHeightRatio
        // Log.d(TAG, "videoAspect = " + videoAspect);
        requestLayout()
    }

    override fun onRenderedFirstFrame() {
        // do nothing
    }

    companion object {
        private val TAG = EPlayerView::class.java.simpleName
    }
}
