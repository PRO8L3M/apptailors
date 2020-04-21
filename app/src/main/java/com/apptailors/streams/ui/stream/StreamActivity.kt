package com.apptailors.streams.ui.stream

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import coil.api.load
import com.apptailors.streams.R
import com.apptailors.streams.data.entity.BuffResult
import com.apptailors.streams.data.network.ResultObserver
import com.apptailors.streams.ext.startCoroutineTimer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_stream.*
import kotlinx.android.synthetic.main.author_label.*
import kotlinx.android.synthetic.main.question_label.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

const val uri = "https://buffup-public.s3.eu-west-2.amazonaws.com/video/toronto+nba+cut+3.mp4"

class StreamActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private val REPEAT = 30000L
    private val DELAY = 5000L
    private val viewModel: StreamViewModel by viewModel()

    private lateinit var exoplayer: ExoPlayer
    private lateinit var  dataSourceFactory: DataSource.Factory
    private val buffAdapter = BuffAdapter()

    private var currentWindow = 0
    private var playbackPosition: Long = 0

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        dataSourceFactory = DefaultDataSourceFactory(this, Util.getUserAgent(this, "apptailors"))
        viewModel.question.observe(this, ResultObserver(::onSuccess, ::onFailure))

        startCoroutineTimer(repeatMillis = REPEAT, delayMillis = DELAY) {
            val questionId = (1..6).random()
            viewModel.getQuestion(questionId)
        }

        buff_button_close.setOnClickListener {
            hideQuestion()
        }
        buff_recycler.adapter = buffAdapter
    }

    private fun showQuestion() {
        streams_buff.visibility = View.VISIBLE
    }

    private fun hideQuestion() {
        streams_buff.visibility = View.INVISIBLE
    }

    private fun handleBuffAppearing() {
        val countDownTimer = object : CountDownTimer(REPEAT/2 , 1000L) {
            override fun onFinish() {
                question_label_count_down_timer.progress = 0
                hideQuestion()
            }

            override fun onTick(millisUntilFinished: Long) {
                question_label_seconds.text = (millisUntilFinished/1000).toString()
                question_label_count_down_timer.progress += 7
            }
        }
        countDownTimer.start()
        showQuestion()
    }

    private fun onSuccess(result: BuffResult) {
        setUpBuff(result)
        handleBuffAppearing()
    }

    private fun setUpBuff(result: BuffResult) = with(result) {
        author_label_author_image.load(result.author.image)
        author_label_author_name.text = "${result.author.first_name} ${result.author.last_name}"
        question_label_question.text = result.question.title
        buffAdapter.author = result.author
        buffAdapter.insertNewList(result.answers)
        question_label_count_down_timer.show()

    }

    private fun onFailure(exception: Exception) = Timber.d("Error: ${exception.localizedMessage}")

    private fun initExoplayer() {
        exoplayer = SimpleExoPlayer.Builder(this).build()

        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(uri))

        with(exoplayer) {
            playWhenReady = true
            seekTo(currentWindow, playbackPosition)
            prepare(videoSource, false, false)
        }
        streams_exoplayer.player = exoplayer
    }

    private fun releasePlayer() {
        exoplayer.release()
    }

    override fun onStart() {
        super.onStart()

        initExoplayer()
        if (streams_exoplayer != null) streams_exoplayer.onResume()
    }

    override fun onStop() {
        super.onStop()

        if (streams_exoplayer != null) streams_exoplayer.onPause()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
}
