package uk.co.baconi.pka.td.errors

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.content_app_bar_layout.*
import kotlinx.android.synthetic.main.content_error.*
import uk.co.baconi.pka.td.R
import uk.co.baconi.pka.td.tables.updateRow

class ErrorActivity : AppCompatActivity() {

    companion object {
        const val ERROR_PARCEL = "uk.co.baconi.pka.td.errors.ERROR_PARCEL"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Configure to be error layout
        setContentView(R.layout.activity_error)
        setSupportActionBar(toolbar)

        // Get the error data from the intent
        val error = intent.getParcelableExtra<ErrorParcel>(ERROR_PARCEL)

        // Update error layout
        updateRow(error_class_row, error_class_value, error?.message)
        updateRow(error_message_row, error_message_value, error?.message)
        updateRow(error_stacktrace_row, error_stacktrace_value, error?.stackTrace)
    }
}