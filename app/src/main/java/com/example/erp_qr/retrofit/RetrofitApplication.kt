package com.example.erp_qr.retrofit

import com.google.gson.GsonBuilder
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

object RetrofitProvider {
    val BASE_URL = "http://192.168.100.165:8080"

    private val nullOnEmptyConverterFactory: Converter.Factory = object : Converter.Factory() {
        override fun responseBodyConverter(
            type: Type,
            annotations: Array<out Annotation>,
            retrofit: Retrofit
        ): Converter<ResponseBody, *> {
            val delegate = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
            return Converter<ResponseBody, Any?> { body ->
                if (body.contentLength() == 0L) null else delegate.convert(body)
            }
        }
    }

    private val gson = GsonBuilder().setLenient().create()

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(nullOnEmptyConverterFactory)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val networkService: NetworkService by lazy {
        retrofit.create(NetworkService::class.java)
    }
}
