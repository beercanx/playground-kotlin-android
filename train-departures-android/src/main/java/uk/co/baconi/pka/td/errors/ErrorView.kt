package uk.co.baconi.pka.td.errors

import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_error.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import uk.co.baconi.pka.td.R
import uk.co.baconi.pka.td.tables.updateRow

interface ErrorView {

    fun AppCompatActivity.displayErrorView(error: Throwable? = null) = GlobalScope.launch(Dispatchers.Main)  {
        // Configure to be error layout
        setContentView(R.layout.activity_error)
        setSupportActionBar(error_toolbar)

        // Update error layout
        updateRow(error_class_row, error_class_value, if (error == null) null else error::class)
        updateRow(error_message_row, error_message_value, error?.message)
        updateRow(error_stacktrace_row, error_stacktrace_value, error?.printStackTraceAsString())
    }
}