package com.significo.bugtracker.extensions

import android.util.Base64

fun ByteArray?.base64Encode(): String? = Base64.encodeToString(this, Base64.DEFAULT)
