package id.radenyaqien.storyapp.ui.storyscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import id.radenyaqien.storyapp.databinding.ItemStoriesBinding
import id.radenyaqien.storyapp.domain.model.Stories

class StoriesAdapter(private val clickListener: (Stories, ImageView) -> Unit) :
    ListAdapter<Stories, StoriesAdapter.StoriesViewHolder>(StoriesDiffCallback()) {

    class StoriesViewHolder(private val binding: ItemStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onbind(stories: Stories, clickListener: (Stories, ImageView) -> Unit) {
            binding.root.setOnClickListener {
                clickListener.invoke(stories, binding.imgPhoto)
            }
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
            )
        )
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        holder.onbind(getItem(position), clickListener)
    }
}