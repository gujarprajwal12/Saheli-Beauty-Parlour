package com.sahelibeautyparlour.DataLayer

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    // Snackbar Utility
    fun showSnackbar(view: View, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
        Snackbar.make(view, message, duration).show()
    }

    // Progress Dialog Utilities
    private var progressDialog: AlertDialog? = null

    fun showProgress(context: Context, message: String) {
        if (progressDialog == null) {
            val builder = AlertDialog.Builder(context)
            builder.setView(View.inflate(context, android.R.layout.simple_spinner_item, null))
            builder.setMessage(message)
            builder.setCancelable(false)
            progressDialog = builder.create()
            progressDialog?.show()
        }
    }

    fun hideProgress() {
        progressDialog?.dismiss()
        progressDialog = null
    }



    // Date Conversion Utilities
    fun formatDate(
        inputDate: String,
        inputFormat: String = "yyyy-MM-dd",
        outputFormat: String = "dd/MM/yyyy"
    ): String? {
        return try {
            val sdfInput = SimpleDateFormat(inputFormat, Locale.getDefault())
            val sdfOutput = SimpleDateFormat(outputFormat, Locale.getDefault())
            val date = sdfInput.parse(inputDate)
            date?.let { sdfOutput.format(it) }
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun convertMillisToDate(milliseconds: Long, outputFormat: String = "dd/MM/yyyy HH:mm:ss"): String {
        return try {
            val sdf = SimpleDateFormat(outputFormat, Locale.getDefault())
            val date = Date(milliseconds)
            sdf.format(date)
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }

    fun getCurrentDate(outputFormat: String = "yyyy-MM-dd HH:mm:ss"): String {
        val sdf = SimpleDateFormat(outputFormat, Locale.getDefault())
        return sdf.format(Date())
    }



//
//    val formattedDate = AppUtils.formatDate("2024-12-25", "yyyy-MM-dd", "MMM dd, yyyy")
//    val readableDate = AppUtils.convertMillisToDate(System.currentTimeMillis())
//    val currentDate = AppUtils.getCurrentDate()

}
