package uk.co.baconi.pka.td.settings

import androidx.annotation.StringRes
import uk.co.baconi.pka.td.R

enum class SearchType(@StringRes override val display: Int) : DisplayValue {
    SINGLE_RESULT(R.string.pref_which_search_type_single),
    MULTIPLE_RESULTS(R.string.pref_which_search_type_multi)
}