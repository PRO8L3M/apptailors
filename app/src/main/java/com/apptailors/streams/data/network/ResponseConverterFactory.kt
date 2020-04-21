package com.apptailors.streams.data.network

import com.apptailors.streams.data.entity.ApiResult
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

object ResponseConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {

        val wrappedType = object : ParameterizedType {
            override fun getRawType(): Type = ApiResult::class.java
            override fun getOwnerType(): Type? = null
            override fun getActualTypeArguments(): Array<Type> = arrayOf(type)
        }
        val delegate = retrofit.nextResponseBodyConverter<Converter<ResponseBody, ApiResult<*>>>(
            this, wrappedType, annotations
        ) as Converter<ResponseBody, ApiResult<Any>>

        return ResponseConverter(delegate)
    }
}