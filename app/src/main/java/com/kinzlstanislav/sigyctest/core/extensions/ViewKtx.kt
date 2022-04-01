@file:Suppress("unused")

package com.kinzlstanislav.sigyctest.core.extensions

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.*
import android.text.TextUtils.TruncateAt
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.isGone
import androidx.core.view.updateLayoutParams
import androidx.core.widget.ImageViewCompat
import androidx.vectordrawable.graphics.drawable.Animatable2Compat
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat

fun View.animateHeight(
    newHeight: Int
) {
    val animator = ValueAnimator.ofInt(this.height, newHeight)
    animator.duration = 500
    animator.addUpdateListener {
        this.layoutParams.height = it.animatedValue as Int
        this.requestLayout()
    }
    animator.start()
}

/**
 * Provides access to the hidden ViewGroup#suppressLayout method.
 */
fun ViewGroup.suppressLayoutCompat(suppress: Boolean) {
    if (Build.VERSION.SDK_INT >= 29) {
        suppressLayout(suppress)
    } else {
        hiddenSuppressLayout(this, suppress)
    }
}

/**
 * False when linking of the hidden suppressLayout method has previously failed.
 */
private var tryHiddenSuppressLayout = true

@SuppressLint("NewApi") // Lint doesn't know about the hidden method.
private fun hiddenSuppressLayout(group: ViewGroup, suppress: Boolean) {
    if (tryHiddenSuppressLayout) {
        // Since this was an @hide method made public, we can link directly against it with
        // a try/catch for its absence instead of doing the same through reflection.
        try {
            group.suppressLayout(suppress)
        } catch (e: NoSuchMethodError) {
            tryHiddenSuppressLayout = false
        }
    }
}

fun TextView.setOnImeDoneClickListener(onClick: () -> Unit) {
    setOnEditorActionListener { _, _, event ->
        // If triggered by an enter key, this is the event; otherwise, this is null.
        if (event != null) {
            // if shift key is down, then we want to insert the '\n' char in the TextView;
            // otherwise, the default action is to send the message.
            if (!event.isShiftPressed) {
                onClick.invoke()
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        onClick.invoke()
        return@setOnEditorActionListener true
    }
}

fun TextView.setTextOrGoneIfEmpty(text: String) {
    isGone = if (text.isEmpty()) true else {
        setText(text)
        false
    }
}

fun <V : View> View.findViewByIdOrNull(@IdRes id: Int?): V? = id?.let {
    findViewById(it)
}

fun TextView.makeLink(
    link: String,
    @ColorRes linkColor: Int,
    bold: Boolean = false,
    action: () -> Unit
) {
    val spannableString = SpannableString(this.text)
    val clickableSpan = object : ClickableSpan() {

        override fun updateDrawState(textPaint: TextPaint) {
            // use this to change the link color
            textPaint.color = ContextCompat.getColor(context, linkColor)
            // toggle below value to enable/disable
            // the underline shown below the clickable text
            textPaint.isUnderlineText = true
        }

        override fun onClick(view: View) {
            Selection.setSelection((view as TextView).text as Spannable, 0)
            view.invalidate()
            action()
        }
    }
    val startIndexOfLink = this.text.toString().indexOf(link)
    spannableString.setSpan(
        clickableSpan, startIndexOfLink, startIndexOfLink + link.length,
        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    if (bold) {
        spannableString.setSpan(
            StyleSpan(Typeface.BOLD), startIndexOfLink,
            startIndexOfLink + link.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
    }

    this.movementMethod =
        LinkMovementMethod.getInstance() // without LinkMovementMethod, link can not click
    this.setText(spannableString, TextView.BufferType.SPANNABLE)
}

@Suppress("UNCHECKED_CAST")
fun <T : View> applyToViews(vararg views: View?, appliedCode: T.() -> Unit) {
    views.forEach {
        it?.apply {
            appliedCode(it as? T ?: return@apply)
        }
    }
}

fun ImageView.setTint(color: Int) {
    ImageViewCompat.setImageTintList(this, ColorStateList.valueOf(color))
}

fun View.requireActivity(): Activity {
    var context: Context = context
    while (context is ContextWrapper) {
        if (context is Activity) {
            return context
        }
        context = context.baseContext
    }
    throw IllegalArgumentException("No activity attached to view")
}

fun View.disableTouchGestures() {
    requireActivity().window.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}

fun View.enableTouchGestures() {
    requireActivity().window.clearFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}

/**
 *
 * */
fun <T : View> View.bindView(@IdRes idRes: Int): Lazy<T> {
    return lazy {
        findViewById(idRes)
    }
}

fun <T : View> Activity.bindView(@IdRes idRes: Int): Lazy<T> {
    return lazy {
        findViewById(idRes)
    }
}

fun View.fadeIn(dur: Int = 300) {
    animate().alphaBy(1f - alpha).duration = dur.toLong()
}

fun View.fadeOut(dur: Int = 300, fadeOutBy: Float = -1f) {
    animate().alphaBy(fadeOutBy).duration = dur.toLong()
}

fun View.margin(left: Int? = null, top: Int? = null, right: Int? = null, bottom: Int? = null) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        left?.run { leftMargin = this.dp }
        top?.run { topMargin = this.dp }
        right?.run { rightMargin = this.dp }
        bottom?.run { bottomMargin = this.dp }
    }
}

fun TextView.isEllipsized(): Boolean {
    val truncateAt = ellipsize
    if (truncateAt == null || TruncateAt.MARQUEE == truncateAt) {
        return false
    }
    val layout = layout ?: return false
    for (line in 0 until layout.lineCount) {
        if (layout.getEllipsisCount(line) > 0) {
            return true
        }
    }
    return false
}

fun <V : View> V.safeRunBlock(block: (V) -> Unit) {
    if (ViewCompat.isLaidOut(this)) {
        block(this)
    } else {
        this.post {
            block(this)
        }
    }

}

inline fun <reified T : ViewGroup.LayoutParams> View.layoutParams(block: T.() -> Unit) {
    if (layoutParams is T) block(layoutParams as T)
}

fun View.getShortAnimTime200() =
    context.resources.getInteger(android.R.integer.config_shortAnimTime)

fun View.getMediumAnimTime400() =
    context.resources.getInteger(android.R.integer.config_mediumAnimTime)

fun View.getLongAnimTime500() = context.resources.getInteger(android.R.integer.config_longAnimTime)

fun View.startAnimatedVectorDrawable(
    @DrawableRes animatedVectorDrawableRes: Int,
    loop: Boolean = true
) {
    val animatedLoading = AnimatedVectorDrawableCompat.create(context, animatedVectorDrawableRes)
    if (loop) {
        animatedLoading?.registerAnimationCallback(object : Animatable2Compat.AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                this@startAnimatedVectorDrawable.post { animatedLoading.start() }
            }
        })
    }
    background = animatedLoading
    animatedLoading?.start()
}

fun View.getCenterX(): Float = x + (measuredWidth / 2)