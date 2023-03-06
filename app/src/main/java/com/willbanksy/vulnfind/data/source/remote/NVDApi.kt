package com.willbanksy.vulnfind.data.source.remote

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NVDApi {
	@GET("rest/json/cves/2.0/")
	fun getCveById(@Query("cveId") cveId: String): Call<CveListingDto>
	
	@GET("rest/json/cves/2.0/")
	fun getInitial(): Call<CveListingDto>
	
	@GET("rest/json/cves/2.0/")
	fun getSection(@Query("startIndex") startIndex: Int): Call<CveListingDto>
}