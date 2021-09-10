

package com.example.myassignment.presentables

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.app.AlertDialog
import android.content.Context
import com.example.myassignment.utils.CustomError

interface ErrorPresentable {

    fun Error.showAlertDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setCancelable(false)

        builder.setMessage(this.localizedMessage)

        builder.setPositiveButton("OK") { dialog, _ ->
            // Do nothing but close the dialog
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }


    fun CustomError.showAlertDialog(context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Error")
        builder.setCancelable(false);
        builder.setMessage(this.message)

        builder.setPositiveButton("OK") { dialog, _ ->
            // Do nothing but close the dialog
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }


    fun CustomError.showAlertDialog(context: Context, title: String, message: String, positiveButton: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setCancelable(false);
        builder.setMessage(message)

        builder.setPositiveButton(positiveButton) { dialog, _ ->
            // Do nothing but close the dialog
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }


    fun CustomError.showAlertDialogWithCallback(context: Context, title: String, message: String, positiveButton: String, callback: DialogButtonsCallback) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setCancelable(false);
        builder.setMessage(message)

        builder.setPositiveButton(positiveButton) { dialog, _ ->
            callback.onPositiveButton()
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()
    }

    interface DialogButtonsCallback {
        fun onPositiveButton() {}
        fun onNegativeButton() {}
    }

    fun CustomError.showAlertDialogWithCloseActivityOption(context: Context, title: String, message: String, positiveButton: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(title)
        builder.setCancelable(false);
        builder.setMessage(message)

        builder.setPositiveButton(positiveButton) { dialog, _ ->
            // Close the dialog
            dialog.dismiss()
            // And close the activity
            (context as? Activity)?.let {
                context.setResult(RESULT_OK)
                context.finish()
            }
        }

        val alert = builder.create()
        alert.show()
    }
}