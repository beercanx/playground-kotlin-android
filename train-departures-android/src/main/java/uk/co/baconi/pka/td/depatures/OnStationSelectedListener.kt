package uk.co.baconi.pka.td.depatures

import android.view.View
import android.widget.AdapterView
import uk.co.baconi.pka.common.stations.StationCode

class OnStationSelectedListener(private val saveSelection: (StationCode) -> Unit) : AdapterView.OnItemSelectedListener {

    override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
        saveSelection(parent.getItemAtPosition(position) as StationCode)
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
    }
}