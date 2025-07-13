package uk.co.baconi.pka.td.errors

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorParcel(val className: String, val message: String?, val stackTrace: String) : Parcelable
