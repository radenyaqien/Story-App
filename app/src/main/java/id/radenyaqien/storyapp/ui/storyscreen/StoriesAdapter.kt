package id.radenyaqien.storyapp.ui.storyscreen

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.radenyaqien.storyapp.databinding.ItemStoriesBinding
import id.radenyaqien.storyapp.domain.model.Stories

class StoriesAdapter(private val clickListener: (Stories) -> Unit) :
    ListAdapter<Stories, StoriesAdapter.StoriesViewHolder>(StoriesDiffCallback()) {


    class StoriesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = ItemStoriesBinding.bind(view)
        fun onbind(stories: Stories, onClickListener: View.OnClickListener) {
            binding.root.setOnClickListener(onClickListener)
            binding.model = stories
        }
    }

    //create diff callback
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        return StoriesViewHolder(
            ItemStoriesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ).root
        )
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        holder.onbind(getItem(position)) {
            clickListener(getItem(position))
        }


    }
}