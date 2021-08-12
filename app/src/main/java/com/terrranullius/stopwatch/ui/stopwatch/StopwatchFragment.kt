package com.terrranullius.stopwatch.ui.stopwatch

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.terrranullius.stopwatch.R
import com.terrranullius.stopwatch.databinding.StopwatchFragmentBinding
import com.terrranullius.stopwatch.db.Lap
import com.terrranullius.stopwatch.other.Constants.ACTION_RESET_LAP_STOPWATCH
import com.terrranullius.stopwatch.other.Constants.ACTION_START_PAUSE_STOPWATCH
import com.terrranullius.stopwatch.other.StopwatchUtility
import com.terrranullius.stopwatch.other.animateSlideUp
import com.terrranullius.stopwatch.services.StopwatchService
import com.terrranullius.stopwatch.ui.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class StopwatchFragment : Fragment() {

    private val viewModel: MainViewModel by viewModels()

    private var lastSecUnits = 0L
    private var lastSecTens = 0L
    private var lastMinUnits = 0L
    private var lastMinTens = 0L
    private var lastHour = 0L

    private var lastSeconds = 0L

    private var secondsUnit = 0L
    private var secondsTens = 0L
    private var minutesUnit = 0L
    private var minutesTens = 0L
    private var hours = 0L

    private var currentMillis = 0L

    private lateinit var lapsAdapter: LapsAdapter

    private lateinit var binding: StopwatchFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.stopwatch_fragment,
            container, false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setObservers()
        setClickListeners()

        lapsAdapter = LapsAdapter()

        binding.lapsRecyclerView.adapter = lapsAdapter


    }

    private fun setObservers() {

        StopwatchService.isStopwatchRunning.observe(viewLifecycleOwner, Observer {

            when {
                StopwatchService.isFirstStart.value!! -> {
                    binding.startPauseButton.text = getString(R.string.stopwatch_start_button_start)
                }
                it -> {
                    binding.startPauseButton.text = getString(R.string.stopwatch_start_button_pause)
                    binding.lapResetButton.text = getString(R.string.stopwatch_reset_button_lap)
                }
                else -> {
                    binding.startPauseButton.text =
                        getString(R.string.stopwatch_start_button_resume)
                    binding.lapResetButton.text = getString(R.string.stopwatch_reset_buttn_reset)
                }
            }
        })

        StopwatchService.isReset.observe(viewLifecycleOwner, Observer {
            if (it) resetStopwatch()
        })

        StopwatchService.totalTimeRunInMillis.observe(viewLifecycleOwner, Observer {

            updateStopwatch(it)
        })

        StopwatchService.lastLap.observe(viewLifecycleOwner, Observer {

            binding.lastlap.text = StopwatchUtility.getFormattedLapTimeString(it)

            if (it != 0L)
                viewModel.insertLap(Lap(it, currentMillis))

        })


        viewModel.getAllLaps().observe(viewLifecycleOwner, Observer {

            lapsAdapter.submitList(it)

        })

    }

    private fun updateStopwatch(ms: Long) {

        var millis = ms

        currentMillis = ms

        hours = TimeUnit.MILLISECONDS.toHours(millis)
        millis -= TimeUnit.HOURS.toMillis(hours)
        val minutes = TimeUnit.MILLISECONDS.toMinutes(millis)
        millis -= TimeUnit.MINUTES.toMillis(minutes)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(millis)

        millis -= TimeUnit.SECONDS.toMillis(seconds)

        secondsUnit = (seconds % 10)
        secondsTens = (seconds / 10)
        minutesUnit = (minutes % 10)
        minutesTens = (minutes / 10)


        if (secondsUnit > lastSecUnits || (secondsUnit == 0L && lastSecUnits == 9L)) binding.secUnit.animateSlideUp(
            secondsUnit
        )
        if (secondsTens > lastSecTens || (secondsTens == 0L && lastSecTens == 5L)) binding.secTens.animateSlideUp(
            secondsTens
        )
        if (minutesUnit > lastMinUnits || (minutesUnit == 0L && lastMinUnits == 9L)) binding.minUnit.animateSlideUp(
            minutesUnit
        )
        if (minutesTens > lastMinTens || (minutesTens == 0L && lastMinTens == 5L)) binding.minTens.animateSlideUp(
            minutesTens
        )
        if (hours > lastHour) binding.hours.animateSlideUp(hours)

        lastSeconds = seconds

        lastSecUnits = secondsUnit
        lastSecTens = secondsTens
        lastMinUnits = minutesUnit
        lastMinTens = minutesTens
        lastHour = hours

        binding.nanosec.text = String.format("%02d", millis % 100)
        binding.microsec.text = (millis / 100).toString()
    }


    private fun setClickListeners() {
        binding.startPauseButton.setOnClickListener {
            sendCommandToService(ACTION_START_PAUSE_STOPWATCH)
        }

        binding.lapResetButton.setOnClickListener {
            sendCommandToService(ACTION_RESET_LAP_STOPWATCH)
        }
    }

    private fun sendCommandToService(action: String) {
        Intent(requireContext(), StopwatchService::class.java).also {
            it.action = action
            requireContext().startService(it)
        }
    }

    private fun resetStopwatch() {

        viewModel.deleteAll()

        resetTextViewsIfGreaterThanZero(
            binding.secUnit,
            binding.secTens,
            binding.minUnit,
            binding.minTens,
            binding.hours
        )

        binding.lastlap.text = StopwatchUtility.getFormattedStopwatchNotificationString(0L)

    }

    private fun resetTextViewsIfGreaterThanZero(vararg views: TextView) {

        views.forEach {

            if (Integer.parseInt(it.text as String) > 0) it.animateSlideUp(0L)

        }
    }

}

