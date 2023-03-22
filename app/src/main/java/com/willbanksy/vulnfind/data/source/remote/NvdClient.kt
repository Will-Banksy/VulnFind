package com.willbanksy.vulnfind.data.source.remote

import android.util.Log
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.time.Duration

object NvdClient {
	private const val BASE_URL = "https://services.nvd.nist.gov/"
	
	private val okHttpClient: OkHttpClient = OkHttpClient()
		.newBuilder()
		.connectTimeout(Duration.ofSeconds(20)) // TODO: Timeouts (there are 2 more that need set I think)
		.addInterceptor(NvdClientInterceptor)
		.build()
	
	private val jacksonConfig: ObjectMapper = ObjectMapper()
		.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
	
	fun getClient(): Retrofit =
		Retrofit.Builder()
			.client(okHttpClient)
			.baseUrl(BASE_URL)
			.addConverterFactory(JacksonConverterFactory.create(jacksonConfig))
			.build()
}

object NvdClientInterceptor : Interceptor {
	override fun intercept(chain: Interceptor.Chain): Response { // Can do stuff like add headers, authorisation, api keys etc. here
		val request = chain.request()
		Log.d("NVDClientInterceptor", "Network request to: ${request.url()}") // Or log requests
		return chain.proceed(request)
	}
}