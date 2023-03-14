package com.willbanksy.vulnfind.util

import androidx.compose.ui.graphics.Color
import com.willbanksy.vulnfind.data.VulnMetric
import com.willbanksy.vulnfind.ui.theme.CvssColours

enum class CvssSeverity {
	NONE,
	LOW,
	MEDIUM,
	HIGH,
	CRITICAL;

	companion object {
		fun mapFrom(score: Float): CvssSeverity {
			return if(score == 0f) {
				NONE
			} else if(score < 4f) {
				LOW
			} else if(score < 7f) {
				MEDIUM
			} else if(score < 9f) {
				HIGH
			} else {
				CRITICAL
			}
		}
		
		fun parseFrom(severity: String): CvssSeverity {
			return if(severity == "NONE") {
				NONE
			} else if(severity == "LOW") {
				LOW
			} else if(severity == "MEDIUM") {
				MEDIUM
			} else if(severity == "HIGH") {
				HIGH
			} else if(severity == "CRITICAL") {
				CRITICAL
			} else {
				NONE
			}
		}
	}
}

fun cvssColourForSeverity(score: CvssSeverity): Color {
	return when(score) {
		CvssSeverity.NONE -> {
			Color(CvssColours.NONE)
		}
		CvssSeverity.LOW -> {
			Color(CvssColours.LOW)
		}
		CvssSeverity.MEDIUM -> {
			Color(CvssColours.MEDIUM)
		}
		CvssSeverity.HIGH -> {
			Color(CvssColours.HIGH)
		}
		CvssSeverity.CRITICAL -> {
			Color(CvssColours.CRITICAL)
		}
	}
}

fun parseVersion(version: String): Float {
	val numStr = version.substring(5)
	return numStr.toFloatOrNull() ?: 0f
}

fun pickPrimaryMetric(metrics: List<VulnMetric>): VulnMetric? {
	var ret: VulnMetric? = null
	var retVersion = 0f
	for(m in metrics) {
		val mVersion = parseVersion(m.version)
		if(ret == null) {
			ret = m
			retVersion = mVersion
		} else if(mVersion > retVersion) {
			ret = m
			retVersion = mVersion
		}
	}
	return ret
}