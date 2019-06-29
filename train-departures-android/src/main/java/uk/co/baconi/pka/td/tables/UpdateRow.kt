package uk.co.baconi.pka.td.tables

import android.view.View
import android.widget.TableRow
import android.widget.TextView

inline fun <reified A> updateRow(row: TableRow, cell: TextView, value: A?) {
    updateRow(row, value) { nullSafeValue ->
        cell.text = when(nullSafeValue) {
            is String -> nullSafeValue
            else -> nullSafeValue.toString()
        }
    }
}

inline fun <reified A> updateRow(row: TableRow, value: A?, update: (A) -> Unit) {
    when (value) {
        is A -> {
            row.visibility = View.VISIBLE
            update(value)
        }
        else -> row.visibility = View.GONE
    }
}