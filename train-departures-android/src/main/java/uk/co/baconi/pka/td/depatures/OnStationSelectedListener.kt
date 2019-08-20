package uk.co.baconi.pka.td.depatures

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.annotation.StringRes
import uk.co.baconi.pka.common.StationCode

class OnStationSelectedListener(
    private val activity: Activity,
    @StringRes private val type: Int
) : AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        val selected = parent.getItemAtPosition(position) as StationCode
        saveStationSelection(selected)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {
        saveStationSelection(null)
    }

    private fun saveStationSelection(value: StationCode?) {
        with(activity.getPreferences(Context.MODE_PRIVATE).edit()) {
            putString(activity.getString(type), value?.crsCode)
            apply()
        }
    }
}