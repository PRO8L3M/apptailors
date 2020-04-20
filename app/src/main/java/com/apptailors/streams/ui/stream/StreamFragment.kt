package com.apptailors.streams.ui.stream

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.apptailors.streams.R
import com.apptailors.streams.common.BaseFragment
import com.apptailors.streams.ext.toast
import org.koin.androidx.viewmodel.ext.android.viewModel

class StreamFragment : BaseFragment() {

    private val viewModel: StreamViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        return inflater.inflate(R.layout.fragment_start, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textFromRepo = viewModel.getText()
        toast(textFromRepo)
    }
}
