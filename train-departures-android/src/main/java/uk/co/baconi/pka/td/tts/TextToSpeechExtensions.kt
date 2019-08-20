package uk.co.baconi.pka.td.tts

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.os.Build
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import uk.co.baconi.pka.td.settings.Settings
import uk.co.baconi.pka.td.settings.SpeechType

fun createTextToSpeech(context: Context) = TextToSpeech(context) { status: Int ->
    when(status) {
        TextToSpeech.SUCCESS -> Log.d("TextToSpeech", "TTS started successfully")
        TextToSpeech.ERROR -> Log.e("TextToSpeech", "TTS failed to start")
    }
}

fun TextToSpeech.configureContentType() {

    val audioAttributes = AudioAttributes.Builder()
        .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
        .build()

    setAudioAttributes(audioAttributes)
}

fun TextToSpeech.configureFocusGain(context: Context) {

    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager

    // TODO - Work out how we do it for older devices
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

        setOnUtteranceProgressListener(object : UtteranceProgressListener() {

            val focusRequests = mutableMapOf<String, AudioFocusRequest>()

            override fun onStart(utteranceId: String) {

                val focusGain = when(Settings.WhichSpeechType.getSetting(context)) {
                    SpeechType.PAUSE_OTHER_SOUNDS -> AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_EXCLUSIVE
                    SpeechType.DIM_OTHER_SOUNDS -> AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK
                }

                val audioFocusRequest = AudioFocusRequest
                    .Builder(focusGain)
                    .build()

                focusRequests[utteranceId] = audioFocusRequest

                audioManager.requestAudioFocus(audioFocusRequest)
            }

            override fun onDone(utteranceId: String) {
                val focusRequest = focusRequests[utteranceId]
                if(focusRequest != null) {
                    audioManager.abandonAudioFocusRequest(focusRequest)
                }
            }

            override fun onStop(utteranceId: String, interrupted: Boolean) {
                onDone(utteranceId)
            }

            override fun onError(utteranceId: String) {
                onDone(utteranceId)
            }
        })
    }
}