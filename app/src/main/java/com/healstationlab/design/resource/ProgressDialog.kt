package com.healstationlab.design.resource

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import com.healstationlab.design.R

class ProgressDialog {
    companion object {
        @SuppressLint("InflateParams")
        fun progressDialog(context: Context): Dialog {
            val dialog= Dialog(context)
            val inflate= LayoutInflater.from(context).inflate(R.layout.progress, null)
            dialog.setContentView(inflate)
            dialog.setCancelable(false)
            dialog.window!!.setBackgroundDrawable(
                    ColorDrawable(Color.TRANSPARENT))
            return dialog
        }
    }
}