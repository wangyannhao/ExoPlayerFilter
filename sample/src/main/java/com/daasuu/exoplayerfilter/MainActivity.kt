package com.daasuu.exoplayerfilter

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.Button
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.daasuu.epf.CustomRendererFactory
import com.daasuu.epf.EPlayerView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.PlaybackParameters
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util

class MainActivity : AppCompatActivity() {
    private var ePlayerView: EPlayerView? = null
    private var player: ExoPlayer? = null
    private var button: Button? = null
    private var speedButton: Button? = null
    private var seekBar: SeekBar? = null
    private var playerTimer: PlayerTimer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setUpViews()
    }

    override fun onResume() {
        super.onResume()
        setUpSimpleExoPlayer()
        setUoGlPlayerView()
        setUpTimer()
    }

    override fun onPause() {
        super.onPause()
        releasePlayer()
        if (playerTimer != null) {
            playerTimer!!.stop()
            playerTimer!!.removeMessages(0)
        }
    }

    private fun setUpViews() {
        // play pause
        button = findViewById<View>(R.id.btn) as Button
        button!!.setOnClickListener(View.OnClickListener {
            if (player == null) return@OnClickListener
            if (button!!.text.toString() == this@MainActivity.getString(R.string.pause)) {
                player!!.playWhenReady = false
                button!!.setText(R.string.play)
            } else {
                player!!.playWhenReady = true
                button!!.setText(R.string.pause)
            }
        })

        // Adjust speed
        speedButton = findViewById<View>(R.id.speed) as Button
        button!!.setOnClickListener(View.OnClickListener {
            if (player == null) return@OnClickListener
            if (speedButton!!.text.toString() == "1X") {
                player!!.playbackParameters = PlaybackParameters(1.5f)
                speedButton!!.text = "1.5X"
            } else if (speedButton!!.text.toString() == "1.5X") {
                player!!.playbackParameters = PlaybackParameters(2f)
                speedButton!!.text = "2X"
            } else if (speedButton!!.text.toString() == "2X") {
                player!!.playbackParameters = PlaybackParameters(4f)
                speedButton!!.text = "4X"
            } else if (speedButton!!.text.toString() == "4X") {
                player!!.playbackParameters = PlaybackParameters(1f)
                speedButton!!.text = "1X"
            }
        })


        // seek
        seekBar = findViewById<View>(R.id.seekBar) as SeekBar
        seekBar!!.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                if (player == null) return
                if (!fromUser) {
                    // We're not interested in programmatically generated changes to
                    // the progress bar's position.
                    return
                }
                player!!.seekTo((progress * 1000).toLong())
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                // do nothing
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                // do nothing
            }
        })

        // list
        val listView = findViewById<View>(R.id.list) as ListView
        val filterTypes = FilterType.createFilterList()
        listView.adapter = FilterAdapter(this, R.layout.row_text, filterTypes)
        listView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            ePlayerView!!.setGlFilter(
                FilterType.createGlFilter(
                    filterTypes[position], applicationContext
                )
            )
        }
    }

    private fun setUpSimpleExoPlayer() {


        // Produces DataSource instances through which media data is loaded.
        val dataSourceFactory: DataSource.Factory =
            DefaultDataSourceFactory(this, Util.getUserAgent(this, "yourApplicationName"))

        // SimpleExoPlayer
        player = ExoPlayer.Builder(this)
            .setMediaSourceFactory(ProgressiveMediaSource.Factory(dataSourceFactory))
            .setRenderersFactory(CustomRendererFactory(this))
            .build()
        player!!.addMediaItem(MediaItem.fromUri(Constant.STREAM_URL_MP4_VOD_SHORT))
        player!!.prepare()
        player!!.playWhenReady = true
    }

    private fun setUoGlPlayerView() {
        ePlayerView = EPlayerView(this)
        ePlayerView!!.setSimpleExoPlayer(player)
        ePlayerView!!.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        (findViewById<View>(R.id.layout_movie_wrapper) as MovieWrapperView).addView(ePlayerView)
        ePlayerView!!.onResume()
    }

    private fun setUpTimer() {
        playerTimer = PlayerTimer()
        playerTimer?.setCallback(object : PlayerTimer.Callback {
            override fun onTick(timeMillis: Long) {
                val position = player?.currentPosition ?: return
                val duration = player?.duration ?: return
                if (duration <= 0) return

                seekBar?.apply {
                    max = duration.toInt() / 1000
                    progress = position.toInt() / 1000
                }
            }
        })
        playerTimer!!.start()
    }

    private fun releasePlayer() {
        ePlayerView!!.onPause()
        (findViewById<View>(R.id.layout_movie_wrapper) as MovieWrapperView).removeAllViews()
        ePlayerView = null
        player!!.stop()
        player!!.release()
        player = null
    }
}