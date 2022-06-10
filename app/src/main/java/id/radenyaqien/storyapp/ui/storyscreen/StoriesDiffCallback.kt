package id.radenyaqien.storyapp.ui.storyscreen

import androidx.recyclerview.widget.DiffUtil
import id.radenyaqien.storyapp.domain.model.Stories

class StoriesDiffCallback : DiffUtil.ItemCallback<Stories>() {
    override fun areItemsTheSame(oldItem: Stories, newItem: Stories): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Stories, newItem: Stories): Boolean {
        return oldItem == newItem
    }


}