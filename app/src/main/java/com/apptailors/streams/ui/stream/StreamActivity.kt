package com.apptailors.streams.ui.stream

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import coil.api.load
import com.apptailors.streams.BuildConfig
import com.apptailors.streams.R
import com.apptailors.streams.common.DELAY
import com.apptailors.streams.common.EVERY_SECOND
import com.apptailors.streams.common.FREEZE_PROGRESS
import com.apptailors.streams.common.REPEAT
import com.apptailors.streams.data.entity.BuffResult
import com.apptailors.streams.data.network.ResultObserver
import com.apptailors.streams.ext.snackBar
import com.apptailors.streams.ext.startCoroutineTimer
import com.apptailors.streams.util.MyCountDownTimer
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.android.synthetic.main.activity_stream.*
import kotlinx.android.synthetic.main.author_label.*
import kotlinx.android.synthetic.main.question_label.*
import kotlinx.coroutines.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.math.roundToInt

class StreamActivity : AppCompatActivity() {

    private val coroutineScope = CoroutineScope(Dispatchers.Main + Job())

    private val viewModel: StreamViewModel by viewModel()
    private lateinit var exoplayer: ExoPlayer
    private val buffAdapter = BuffAdapter()
    private val myCountDownTimer = MyCountDownTimer(REPEAT / 2, EVERY_SECOND)

    @SuppressLint("SourceLockedOrientationActivity")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream)

        /**
         * I assumed according to UI mocks that only screen orientation for exoplayer should be landscape
         * that's why I forced landscape mode only. This is be the thing to improve for portrait mode.
         * */
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        viewModel.question.observe(this, ResultObserver(::onSuccess, ::onFailure))

        handleBuffClickEvents()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        buff_recycler.adapter = buffAdapter
    }

    override fun onResume() {
        super.onResume()
        handleBuffNetworkRequests()
    }

    private fun startBuffNetworkRequests() {
        coroutineScope.startCoroutineTimer(repeatMillis = REPEAT, delayMillis = DELAY) {
            val questionId = (1..6).random()
            viewModel.getQuestion(questionId)
        }
    }

    private fun handleBuffNetworkRequests() {
        exoplayer.addListener(object : Player.EventListener {
            override fun onIsPlayingChanged(isPlaying: Boolean) {
                super.onIsPlayingChanged(isPlaying)
                if (!isPlaying) coroutineScope.coroutineContext.cancelChildren()
                else startBuffNetworkRequests()
            }
        })
    }

    private fun handleBuffClickEvents() {
        fun clearProgressbar() {
            hideQuestion()
            question_label_count_down_timer.progress = 0
        }
        buffAdapter.onAnswerClick = { _ ->
            coroutineScope.launch {
                try {
                    myCountDownTimer.cancel()
                    delay(FREEZE_PROGRESS)
                    clearProgressbar()
                } catch (e: Exception) {
                    clearProgressbar()
                }
            }
        }

        buff_button_close.setOnClickListener {
            hideQuestion()
        }
    }

    private fun showQuestion() = with(streams_buff) {
        visibility = View.VISIBLE
    }


    private fun hideQuestion() = with(streams_buff) {
       visibility = View.GONE
    }

    private fun handleBuffAppearances() {
        myCountDownTimer.onFinishLambda = {
            question_label_count_down_timer.progress = 0
            hideQuestion()
        }

        myCountDownTimer.onTickLambda = { millisUntilFinished ->
            val remainingSeconds = (millisUntilFinished / 1000).toString()
            val progressEverySecond = 100 / ((REPEAT / 2) / 1000).toFloat()
            question_label_seconds.text = remainingSeconds
            question_label_count_down_timer.progress += progressEverySecond.roundToInt()
        }

        myCountDownTimer.start()
        showQuestion()
    }

    private fun onSuccess(result: BuffResult) {
        setUpBuff(result)
        handleBuffAppearances()
    }

    private fun onFailure(exception: Exception) =
        stream_layout.snackBar(exception.localizedMessage ?: "Error occured")

    private fun setUpBuff(result: BuffResult) = with(result) {
        author_label_author_image.load(result.author.image)
        author_label_author_name.text = "${result.author.first_name} ${result.author.last_name}"
        question_label_question.text = result.question.title
        buffAdapter.author = result.author
        buffAdapter.insertNewList(result.answers)
    }

    private fun initPlayer() {
        exoplayer = SimpleExoPlayer.Builder(this).build()

        val dataSourceFactory =
            DefaultDataSourceFactory(this, Util.getUserAgent(this, BuildConfig.APPLICATION_ID))

        val videoSource = ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(BuildConfig.STREAM_URI))

        with(exoplayer) {
            prepare(videoSource, false, false)
            playWhenReady = true
        }
        streams_exoplayer.player = exoplayer
    }

    private fun releasePlayer() {
        exoplayer.release()
    }

    override fun onStart() {
        super.onStart()

        initPlayer()
        if (streams_exoplayer != null) streams_exoplayer.onResume()
    }

    override fun onStop() {
        super.onStop()

        if (streams_exoplayer != null) streams_exoplayer.onPause()
        releasePlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }
}
