package com.daasuu.epf

import android.graphics.SurfaceTexture
import android.graphics.SurfaceTexture.OnFrameAvailableListener
import android.opengl.GLES20
import android.opengl.Matrix
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Surface
import com.daasuu.epf.filter.GlFilter
import com.daasuu.epf.filter.GlLookUpTableFilter
import com.daasuu.epf.filter.GlPreviewFilter
import com.google.android.exoplayer2.ExoPlayer
import javax.microedition.khronos.egl.EGLConfig

/**
 * Created by sudamasayuki on 2017/05/16.
 */
internal class EPlayerRenderer(glPreview: EPlayerView) : EFrameBufferObjectRenderer(),
    OnFrameAvailableListener {
    private var previewTexture: ESurfaceTexture? = null
    private var updateSurface = false
    private var texName = 0
    private val MVPMatrix = FloatArray(16)
    private val ProjMatrix = FloatArray(16)
    private val MMatrix = FloatArray(16)
    private val VMatrix = FloatArray(16)
    private val STMatrix = FloatArray(16)
    private var filterFramebufferObject: EFramebufferObject? = null
    private var previewFilter: GlPreviewFilter? = null
    private var glFilter: GlFilter? = null
    private var isNewFilter = false
    private val glPreview: EPlayerView
    private var aspectRatio = 1f
    private var simpleExoPlayer: ExoPlayer? = null

    init {
        Matrix.setIdentityM(STMatrix, 0)
        this.glPreview = glPreview
    }

    fun setGlFilter(filter: GlFilter?) {
        glPreview.queueEvent {
            if (glFilter != null) {
                glFilter!!.release()
                if (glFilter is GlLookUpTableFilter) {
                    (glFilter as GlLookUpTableFilter).releaseLutBitmap()
                }
                glFilter = null
            }
            glFilter = filter
            isNewFilter = true
            glPreview.requestRender()
        }
    }

    override fun onSurfaceCreated(config: EGLConfig?) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f)
        val args = IntArray(1)
        GLES20.glGenTextures(args.size, args, 0)
        texName = args[0]
        previewTexture = ESurfaceTexture(texName)
        previewTexture!!.setOnFrameAvailableListener(this)
        GLES20.glBindTexture(previewTexture!!.textureTarget, texName)
        // GL_TEXTURE_EXTERNAL_OES
        EglUtil.setupSampler(previewTexture!!.textureTarget, GLES20.GL_LINEAR, GLES20.GL_NEAREST)
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, 0)
        filterFramebufferObject = EFramebufferObject()
        // GL_TEXTURE_EXTERNAL_OES
        previewFilter = GlPreviewFilter(previewTexture!!.textureTarget)
        previewFilter!!.setup()
        Handler(Looper.getMainLooper()).post {
            val surface = Surface(previewTexture!!.surfaceTexture)
            simpleExoPlayer!!.setVideoSurface(surface)
        }
        Matrix.setLookAtM(
            VMatrix, 0,
            0.0f, 0.0f, 5.0f,
            0.0f, 0.0f, 0.0f,
            0.0f, 1.0f, 0.0f
        )
        synchronized(this) { updateSurface = false }
        if (glFilter != null) {
            isNewFilter = true
        }
        GLES20.glGetIntegerv(GLES20.GL_MAX_TEXTURE_SIZE, args, 0)
    }

    override fun onSurfaceChanged(width: Int, height: Int) {
        Log.d(TAG, "onSurfaceChanged width = $width  height = $height")
        filterFramebufferObject!!.setup(width, height)
        previewFilter!!.setFrameSize(width, height)
        if (glFilter != null) {
            glFilter!!.setFrameSize(width, height)
        }
        aspectRatio = width.toFloat() / height
        Matrix.frustumM(ProjMatrix, 0, -aspectRatio, aspectRatio, -1f, 1f, 5f, 7f)
        Matrix.setIdentityM(MMatrix, 0)
    }

    override fun onDrawFrame(fbo: EFramebufferObject?) {
        synchronized(this) {
            if (updateSurface) {
                previewTexture!!.updateTexImage()
                previewTexture!!.getTransformMatrix(STMatrix)
                updateSurface = false
            }
        }
        if (isNewFilter) {
            if (glFilter != null) {
                glFilter!!.setup()
                glFilter!!.setFrameSize(fbo!!.width, fbo!!.height)
            }
            isNewFilter = false
        }
        if (glFilter != null) {
            filterFramebufferObject!!.enable()
            GLES20.glViewport(
                0,
                0,
                filterFramebufferObject!!.width,
                filterFramebufferObject!!.height
            )
        }
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
        Matrix.multiplyMM(MVPMatrix, 0, VMatrix, 0, MMatrix, 0)
        Matrix.multiplyMM(MVPMatrix, 0, ProjMatrix, 0, MVPMatrix, 0)
        previewFilter!!.draw(texName, MVPMatrix, STMatrix, aspectRatio)
        if (glFilter != null) {
            fbo!!.enable()
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT)
            glFilter!!.draw(filterFramebufferObject!!.texName, fbo)
        }
    }

    @Synchronized
    override fun onFrameAvailable(previewTexture: SurfaceTexture) {
        updateSurface = true
        glPreview.requestRender()
    }

    fun setSimpleExoPlayer(simpleExoPlayer: ExoPlayer?) {
        this.simpleExoPlayer = simpleExoPlayer
    }

    fun release() {
        if (glFilter != null) {
            glFilter!!.release()
        }
        if (previewTexture != null) {
            previewTexture!!.release()
        }
    }

    companion object {
        private val TAG = EPlayerRenderer::class.java.simpleName
    }
}
