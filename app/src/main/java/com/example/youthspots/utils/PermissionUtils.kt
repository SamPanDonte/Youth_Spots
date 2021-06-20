package com.example.youthspots.utils

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment


object PermissionUtils {

    private fun shouldShowRationale(activity: AppCompatActivity, permissions: List<String>) : Boolean {
        for (permission in permissions) {
            if (shouldShowRequestPermissionRationale(activity, permission)) {
                return true
            }
        }
        return false
    }

    fun checkPermissions(context: Context, vararg permissions: String) : Boolean {
        for (permission in permissions) {
            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun checkAndRequestPermissions(
        activity: AppCompatActivity,
        rationaleStringResource: Int,
        callback: ActivityResultLauncher<Array<String>>,
        failedCallback: android.content.DialogInterface.OnClickListener?,
        vararg permissions: String
    ): Boolean {
        val missing = permissions.filter {
            ContextCompat.checkSelfPermission(activity.applicationContext, it) != PackageManager.PERMISSION_GRANTED
        }
        return when {
            missing.isEmpty() -> true
            shouldShowRationale(activity, missing) -> {
                RationaleDialog(
                    rationaleStringResource,
                    callback,
                    failedCallback,
                    missing.toTypedArray()
                ).show(activity.supportFragmentManager, null)
                false
            }
            else -> {
                callback.launch(missing.toTypedArray())
                false
            }
        }
    }

    class RationaleDialog(
        private val stringRes: Int,
        private val callback: ActivityResultLauncher<Array<String>>,
        private val failedCallback: android.content.DialogInterface.OnClickListener?,
        private val permissions: Array<String>
    ) : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return AlertDialog.Builder(activity)
                .setMessage(stringRes)
                .setPositiveButton(android.R.string.ok) { _, _ ->
                    callback.launch(permissions)
                }
                .setNegativeButton(android.R.string.cancel, failedCallback)
                .create()
        }
    }
}