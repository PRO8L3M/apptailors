package com.apptailors.streams.data.network

import com.apptailors.streams.data.entity.ApiResult
import okhttp3.ResponseBody
import retrofit2.Converter

class ResponseConverter<T>(
    private val delegate: Converter<ResponseBody, ApiResult<T>>
) : Converter<ResponseBody, T> {

    override fun convert(value: ResponseBody): T? = delegate.convert(value)?.result
}
