package com.timepasslabs.positivenote.extensions

import android.content.Context
import android.widget.Toast
import androidx.annotation.StringRes

fun Context.toast(@StringRes stringResId :Int) {
	this.toast(this.getString(stringResId))
}

fun Context.toast(message : String) {
	Toast.makeText(this,message,Toast.LENGTH_SHORT).show()
}