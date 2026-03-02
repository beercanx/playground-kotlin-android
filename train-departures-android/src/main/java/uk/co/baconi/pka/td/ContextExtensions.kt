package uk.co.baconi.pka.td

import android.content.Context
import android.os.Build
import androidx.annotation.ColorRes

fun Context.getColourCompat(@ColorRes id: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getColor(id)
    } else {
        resources.getColor(id)
    }
}