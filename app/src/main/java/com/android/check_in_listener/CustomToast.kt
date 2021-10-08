package com.android.check_in_listener

import android.app.Activity
import android.view.Gravity
import android.widget.TextView
import android.widget.Toast

fun Toast.showCustomToast(msg:String, activity: Activity, durationToast:Int) {
    val layout = activity.layoutInflater.inflate (
        R.layout.custom_toast_layout,
        activity.findViewById(R.id.toast_layout)
    )

    val textView = layout.findViewById<TextView>(R.id.toast_text)
    textView.text = msg

    this.apply {
        setGravity(Gravity.CENTER, 0, 200)
        duration = durationToast
        view = layout
        show()
    }
}