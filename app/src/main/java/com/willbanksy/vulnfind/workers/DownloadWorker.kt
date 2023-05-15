package com.willbanksy.vulnfind.workers

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.room.Room
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.willbanksy.vulnfind.MainActivity
import com.willbanksy.vulnfind.R
import com.willbanksy.vulnfind.data.source.VulnRepository
import com.willbanksy.vulnfind.data.source.local.SettingsLocalDataSource
import com.willbanksy.vulnfind.data.source.local.VulnDB
import com.willbanksy.vulnfind.data.source.local.VulnLocalDataSource
import com.willbanksy.vulnfind.data.source.local.settingsDataStore
import com.willbanksy.vulnfind.data.source.remote.VulnRemoteDataSource
import com.willbanksy.vulnfind.services.WorkManagerInitiatorService
import com.willbanksy.vulnfind.utils.isConnectionMetered
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException

enum class ErrorType {
	UnknownHostError,
	TimedOutError,
	MiscNetworkError,
	DatabaseAccessError,
	MeteredConnectionError,
	OtherError,
}

data class MeteredConnectionException(val e: String): Exception()

class DownloadWorker(
	context: Context,
	params: WorkerParameters,
) : CoroutineWorker(context, params) {
	private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
	private val vulnDB = Room.databaseBuilder(applicationContext, VulnDB::class.java, "VulnDB")
		.enableMultiInstanceInvalidation()
		.build()
	private val repository = VulnRepository(VulnRemoteDataSource(), VulnLocalDataSource(vulnDB.dao()))
	
	private var itemsPerSection = -1
	private var totalItems = -1
	private var currentSection = -1
	
	private var currentApiKey: String? = null
	private var currentUseMetered: Boolean? = null
	
	private fun createForegroundInfo(initial: Boolean) : ForegroundInfo {
		val title = applicationContext.getString(R.string.notification_download_title)
		val cancel = applicationContext.getString(R.string.notification_download_action_cancel)
		
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
		val name = applicationContext.getString(R.string.notification_channel_download_name)
		val desc = applicationContext.getString(R.string.notification_channel_download_desc)
		val importance = NotificationManager.IMPORTANCE_LOW
		val channel = NotificationChannel(DOWNLOAD_CHANNEL_ID, name, importance)
		channel.description = desc
		notificationManager.createNotificationChannel(channel)
	}
	
	override suspend fun doWork(): Result {
		// Cancel error notifications if there are any
		notificationManager.cancel(DOWNLOAD_FAILURE_NOTIF_ID)
		
		try {
			var initial = true
//			var lastRequestedAt: Long = -1
			withContext(Dispatchers.IO) {
				while(true) {
					currentApiKey = applicationContext.settingsDataStore.data.firstOrNull()?.get(SettingsLocalDataSource.PKEY_API_KEY)
					currentUseMetered = applicationContext.settingsDataStore.data.firstOrNull()?.get(SettingsLocalDataSource.PKEY_USE_METERED)
					
					if(currentUseMetered == false && isConnectionMetered(applicationContext)) {
						throw MeteredConnectionException("")
					}
					
					if(initial) {
						setForeground(createForegroundInfo(true))
						setProgress(Data.Builder().putFloat(PARAM_PROGRESS, 0f).build())
						initial = false
					}

//					val now = System.currentTimeMillis()
//					if(now - lastRequestedAt < 6000 && (lastRequestedAt != -1L)) {
//						Log.d("Thread Was Not Slept For Long Enough","Oh no!")
//					} else {
//						Log.d("Thread Was Slept For Long Enough", "${now - lastRequestedAt}")
//					}
//					lastRequestedAt = now
					downloadNvdSection(currentApiKey)
					setForeground(createForegroundInfo(false))

					val completedItems = (currentSection * itemsPerSection).coerceAtMost(totalItems)
					val percentProgress = (completedItems.toFloat() / totalItems.toFloat()) * 100
					setProgress(Data.Builder().putFloat(PARAM_PROGRESS, percentProgress).build())

					if(currentSection * itemsPerSection >= totalItems) {
						Log.d("Things", "currentSection: ${currentSection}, itemsPerSection: ${itemsPerSection}, totalItems: ${totalItems}")
						break
					}
					
					if(!currentApiKey.isNullOrEmpty()) {
						Thread.sleep(1700) // But with an API key, we only have to sleep for 1.6666 seconds (rounding up to 1.7 to be safe)
					} else {
						Thread.sleep(6000) // Sleep for 6 seconds to avoid being temporarily blocked from the NVD
					}
				}
			}
			return Result.success()
		} catch(e: UnknownHostException) { // Possible unknown host exception. Want to catch this and notify the user
			Log.d("DownloadWorker Network Exception", e.toString())
			notificationManager.cancel(DOWNLOAD_NOTIF_ID)
			createErrorNotification(ErrorType.UnknownHostError)
		} catch(e: SocketTimeoutException) { // Request can time out
			Log.d("DownloadWorker Timeout Exception", e.toString())
			notificationManager.cancel(DOWNLOAD_NOTIF_ID)
			createErrorNotification(ErrorType.TimedOutError)
		} catch(e: IOException) { // Retrofit2 says that it uses IOException only for network errors (here anyway)
			Log.d("DownloadWorker Misc Exception", e.toString())
			notificationManager.cancel(DOWNLOAD_NOTIF_ID)
			createErrorNotification(ErrorType.MiscNetworkError)
		} catch (e: MeteredConnectionException) { // The exception type for when the settings say not to use metered connections but we are anyway
			Log.d("DownloadWorker Metered Exception", e.toString())
			notificationManager.cancel(DOWNLOAD_NOTIF_ID)
			createErrorNotification(ErrorType.MeteredConnectionError)
		} catch(e: CancellationException) { // This is thrown when the work is cancelled
			notificationManager.cancel(DOWNLOAD_NOTIF_ID)
			Log.d("DownloadWorker Cancellation Exception", e.toString())
			return Result.success()
		} catch(e: Exception) {
			notificationManager.cancel(DOWNLOAD_NOTIF_ID)
			Log.d("DownloadWorker Exception", e.toString())
		}
		return Result.failure()
	}
	
	private suspend fun downloadNvdSection(apiKey: String? = null) {
		if(totalItems == -1 && currentSection == -1 && itemsPerSection == -1) {
			val pagingInfo = repository.refreshWithPagingInfo(apiKey)
			Log.d("PagingInfo", pagingInfo.toString())
			if(pagingInfo != null) {
				totalItems = pagingInfo.totalItems
				itemsPerSection = pagingInfo.itemsPerPage
				currentSection = 1
			} else {
				throw IOException("Could not get paging data from NVD")
			}
		} else {
			repository.refreshSection(currentSection, VulnRepository.PagingInfo(itemsPerSection, totalItems), apiKey)
			currentSection++
		}
	}
	
	private fun createErrorNotificationChannel() {
		val name = applicationContext.getString(R.string.notification_channel_error_name)
		val desc = applicationContext.getString(R.string.notification_channel_error_desc)
		val importance = NotificationManager.IMPORTANCE_HIGH
		
		val channel = NotificationChannel(DOWNLOAD_FAILURE_CHANNEL_ID, name, importance)
		channel.description = desc
		notificationManager.createNotificationChannel(channel)
	}
	
	private fun createErrorNotification(e: ErrorType) {
		createErrorNotificationChannel()
		
		val openIntent = Intent(applicationContext, MainActivity::class.java)
		val openPending = PendingIntent.getActivity(applicationContext, 0, openIntent, FLAG_IMMUTABLE)
		
		val title = applicationContext.getString(R.string.notification_network_error_title)
		val desc = when(e) {
			ErrorType.UnknownHostError -> {
				applicationContext.getString(R.string.notification_network_error_unknown_host_desc)
			}
			ErrorType.TimedOutError -> {
				applicationContext.getString(R.string.notification_network_error_timeout_desc)
			}
			ErrorType.MiscNetworkError -> {
				applicationContext.getString(R.string.notification_network_error_misc_desc)
			}
			ErrorType.DatabaseAccessError -> {
				applicationContext.getString(R.string.notification_network_error_database_access_desc)
			}
			ErrorType.MeteredConnectionError -> {
				applicationContext.getString(R.string.notification_network_error_metered)
			}
			ErrorType.OtherError -> {
				applicationContext.getString(R.string.notification_network_error_other)
			}
		}
		
		val retryIntent = Intent(applicationContext, WorkManagerInitiatorService::class.java)
		val retryPending = PendingIntent.getService(applicationContext, 0, retryIntent, FLAG_IMMUTABLE)
		val retryActionName = applicationContext.getString(R.string.notification_network_error_action_retry)
		
		val notificationBuilder = NotificationCompat.Builder(applicationContext, DOWNLOAD_FAILURE_CHANNEL_ID)
			.setContentTitle(title)
			.setContentText(desc)
			.setTicker(title)
			.setSmallIcon(android.R.drawable.stat_notify_error)
			.setCategory(NotificationCompat.CATEGORY_ERROR)
			.setContentIntent(openPending)
			.addAction(android.R.drawable.stat_notify_sync_noanim, retryActionName, retryPending)
			.setAutoCancel(true)
			.setSilent(true)

		notificationManager.notify(DOWNLOAD_FAILURE_NOTIF_ID, notificationBuilder.build())
	}
	
	companion object {
		const val UNIQUE_WORK_ID = "com.willbanksy.vulnfind.DownloadWorker"
		
		const val DOWNLOAD_NOTIF_ID = 0
		const val DOWNLOAD_CHANNEL_ID = "com.willbanksy.vulnfind.DownloadChannel"
		
		const val DOWNLOAD_FAILURE_NOTIF_ID = 1
		const val DOWNLOAD_FAILURE_CHANNEL_ID = "com.willbanksy.vulnfind.DownloadErrorChannel"
		
		const val PARAM_PROGRESS = "percentProgress"
	}
}