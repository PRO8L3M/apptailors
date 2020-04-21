package com.apptailors.streams.ui.streamList

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.apptailors.streams.R
import com.apptailors.streams.ui.stream.StreamActivity
import kotlinx.android.synthetic.main.activity_stream_list.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class StreamListActivity : AppCompatActivity() {

    private val viewModel: StreamListViewModel by viewModel()
    private val streamAdapter = StreamAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stream_list)

        setUpRecyclerView()
        handleRecyclerClickEvents()
    }

    private fun handleRecyclerClickEvents() {
        streamAdapter.onStreamClick = { _ ->
            startActivity(Intent(this, StreamActivity::class.java))
        }
    }

    private fun setUpRecyclerView() {
        val streamList = viewModel.getStreamList().also { Timber.i("aa $it") }
        streamAdapter.insertNewList(streamList)
        stream_list_recycler.adapter = streamAdapter
    }
}
