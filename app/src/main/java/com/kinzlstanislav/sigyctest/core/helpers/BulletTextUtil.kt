@file:Suppress("unused")

package com.kinzlstanislav.sigyctest.core.helpers

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BulletSpan

object BulletTextUtil {
    /**
     * Returns a CharSequence containing a bulleted and properly indented list.
     *
     * @param leadingMargin In pixels, the space between the left edge of the bullet and the left edge of the text.
     * @param context
     * @param stringArrayResId A resource id pointing to a string array. Each string will be a separate line/bullet-point.
     * @return
     */
    fun makeBulletListFromStringArrayResource(leadingMargin: Int, context: Context, stringArrayResId: Int): CharSequence {
        return makeBulletList(leadingMargin, context.resources.getStringArray(stringArrayResId))
    }

    /**
     * Returns a CharSequence containing a bulleted and properly indented list.
     *
     * @param leadingMargin In pixels, the space between the left edge of the bullet and the left edge of the text.
     * @param context
     * @param linesResIds An array of string resource ids. Each string will be a separate line/bullet-point.
     * @return
     */
    fun makeBulletListFromStringResources(leadingMargin: Int, context: Context, vararg linesResIds: Int): CharSequence {
        val len = linesResIds.size
        val csLines = mutableListOf<String>()
        for (i in 0 until len) {
            csLines.add(context.getString(linesResIds[i]))
        }
        return makeBulletList(leadingMargin, csLines.toTypedArray())
    }

    /**
     * Returns a CharSequence containing a bulleted and properly indented list.
     *
     * @param leadingMargin In pixels, the space between the left edge of the bullet and the left edge of the text.
     * @param lines An array of CharSequences. Each CharSequences will be a separate line/bullet-point.
     * @return
     */
    fun makeBulletList(leadingMargin: Int, lines: Array<String>): CharSequence {
        val sb = SpannableStringBuilder()
        for (i in lines.indices) {
            val line: CharSequence = lines[i] + if (i < lines.size - 1) "\n" else ""
            val spannable: Spannable = SpannableString(line)
            spannable.setSpan(BulletSpan(leadingMargin), 0, spannable.length, Spanned.SPAN_INCLUSIVE_EXCLUSIVE)
            sb.append(spannable)
        }
        return sb
    }
}