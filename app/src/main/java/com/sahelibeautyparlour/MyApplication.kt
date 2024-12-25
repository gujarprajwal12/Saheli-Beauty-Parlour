package com.sahelibeautyparlour

import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Intent
import android.util.Log
import com.google.android.datatransport.BuildConfig
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.FirebaseApp
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        // Initialize Crashlytics
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true)

        // Initialize Timber for logging
        Timber.plant(Timber.DebugTree())

        // Set the global uncaught exception handler
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            // Capture crash log
            val crashLog = "Crash occurred in thread ${thread.name}.\n" +
                    "Message: ${throwable.message}\n" +
                    "Stack Trace: ${Log.getStackTraceString(throwable)}"

            // Send the crash log by email
            sendCrashLogByEmail(crashLog)

            // Send the exception to Crashlytics
            FirebaseCrashlytics.getInstance().recordException(throwable)

            // Log the crash (for local debugging)
            Timber.e(throwable, "Uncaught Exception")

            // Let the system handle the crash
            android.os.Process.killProcess(android.os.Process.myPid())
            System.exit(1)


        }
    }

    // Function to send crash log by email
    private fun sendCrashLogByEmail(log: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "message/rfc822"
        intent.putExtra(Intent.EXTRA_EMAIL, arrayOf("gujarprajwal12@gmail.com"))  // Replace with your email
        intent.putExtra(Intent.EXTRA_SUBJECT, "Crash Log - MyApp(Saheli Beauty Parlour)")
        intent.putExtra(Intent.EXTRA_TEXT, log)

        try {
            startActivity(Intent.createChooser(intent, "Send Crash Log"))
        } catch (e: ActivityNotFoundException) {
            Log.e("CrashLog", "No email client found", e)
        }
    }
}
