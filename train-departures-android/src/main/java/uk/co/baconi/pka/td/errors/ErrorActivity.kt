package uk.co.baconi.pka.td.errors

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import uk.co.baconi.pka.td.databinding.ActivityErrorBinding
import uk.co.baconi.pka.td.tables.updateRow

class ErrorActivity : AppCompatActivity() {

    companion object {
        const val ERROR_PARCEL = "uk.co.baconi.pka.td.errors.ERROR_PARCEL"
    }

    private lateinit var errorBinding: ActivityErrorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        errorBinding = ActivityErrorBinding.inflate(layoutInflater)

        // Configure to be error layout
        setContentView(errorBinding.root)
        setSupportActionBar(errorBinding.contentAppBarLayout.toolbar)

        // Get the error data from the intent
        val error = intent.getParcelableExtra<ErrorParcel>(ERROR_PARCEL)

        // Update error layout

        updateRow(errorBinding.contentError.errorClassRow, errorBinding.contentError.errorClassValue, error?.message)
        updateRow(errorBinding.contentError.errorMessageRow, errorBinding.contentError.errorMessageValue, error?.message)
        updateRow(errorBinding.contentError.errorStacktraceRow, errorBinding.contentError.errorStacktraceValue, error?.stackTrace)
    }
}