package com.willbanksy.vulnfind.data.source.remote

import com.fasterxml.jackson.annotation.JsonProperty

// https://csrc.nist.gov/schema/nvd/api/2.0/cve_api_json_2.0.schema

data class CveListingDto(
	@JsonProperty("resultsPerPage") val resultsPerPage: Int,
	@JsonProperty("startIndex") val startIndex: Int,
	@JsonProperty("totalResults") val totalResults: Int,
	@JsonProperty("format") val format: String,
	@JsonProperty("version") val version: String,
	@JsonProperty("timestamp") val timestamp: String,
	@JsonProperty("vulnerabilities") val vulnerabilities: List<VulnerabilityDto>,
)

data class VulnerabilityDto(
	@JsonProperty("cve") val cve: CveDto,
)

data class CveDto(
	@JsonProperty("id") val id: String,
	@JsonProperty("sourceIdentifier") val sourceIdentifier: String,
	@JsonProperty("published") val published: String,
	@JsonProperty("lastModified") val lastModified: String,
	@JsonProperty("vulnStatus") val vulnStatus: String,
	@JsonProperty("descriptions") val descriptions: List<CveDescriptionDto>,
	@JsonProperty("metrics") val metrics: CveMetricsDto,
)

data class CveDescriptionDto(
	@JsonProperty("lang") val lang: String,
	@JsonProperty("value") val value: String,
)

data class CveMetricsDto(
	@JsonProperty("cvssMetricV31") val cvssMetricV31: List<CvssMetricV31Dto>,
)

data class CvssMetricV31Dto(
	@JsonProperty("source") val source: String,
	@JsonProperty("type") val type: String,
//    @JsonProperty("cvssData") val cvssData: String,
	@JsonProperty("exploitabilityScore") val exploitabilityScore: Float,
	@JsonProperty("impactScore") val impactScore: Float,
)

data class CvssDataV31Dto( // Some members of this (e.g. attackVector) are really enums but we can treat them as strings
	@JsonProperty("version") val version: String,
	@JsonProperty("vectorString") val vectorString: String,
	@JsonProperty("attackVector") val attackVector: String,
	@JsonProperty("attackComplexity") val attackComplexity: String,
	@JsonProperty("privilegesRequired") val privilegesRequired: String,
	@JsonProperty("userInteraction") val userInteraction: String,
	@JsonProperty("scope") val scope: String,
	@JsonProperty("confidentialityImpact") val confidentialityImpact: String,
	@JsonProperty("integrityImpact") val integrityImpact: String,
	@JsonProperty("availabilityImpact") val availabilityImpact: String,
	@JsonProperty("baseScore") val baseScore: Float,
	@JsonProperty("baseSeverity") val baseSeverity: Float,
	@JsonProperty("exploitCodeMaturity") val exploitCodeMaturity: String,
)