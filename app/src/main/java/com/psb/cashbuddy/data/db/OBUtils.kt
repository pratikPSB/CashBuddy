package com.psb.cashbuddy.data.db

import android.content.Context
import com.psb.cashbuddy.data.db.models.MyObjectBox
import io.objectbox.BoxStore

class OBUtils(val context: Context) {
    val store: BoxStore by lazy {
        MyObjectBox.builder()
            .androidContext(context)
            .build()
    }
}