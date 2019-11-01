package uk.co.baconi.pka.td.depatures

import android.app.Activity
import android.content.Context
import android.view.View
import android.widget.AdapterView
import androidx.annotation.StringRes
import uk.co.baconi.pka.common.StationCode
import uk.co.baconi.pka.common.StationCodes
import uk.co.baconi.pka.td.R

class StationSelections(private val activity: Activity) {

    fun saveStationSelectionFrom(value: StationCode) {
        saveStation(R.string.pref_from_station_code, value)
    }

    fun getStationSelectionFrom(): StationCode {
        return getStation(R.string.pref_from_station_code, R.string.pref_from_station_code_default)
    }

    fun saveStationSelectionTo(value: StationCode) {
        saveStation(R.string.pref_to_station_code, value)
    }

    fun getStationSelectionTo(): StationCode {
        return getStation(R.string.pref_to_station_code, R.string.pref_to_station_code_default)
    }

    private fun saveStation(@StringRes type: Int, value: StationCode) {
        with(activity.getPreferences(Context.MODE_PRIVATE).edit()) {
            putString(activity.getString(type), value.crsCode)
            apply()
        }
    }

    private fun getStation(@StringRes type: Int, @StringRes default: Int): StationCode {
        val crsCode = with(activity.getPreferences(Context.MODE_PRIVATE)) {
            getString(activity.getString(type), null) ?: activity.getString(default)
        }
        return StationCodes.firstByCode(crsCode)
    }
}