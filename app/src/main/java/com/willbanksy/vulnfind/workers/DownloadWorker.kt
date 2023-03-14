package com.willbanksy.vulnfind.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.core.app.NotificationCompat
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.willbanksy.vulnfind.MainActivity
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.data.source.VulnRepository
import com.willbanksy.vulnfind.data.source.local.VulnDB
import com.willbanksy.vulnfind.data.source.local.VulnLocalDataSource
import com.willbanksy.vulnfind.data.source.remote.VulnRemoteDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DownloadWorker(
	context: Context,
	params: WorkerParameters,
) : CoroutineWorker(context, params) {
	private val DOWNLOAD_NOTIF_ID = 0
	private val DOWNLOAD_CHANNEL_ID = "com.willbanksy.vulnfind.DownloadSyncChannel"
	
	private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
	private val vulnDB = Room.databaseBuilder(applicationContext, VulnDB::class.java, "VulnDB").build()
	private val repository = VulnRepository(VulnRemoteDataSource(), VulnLocalDataSource(vulnDB.dao()))
	
	private var itemsPerSection = -1
	private var totalItems = -1
	private var currentSection = -1
	
	private fun createForegroundInfo(initial: Boolean) : ForegroundInfo {
		val title = "Downloading NVD"
		val cancel = "Cancel"
		
		val intent = WorkManager.getInstance(applicationContext).createCancelPendingIntent(id)
		
		val openIntent = Intent(applicationContext, MainActivity::class.java)
		val openPending = PendingIntent.getActivity(applicationContext, 0, openIntent, FLAG_IMMUTABLE)
		
		createChannel()
		
		val completedItems = (currentSection * itemsPerSection).coerceAtMost(totalItems)
		val percentProgress = (completedItems.toFloat() / totalItems.toFloat()) * 100
		val percentProgressStr = if(initial) {
			"${"%.2f".format(0f)}%"
		} else {
			"${"%.2f".format(percentProgress)}%"
		}
		
		val notification = NotificationCompat.Builder(applicationContext, DOWNLOAD_CHANNEL_ID)
			.setContentTitle(title)
			.setTicker(title)
			.setSubText(percentProgressStr)
			.setProgress(totalItems, currentSection * itemsPerSection, false)
			.setSmallIcon(android.R.drawable.stat_sys_download)
			.setOngoing(true)
			.addAction(android.R.drawable.ic_delete, cancel, intent)
			.setContentIntent(openPending)
			.setCategory(NotificationCompat.CATEGORY_PROGRESS)
			.build()
		
		return ForegroundInfo(DOWNLOAD_NOTIF_ID, notification)
	}
	
	private fun createChannel() {
		val name = applicationContext.getString(R.string.notification_channel_name)
		val desc = applicationContext.getString(R.string.notification_channel_desc)
		val importance = NotificationManager.IMPORTANCE_LOW
		val channel = NotificationChannel(DOWNLOAD_CHANNEL_ID, name, importance)
		channel.description = desc
		notificationManager.createNotificationChannel(channel)
	}
	
	override suspend fun doWork(): Result {
		try {
			setForeground(createForegroundInfo(true))
			withContext(Dispatchers.IO) {
				while(true) {
					downloadNVDSection()
					if(currentSection * itemsPerSection >= totalItems) {
						break
					}
					Thread.sleep(6000) // Sleep for 6 seconds to avoid being temporarily blocked from the NVD
					setForeground(createForegroundInfo(false))
				}
			}
		} catch (e: Exception) {
			// It is possible that the notification does not disappear when the job is cancelled
			// So in that case, we catch the JobCancellationException and manually cancel the previously shown notifications (1)
			notificationManager.cancelAll()
			Log.d(TAG, e.toString())
		}
		return Result.success()
	}
	
	private suspend fun downloadNVDSection() {
		if(totalItems == -1 && currentSection == -1 && itemsPerSection == -1) {
			val pagingInfo = repository.refreshWithPagingInfo()
			if(pagingInfo != null) {
				totalItems = pagingInfo.totalItems
				itemsPerSection = pagingInfo.itemsPerPage
				currentSection = 1
			}
		} else {
			repository.refreshSection(currentSection, VulnRepository.PagingInfo(itemsPerSection, totalItems))
			currentSection++
		}
	}
	
	companion object {
		const val UNIQUE_WORK_ID = "com.willbanksy.vulnfind.DownloadWorker"
	}
}