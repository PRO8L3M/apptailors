package com.apptailors.streams.ui.streamList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.apptailors.streams.R
import com.apptailors.streams.data.entity.StreamOverview
import com.apptailors.streams.ext.getAsyncListDiffer
import kotlinx.android.synthetic.main.stream_view.view.*

class StreamAdapter : RecyclerView.Adapter<StreamViewHolder>() {

    var onStreamClick: ((StreamOverview) -> Unit)? = null

    private val differ = getAsyncListDiffer<StreamOverview> { oldItem, newItem -> oldItem.id == newItem.id }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StreamViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.stream_view, parent, false)
        return StreamViewHolder(view)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: StreamViewHolder, position: Int) = holder.bind(differ.currentList[position], onStreamClick)

    fun insertNewList(newList: List<StreamOverview>) = differ.submitList(newList)
}

class StreamViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {

    fun bind(stream: StreamOverview, onAnswerClick: ((StreamOverview) -> Unit)?) = with(view) {
        when(stream.state) {
            "live now" -> {
                stream_view_state.setBackgroundColor(ContextCompat.getColor(context, R.color.stream_view_state_live))
                stream_view_state.text = stream.state
                stream_view_action_icon.setBackgroundResource(R.drawable.ic_play_ico)
                stream_view_action_icon.setOnClickListener { onAnswerClick?.invoke(stream) }
            }
            "tomorrow" -> {
                stream_view_state.setBackgroundColor(ContextCompat.getColor(context, R.color.stream_view_state_tomorrow))
                stream_view_state.text = stream.state
                stream_view_action_icon.setBackgroundResource(R.drawable.ic_schedule_ico)
            }
            else -> {
                stream_view_state.setBackgroundColor(ContextCompat.getColor(context, R.color.stream_view_state_finished))
                stream_view_state.text = stream.state
                stream_view_action_icon.visibility = View.GONE
            }
        }
        stream_view_station.load(stream.station)
        stream_view_image.load(stream.imageRes)
        stream_view_title.text = stream.title
        stream_view_description.text = stream.description
    }
}