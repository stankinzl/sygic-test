@file:Suppress("unused")

package com.kinzlstanislav.sigyctest.core.extensions

import android.annotation.SuppressLint
import android.text.TextUtils
import android.text.format.DateFormat
import android.util.Patterns
import timber.log.Timber
import java.lang.IllegalArgumentException
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

fun String.isValidEmail(): Boolean {
    return !TextUtils.isEmpty(this) && Patterns.EMAIL_ADDRESS.matcher(this).matches()
}

fun String.formatDate(initDateFormat: String, endDateFormat: String): String? {
    return try {
        val initDate = SimpleDateFormat(initDateFormat, Locale.getDefault()).parse(this)
        val formatter = SimpleDateFormat(endDateFormat, Locale.getDefault())
        @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        formatter.format(initDate)
    } catch (e: ParseException) {
        null
    }
}

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
@SuppressLint("SimpleDateFormat")
fun String.toDate(format: String, locale: Locale? = null, dfTimezone: String? = null): Date =
    (if (locale == null) SimpleDateFormat(format) else SimpleDateFormat(format, locale))
        .apply {
            dfTimezone?.let {
                timeZone = TimeZone.getTimeZone(it)
            }
        }
        .parse(this)

@SuppressLint("SimpleDateFormat")
fun String.toTimestamp(format: String, locale: Locale? = null) = try {
    (if (locale == null) SimpleDateFormat(format) else SimpleDateFormat(format, locale))
        .parse(this)?.time
} catch (e: Exception) {
    null
}

fun List<String>.getString(): String {
    var finalString = ""
    forEach {
        finalString += if (it.isNotEmpty()) {
            "$it\n".removePrefix(" ")
        } else {
            ""
        }
    }
    return finalString.removeSuffix("\n")
}

fun List<String>.getStringWithoutNewLines(): String {
    var finalString = ""
    forEach {
        if (it.isEmpty() || it.isWhitespacesOnly()) return@forEach
        finalString += "${it.removePrefix(" ").removeNewlines()}, "
    }
    return finalString.removeSuffix(", ")
}

fun String.isWhitespacesOnly() = trim().isEmpty()

fun String.isValidUrl() = Patterns.WEB_URL.matcher(this).matches()

fun String?.thisIfNotNullAndNotEmpty(action: (String) -> String?): String? =
    if (this != null && isNotEmpty()) action(this) else ""

fun String?.notNullAndNotEmpty(action: (String) -> Any?) {
    if (!this.isNullOrEmpty()) {
        action.invoke(this)
    }
}


fun String.chop(where: Char): Pair<String, String> {
    if (!this.contains(where)) throw IllegalArgumentException("This string {$this} does not contain value $where")
    val first = this.substringBefore(where)
    val second = this.substringAfter(where)
    return Pair(first, second)
}

fun String.removeNewlines() = this.replace("\n", " ")

fun String.removeWhitespaces() = replace("\\s".toRegex(), "")

fun combineTwoPossibleNullStringsWithCustomSeparationSequence(
    string1: String?,
    string2: String?,
    separationSequence: String?,
    prefix: String? = "",
    suffix: String? = "",
    keepSeparationSequenceEvenIfString1IsEmpty: Boolean = false
): String? {
    val stringBuilder = StringBuilder()
    with(stringBuilder) {
        append(prefix)
        if (!string1.isNullOrEmpty()) {
            append(string1)
        }
        if (string1 != string2) {
            if (!separationSequence.isNullOrEmpty() && !string2.isNullOrEmpty() &&
                (toString().isNotEmpty() || keepSeparationSequenceEvenIfString1IsEmpty)) {
                append(separationSequence)
            }
            if (!string2.isNullOrEmpty()) {
                append(string2)
            }
        }
        append(suffix)
    }
    val finalString = stringBuilder.toString()
    return finalString.ifEmpty { null }
}

fun Long.getDateString(formatString: String): String {
    return try {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.timeInMillis = this * 1000L
        DateFormat.format(formatString, calendar).toString()
    } catch (e: Exception) {
        Timber.e(e)
        ""
    }
}

fun Long.convertSingleDayMinutesCountToHourStringFormat(): String =
    floorDiv(60).toString() + ":" + rem(60).toString().let { minutes ->
        if (minutes.length == 1) {
            "0$minutes"
        } else {
            minutes
        }
    }