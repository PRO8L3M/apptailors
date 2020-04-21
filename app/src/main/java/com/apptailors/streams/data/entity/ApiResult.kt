package com.apptailors.streams.data.entity

data class ApiResult<out T>(
    val result: T
)

data class BuffResult(
    val answers: List<Answer>,
    val author: Author,
    val client_id: Int,
    val created_at: String,
    val id: Int,
    val language: String,
    val priority: Int,
    val question: Question,
    val stream_id: Int,
    val time_to_show: Int
)

data class Author(
    val first_name: String,
    val image: String,
    val last_name: String
)

data class Question(
    val category: Int,
    val id: Int,
    val title: String
)

data class Answer(
    val buff_id: Int,
    val id: Int,
    val image: Image,
    val title: String
)

data class Image(
    val `0`: X0,
    val `1`: X1,
    val `2`: X2
)

data class X0(
    val id: String,
    val key: String,
    val url: String
)

data class X1(
    val id: String,
    val key: String,
    val url: String
)

data class X2(
    val id: String,
    val key: String,
    val url: String
)