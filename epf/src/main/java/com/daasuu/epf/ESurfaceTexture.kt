package com.daasuu.epf

import android.graphics.SurfaceTexture
import android.graphics.SurfaceTexture.OnFrameAvailableListener
import com.daasuu.epf.filter.GlPreviewFilter

/**
 * Created by sudamasayuki on 2017/05/16.
 */
internal class ESurfaceTexture(texName: Int) : OnFrameAvailableListener {
    @JvmField
    val surfaceTexture: SurfaceTexture
    private var onFrameAvailableListener: OnFrameAvailableListener? = null

    init {
        surfaceTexture = SurfaceTexture(texName)
        surfaceTexture.setOnFrameAvailableListener(this)
    }

    fun setOnFrameAvailableListener(l: OnFrameAvailableListener?) {
        onFrameAvailableListener = l
    }

    val textureTarget: Int
        get() = GlPreviewFilter.GL_TEXTURE_EXTERNAL_OES

    fun updateTexImage() {
        surfaceTexture.updateTexImage()
    }

    fun getTransformMatrix(mtx: FloatArray?) {
        surfaceTexture.getTransformMatrix(mtx)
    }

    override fun onFrameAvailable(surfaceTexture: SurfaceTexture) {
        if (onFrameAvailableListener != null) {
            onFrameAvailableListener!!.onFrameAvailable(this.surfaceTexture)
        }
    }

    fun release() {
        surfaceTexture.release()
    }
}
