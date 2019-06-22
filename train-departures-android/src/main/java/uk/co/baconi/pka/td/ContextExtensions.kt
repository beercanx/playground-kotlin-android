package uk.co.baconi.pka.td

import android.content.Context
import android.os.Build
import androidx.annotation.ColorRes
import java.util.*

fun Context.getCurrentLocale(): Locale {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        resources.configuration.locales.get(0)
    } else {
        resources.configuration.locale
    }
}

fun Context.getColorCompat(@ColorRes id: Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        getColor(id)
    } else {
        return resources.getColor(id)
    }
}