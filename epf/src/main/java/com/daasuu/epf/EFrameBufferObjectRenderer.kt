package com.daasuu.epf

import android.opengl.GLES20
import android.opengl.GLSurfaceView
import com.daasuu.epf.filter.GlFilter
import java.util.LinkedList
import java.util.Queue
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

/**
 * Created by sudamasayuki on 2017/05/16.
 */
internal abstract class EFrameBufferObjectRenderer : GLSurfaceView.Renderer {
    private var framebufferObject: EFramebufferObject? = null
    private var normalShader: GlFilter? = null
    private val runOnDraw: Queue<Runnable>

    init {
        runOnDraw = LinkedList()
    }

    override fun onSurfaceCreated(gl: GL10, config: EGLConfig) {
        framebufferObject = EFramebufferObject()
        normalShader = GlFilter()
        normalShader!!.setup()
        onSurfaceCreated(config)
    }

    override fun onSurfaceChanged(gl: GL10, width: Int, height: Int) {
        framebufferObject!!.setup(width, height)
        normalShader!!.setFrameSize(width, height)
        onSurfaceChanged(width, height)
    }

    override fun onDrawFrame(gl: GL10) {
        synchronized(runOnDraw) {
            while (!runOnDraw.isEmpty()) {
                runOnDraw.poll().run()
            }
        }
        framebufferObject!!.enable()
        GLES20.glViewport(0, 0, framebufferObject!!.width, framebufferObject!!.height)
        onDrawFrame(framebufferObject)
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0)
        GLES20.glViewport(0, 0, framebufferObject!!.width, framebufferObject!!.height)
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT or GLES20.GL_DEPTH_BUFFER_BIT)
        normalShader!!.draw(framebufferObject!!.texName, null)
    }

    @Throws(Throwable::class)
    protected fun finalize() {
    }

    abstract fun onSurfaceCreated(config: EGLConfig?)
    abstract fun onSurfaceChanged(width: Int, height: Int)
    abstract fun onDrawFrame(fbo: EFramebufferObject?)
}
