package com.terrranullius.stopwatch.other

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.terrranullius.stopwatch.R
import java.util.concurrent.TimeUnit

fun TextView.animateSlideUp(value: Long) {

    val animation = AnimationUtils.loadAnimation(context, R.anim.slideup)
    startAnimation(animation)

    val anim2 = AnimationUtils.loadAnimation(context, R.anim.slideup2)

    animation.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {

        }

        override fun onAnimationEnd(animation: Animation?) {
            text = value.toString()
            startAnimation(anim2)
        }

        override fun onAnimationStart(animation: Animation?) {

        }
    })
}

object StopwatchUtility {

    fun getFormattedStopwatchNotificationString(
        secs: Long,
        includeMillis: Boolean = false
    ): String {

        var seconds = secs

        val hours = TimeUnit.SECONDS.toHours(seconds)
        seconds -= TimeUnit.HOURS.toSeconds(hours)

        val minutes = TimeUnit.SECONDS.toMinutes(seconds)
        seconds -= TimeUnit.MINUTES.toSeconds(minutes)

        return "${if (hours < 10) "0" else ""}$hours:" +
                "${if (minutes < 10) "0" else ""}$minutes:" +
                "${if (seconds < 10) "0" else ""}$seconds"
    }

    fun getFormattedLapTimeString(ms: Long): String {

        var millis = ms

        val hours = TimeUnit.MILLISECONDS.toHours(millis)
        millis -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        millis -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis)
        millis -= TimeUnit.SECONDS.toMillis(seconds)

        val centis = millis / 10

        return if (hours > 0) "$hours:$minutes:$seconds.$centis" else if (minutes > 0) "$minutes:$seconds.$centis" else "$seconds.$centis"

    }

}