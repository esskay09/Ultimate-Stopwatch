package com.terrranullius.stopwatch.ui.stopwatch

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.terrranullius.stopwatch.R
import com.terrranullius.stopwatch.databinding.StopwatchLapItemBinding
import com.terrranullius.stopwatch.db.Lap

class LapsAdapter : ListAdapter<Lap, LapsAdapter.ViewHolder>(LapDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = getItem(position)
        holder.bind(item, position)
    }


    class ViewHolder private constructor(val binding: StopwatchLapItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {

            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding: StopwatchLapItemBinding = DataBindingUtil.inflate(
                    layoutInflater,
                    R.layout.stopwatch_lap_item, parent, false
                )

                return ViewHolder(binding)
            }
        }

        fun bind(item: Lap, position: Int) {
            binding.lap = item
            binding.positionVariable = position
            binding.executePendingBindings()
        }
    }

}

class LapDiffCallback : DiffUtil.ItemCallback<Lap>() {

    override fun areItemsTheSame(oldItem: Lap, newItem: Lap): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Lap, newItem: Lap): Boolean {
        return oldItem == newItem
    }
}