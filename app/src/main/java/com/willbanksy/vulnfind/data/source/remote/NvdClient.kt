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
		.connectTimeout(Duration.ofSeconds(0))
		.callTimeout(Duration.ofSeconds(60))
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
		Log.d("Intercepted Request", "Network request to: $request") // Or log requests
		val response = chain.proceed(request)
		Log.d("Intercepted Response", "Network response: $response") // Or log requests
		return response
	}
}