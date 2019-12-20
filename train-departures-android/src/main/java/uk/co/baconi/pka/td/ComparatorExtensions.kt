package uk.co.baconi.pka.td

import android.os.Build

/**
 * Copied from java.util.Comparator as requires API 24
 */
fun <T> Comparator<T>.thenComparingWith(other: Comparator<T>): Comparator<T> {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        thenComparing(other)
    } else {
        Comparator { lhs, rhs ->
            val res = compare(lhs, rhs)
            if(res != 0) {
                res
            } else {
                other.compare(lhs, rhs)
            }
        }
    }
}