package com.apptailors.streams.ui.stream

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.apptailors.streams.R
import com.apptailors.streams.data.entity.Answer
import com.apptailors.streams.data.entity.Author
import com.apptailors.streams.ext.getAsyncListDiffer
import kotlinx.android.synthetic.main.answer_view.view.*

class BuffAdapter : RecyclerView.Adapter<BuffViewHolder>() {

    var onAnswerClick: ((Answer) -> Unit)? = null
    var author: Author? = null

    private val differ = getAsyncListDiffer<Answer> { oldItem, newItem -> oldItem.id == newItem.id }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuffViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.answer_view, parent, false)
        return BuffViewHolder(view)
    }

    override fun getItemCount(): Int = differ.currentList.size

    override fun onBindViewHolder(holder: BuffViewHolder, position: Int) = holder.bind(author, differ.currentList[position], onAnswerClick)

    fun insertNewList(newList: List<Answer>) = differ.submitList(newList)
}

class BuffViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(author: Author?, answer: Answer, onAnswerClick: ((Answer) -> Unit)?) = with(view) {
        answer_view_image.load(R.drawable.ic_generic_answer_ico)
        answer_view_text.text = answer.title
        answer_view.setOnClickListener { onAnswerClick?.invoke(answer) }
    }
}