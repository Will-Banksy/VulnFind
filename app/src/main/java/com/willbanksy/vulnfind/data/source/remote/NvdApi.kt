package com.willbanksy.vulnfind.data.source.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface NvdApi {
	@GET("rest/json/cves/2.0/")
	fun getInitial(): Call<NvdCveListingDto>
	
	@GET("rest/json/cves/2.0/")
	fun getInitialWithKey(@Header("apiKey") apiKey: String): Call<NvdCveListingDto>
	
	@GET("rest/json/cves/2.0/")
	fun getSection(@Query("startIndex") startIndex: Int): Call<NvdCveListingDto>
	
	@GET("rest/json/cves/2.0/")
	fun getSectionWithKey(@Header("apiKey") apiKey: String, @Query("startIndex") startIndex: Int): Call<NvdCveListingDto>
}