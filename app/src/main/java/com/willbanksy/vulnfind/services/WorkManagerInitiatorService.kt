package com.willbanksy.vulnfind.services

import android.Manifest
import android.app.Service
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.willbanksy.vulnfind.workers.DownloadWorker

// Service to start work with WorkManager, for use in contexts where actions must be expressed as PendingIntents such as in the case of notifications
class WorkManagerInitiatorService : Service() {
	override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
			if(this.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
				val work: OneTimeWorkRequest = OneTimeWorkRequestBuilder<DownloadWorker>().build()
				WorkManager.getInstance(this).enqueueUniqueWork(
					DownloadWorker.UNIQUE_WORK_ID,
					ExistingWorkPolicy.KEEP,
					work
				)
			}
		}
		return super.onStartCommand(intent, flags, startId)
	}
	
	override fun onBind(p0: Intent?): IBinder? {
		return null // No binding
	}
}