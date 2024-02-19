package com.daasuu.epf

import java.io.Serializable

/**
 * Created by sudamasayuki on 2017/05/16.
 */
class Resolution(private val width: Int, private val height: Int) : Serializable {
    fun width(): Int {
        return width
    }

    fun height(): Int {
        return height
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as Resolution
        return if (height != that.height) false else width == that.width
    }

    override fun hashCode(): Int {
        var result = width
        result = 31 * result + height
        return result
    }
}
