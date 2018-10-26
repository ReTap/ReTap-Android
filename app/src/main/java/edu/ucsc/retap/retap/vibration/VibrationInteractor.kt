package edu.ucsc.retap.retap.vibration

import android.os.Vibrator
import android.util.Log
import edu.ucsc.retap.retap.messages.model.Message

/**
 * Copied from Jake Wharton's library
 * https://github.com/JakeWharton/SMSMorse/blob/master/src/com/jakewharton/smsmorse/transaction/EventReceiver.java
 */
class VibrationInteractor(
    private val vibrator: Vibrator
) {
    private companion object {

        //Preference defaults
        private const val DEFAULT_VIBRATE_COUNTS = false
        private const val DEFAULT_DOT_LENGTH = 150
        private const val DEFAULT_INITIAL_PAUSE = 500L

        //Morse code
        private const val DOTS_IN_DASH = 3
        private const val DOTS_IN_GAP = 1
        private const val DOTS_IN_LETTER_GAP = 3
        private const val DOTS_IN_WORD_GAP = 7

        //Character sets
        private const val CHARSET_MORSE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,?'!/()&:;=+-_\"$@"
        private val CHARSET_COUNTS = "0123456789"
        private const val DOT = true
        private const val DASH = false
        private val MORSE = arrayOf(
            booleanArrayOf(DOT, DASH), //A
            booleanArrayOf(DASH, DOT, DOT, DOT), //B
            booleanArrayOf(DASH, DOT, DASH, DOT), //C
            booleanArrayOf(DASH, DOT, DOT), //D
            booleanArrayOf(DOT), //E
            booleanArrayOf(DOT, DOT, DASH, DOT), //F
            booleanArrayOf(DASH, DASH, DOT), //G
            booleanArrayOf(DOT, DOT, DOT, DOT), //H
            booleanArrayOf(DOT, DOT), //I
            booleanArrayOf(DOT, DASH, DASH, DASH), //J
            booleanArrayOf(DASH, DOT, DASH), //K
            booleanArrayOf(DOT, DASH, DOT, DOT), //L
            booleanArrayOf(DASH, DASH), //M
            booleanArrayOf(DASH, DOT), //N
            booleanArrayOf(DASH, DASH, DASH), //O
            booleanArrayOf(DOT, DASH, DASH, DOT), //P
            booleanArrayOf(DASH, DASH, DOT, DASH), //Q
            booleanArrayOf(DOT, DASH, DOT), //R
            booleanArrayOf(DOT, DOT, DOT), //S
            booleanArrayOf(DASH), //T
            booleanArrayOf(DOT, DOT, DASH), //U
            booleanArrayOf(DOT, DOT, DOT, DASH), //V
            booleanArrayOf(DOT, DASH, DASH), //W
            booleanArrayOf(DASH, DOT, DOT, DASH), //X
            booleanArrayOf(DASH, DOT, DASH, DASH), //Y
            booleanArrayOf(DASH, DASH, DOT, DOT), //Z
            booleanArrayOf(DASH, DASH, DASH, DASH, DASH), //0
            booleanArrayOf(DOT, DASH, DASH, DASH, DASH), //1
            booleanArrayOf(DOT, DOT, DASH, DASH, DASH), //2
            booleanArrayOf(DOT, DOT, DOT, DASH, DASH), //3
            booleanArrayOf(DOT, DOT, DOT, DOT, DASH), //4
            booleanArrayOf(DOT, DOT, DOT, DOT, DOT), //5
            booleanArrayOf(DASH, DOT, DOT, DOT, DOT), //6
            booleanArrayOf(DASH, DASH, DOT, DOT, DOT), //7
            booleanArrayOf(DASH, DASH, DASH, DOT, DOT), //8
            booleanArrayOf(DASH, DASH, DASH, DASH, DOT), //9
            booleanArrayOf(DOT, DASH, DOT, DASH, DOT, DASH), //.
            booleanArrayOf(DASH, DASH, DOT, DOT, DASH, DASH), //,
            booleanArrayOf(DOT, DOT, DASH, DASH, DOT, DOT), //?
            booleanArrayOf(DOT, DASH, DASH, DASH, DASH, DOT), //'
            booleanArrayOf(DASH, DOT, DASH, DOT, DASH, DASH), //!
            booleanArrayOf(DASH, DOT, DOT, DASH, DOT), ///
            booleanArrayOf(DASH, DOT, DASH, DASH, DOT), //(
            booleanArrayOf(DASH, DOT, DASH, DASH, DOT, DASH), //)
            booleanArrayOf(DOT, DASH, DOT, DOT, DOT), //&
            booleanArrayOf(DASH, DASH, DASH, DOT, DOT, DOT), //:
            booleanArrayOf(DASH, DOT, DASH, DOT, DASH, DOT), //;
            booleanArrayOf(DASH, DOT, DOT, DOT, DASH), //=
            booleanArrayOf(DOT, DASH, DOT, DASH, DOT), //+
            booleanArrayOf(DASH, DOT, DOT, DOT, DOT, DASH), //-
            booleanArrayOf(DOT, DOT, DASH, DASH, DOT, DASH), //_
            booleanArrayOf(DOT, DASH, DOT, DOT, DASH, DOT), //"
            booleanArrayOf(DOT, DOT, DOT, DASH, DOT, DOT, DASH), //$
            booleanArrayOf(DOT, DASH, DASH, DOT, DASH, DOT) //@
        )
        private val COUNTS = arrayOf(
            booleanArrayOf(DASH), //0
            booleanArrayOf(DOT), //1
            booleanArrayOf(DOT, DOT), //2
            booleanArrayOf(DOT, DOT, DOT), //3
            booleanArrayOf(DOT, DOT, DOT, DOT), //4
            booleanArrayOf(DOT, DOT, DOT, DOT, DOT), //5
            booleanArrayOf(DOT, DOT, DOT, DOT, DOT, DOT), //6
            booleanArrayOf(DOT, DOT, DOT, DOT, DOT, DOT, DOT), //7
            booleanArrayOf(DOT, DOT, DOT, DOT, DOT, DOT, DOT, DOT), //8
            booleanArrayOf(DOT, DOT, DOT, DOT, DOT, DOT, DOT, DOT, DOT) //9
        )
    }

    fun vibrate(message: Message) {
        vibrator.cancel()
        val vibrations = convertToVibrations(message.sender, true)
        vibrations.addAll(convertToVibrations(message.contents, false))
        vibrateMorse(vibrations)
    }

    private fun vibrateMorse(vibrationLongs: ArrayList<Long>) {
        val vibrations = LongArray(vibrationLongs.size)
        val morseVibrations = StringBuffer("Vibrating Morse: ")

        //Unbox the array and generate a log line simultaneously
        for (i in 0 until vibrationLongs.size) {
            vibrations[i] = vibrationLongs[i]
            morseVibrations.append(if (i % 2 == 0) '-' else '+')
            morseVibrations.append(vibrationLongs[i])
        }

        this.vibrator.vibrate(vibrations, -1)
        Log.d(this::class.java.name, morseVibrations.toString())
    }

    private fun convertToVibrations(message: String, isNumber: Boolean): ArrayList<Long> {
        val vibrateCounts = DEFAULT_VIBRATE_COUNTS

        //Establish all lengths
        val dot = DEFAULT_DOT_LENGTH

        val dash = dot * DOTS_IN_DASH
        val gap = dot * DOTS_IN_GAP
        val letterGap = dot * DOTS_IN_LETTER_GAP
        val wordGap = dot * DOTS_IN_WORD_GAP

        val words =
            message.toUpperCase().trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val vibrationObjects = ArrayList<Long>()
        val charset = if (isNumber && vibrateCounts) CHARSET_COUNTS else CHARSET_MORSE
        val lookups = if (isNumber && vibrateCounts) COUNTS else MORSE

        //Add initial pause
        vibrationObjects.add(DEFAULT_INITIAL_PAUSE)

        var word: String
        var letterBooleans: BooleanArray
        var letterIndex: Int
        for (i in words.indices) {
            word = words[i]

            for (j in 0 until word.length) {
                letterIndex = charset.indexOf(word[j].toString())

                if (letterIndex >= 0) {
                    letterBooleans = lookups[letterIndex]

                    for (k in letterBooleans.indices) {
                        vibrationObjects.add(if (letterBooleans[k]) dot.toLong() else dash.toLong())

                        if (k < letterBooleans.size - 1)
                            vibrationObjects.add(gap.toLong())
                    }
                    if (j < word.length - 1)
                        vibrationObjects.add(letterGap.toLong())
                }
            }
            if (i < words.size - 1)
                vibrationObjects.add(wordGap.toLong())
        }

        return vibrationObjects
    }
}