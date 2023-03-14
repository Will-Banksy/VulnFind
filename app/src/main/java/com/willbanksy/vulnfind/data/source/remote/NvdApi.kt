package com.willbanksy.vulnfind.data.source.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NvdApi {
	@GET("rest/json/cves/2.0/")
	fun getCveById(@Query("cveId") cveId: String): Call<NvdCveListingDto>
	
	@GET("rest/json/cves/2.0/")
	fun getInitial(): Call<NvdCveListingDto>
	
	@GET("rest/json/cves/2.0/")
	fun getSection(@Query("startIndex") startIndex: Int): Call<NvdCveListingDto>
}