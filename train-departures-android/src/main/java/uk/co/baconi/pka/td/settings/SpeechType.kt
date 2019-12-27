package uk.co.baconi.pka.td.settings

import androidx.annotation.StringRes
import uk.co.baconi.pka.td.R

enum class SpeechType(@StringRes override val display: Int) : DisplayValue {
    PAUSE_OTHER_SOUNDS(R.string.pref_which_speech_type_pause),
    DIM_OTHER_SOUNDS(R.string.pref_which_speech_type_dim)
}