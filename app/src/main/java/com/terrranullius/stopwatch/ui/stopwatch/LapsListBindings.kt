package com.terrranullius.stopwatch.ui.stopwatch

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.terrranullius.stopwatch.R
import com.terrranullius.stopwatch.other.StopwatchUtility


@BindingAdapter("itemPosition")
fun setItemPositionText(textView: TextView, pos: Int){
    textView.text = pos.toString()
}

@BindingAdapter("lapTime")
fun setLapTime(textView: TextView, lapTime: Long){
    textView.text = textView.context.getString(R.string.lapTimeFormatted, StopwatchUtility.getFormattedLapTimeString(lapTime))
}

@BindingAdapter("timeStamp")
fun setTimeStamp(textView: TextView, timeStamp: Long){
    textView.text = StopwatchUtility.getFormattedLapTimeString(timeStamp)
}