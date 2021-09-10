package com.example.myassignment.commonview


import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.view.LayoutInflater
import com.example.myassignment.R
import com.example.myassignment.interfaces.ProgressBarInterface

@SuppressLint("InflateParams")
class CustomProgressBar(activity: Activity) : ProgressBarInterface {

    private var isShowing = false
    private var alertDialog: AlertDialog? = null

    /* custom layout binding in alert dialog */
    init {
        val loadingView = LayoutInflater.from(activity).inflate(R.layout.loading_view, null)
        alertDialog = AlertDialog.Builder(activity, R.style.CustomDialog)
                .setView(loadingView)
                .setCancelable(false)
                .create()

        alertDialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    /**
     * showProgressBar : It shows the progress loading
     */
    override fun showProgressBar() {
        if (!isShowing && alertDialog != null) {
            isShowing = true
            alertDialog?.show()

        }
    }

    /**
     * hideProgressBar : It hide the progress loading
     */
    override fun hideProgressBar() {
        isShowing = false
        if (alertDialog != null) {
            alertDialog?.dismiss()
        }
    }
}