package com.daasuu.exoplayerfilter

import android.os.Handler
import android.os.Message

/**
 * Timer for Seekbar
 * Created by sudamasayuki on 2017/05/18.
 */
class PlayerTimer : Handler() {
    private var isUpdate = false
    private var callback: Callback? = null
    private var startTimeMillis: Long = 0
    private fun init() {
        startTimeMillis = System.currentTimeMillis()
    }

    fun start() {
        init()
        isUpdate = true
        handleMessage(Message())
    }

    fun stop() {
        isUpdate = false
    }

    fun setCallback(callback: Callback?) {
        this.callback = callback
    }

    override fun handleMessage(msg: Message) {
        this.removeMessages(0)
        if (isUpdate) {
            sendMessageDelayed(obtainMessage(0), DEFAULT_INTERVAL_MILLIS.toLong())
            callback!!.onTick(System.currentTimeMillis() - startTimeMillis)
        }
    }

    interface Callback {
        fun onTick(timeMillis: Long)
    }

    companion object {
        private const val DEFAULT_INTERVAL_MILLIS = 1000
    }
}
