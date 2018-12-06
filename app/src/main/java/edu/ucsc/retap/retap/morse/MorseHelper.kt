package edu.ucsc.retap.retap.morse

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import edu.ucsc.retap.retap.R

@Suppress("DEPRECATION")
object MorseHelper {
    private const val DOT = true
    private const val DASH = false
    private const val DOT_STRING = "•"
    private const val DASH_STRING = "–"

    //Character sets
    const val CHARSET_MORSE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789.,?'!/()&:;=+-_\"$@"
    const val CHARSET_COUNTS = "0123456789"
    val MORSE = arrayOf(
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
    val COUNTS = arrayOf(
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

    fun convertToMorse(context: Context, message: String): Spannable {
        val stringBuilder = SpannableStringBuilder()
        for (rawCharacter in message.toCharArray()) {
            val character = Character.toUpperCase(rawCharacter)
            val morseChars = CHARSET_MORSE.toCharArray()
            val countsChars = CHARSET_COUNTS.toCharArray()

            when (character) {
                in morseChars -> {
                    MORSE[morseChars.indexOf(character)].forEach {
                        appendMorse(it, context, stringBuilder)
                    }

                }
                in countsChars -> {
                    COUNTS[countsChars.indexOf(character)].forEach {
                        appendMorse(it, context, stringBuilder)
                    }
                }
                else -> stringBuilder.append(character)
            }
        }

        return stringBuilder
    }

    fun convertToText(morse: String): String {
        val escapedSpaces = morse.replace("  ", " / ")
        val morseCharacters = escapedSpaces.split(" ")
        return morseCharacters.map {
            var invalidMorse = false
            val boolArray = it.toCharArray().map {
                when {
                    it.toString() == DOT_STRING -> {
                        DOT
                    }
                    it.toString() == DASH_STRING -> {
                        DASH
                    }
                    else -> {
                        invalidMorse = true
                        DOT
                    }
                }
            }
                    .toBooleanArray()

            if (invalidMorse) {
                return@map it
            }

            val morseCharsetIndex = indexOf(MORSE, boolArray)
            val countsCharsetIndex = indexOf(COUNTS, boolArray)

            if (morseCharsetIndex >= 0) {
                return@map CHARSET_MORSE[morseCharsetIndex].toString()
            }
            if (countsCharsetIndex >= 0) {
                return@map CHARSET_COUNTS[countsCharsetIndex].toString()
            }
            return@map it
        }
                .joinToString("")
                .replace("/", " ")
    }

    private fun indexOf(booleanArrays: Array<BooleanArray>, subject: BooleanArray): Int {
        return booleanArrays
                .map {
                    subject
                            .filterIndexed { index, boolean ->
                                index >= it.size || it[index] != boolean }
                            .none() && (subject.size == it.size)
                }
                .indexOfFirst { it }
    }

    fun appendMorse(dot: Boolean, context: Context, stringBuilder: SpannableStringBuilder) {
        val dotString = SpannableString(DOT_STRING)
        if (dot) {
            val redSpan = ForegroundColorSpan(context.resources.getColor(R.color.blueColor))
            dotString.setSpan(redSpan, 0, dotString.length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            stringBuilder.append(dotString)
        } else {
            val blueSpan = ForegroundColorSpan(context.resources.getColor(R.color.redColor))
            val dashString = SpannableString(DASH_STRING)
            dashString.setSpan(blueSpan, 0, dashString.length,
                    Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
            stringBuilder.append(dashString)
        }
    }
}