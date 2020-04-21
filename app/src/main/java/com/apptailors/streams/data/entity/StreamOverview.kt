package com.apptailors.streams.data.entity

data class StreamOverview(
    val id: Int,
    val title: String,
    val description: String,
    val state: String,
    val station: Int,
    val date: String,
    val imageRes: Int
    )