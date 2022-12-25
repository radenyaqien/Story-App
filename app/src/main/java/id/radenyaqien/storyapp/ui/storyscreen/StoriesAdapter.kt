package id.radenyaqien.storyapp.ui.storyscreen

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import id.radenyaqien.storyapp.databinding.ItemStoriesBinding
import id.radenyaqien.storyapp.domain.model.Stories

class StoriesAdapter(private val clickListener: (Stories, ImageView) -> Unit) :
    PagingDataAdapter<Stories, StoriesAdapter.StoriesViewHolder>(StoriesDiffCallback()) {

    inner class StoriesViewHolder(private val binding: ItemStoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                getItem(bindingAdapterPosition)?.let { it1 ->
                    clickListener.invoke(
                        it1,
                        binding.imgPhoto
                    )
                }
            }
        }

        fun onbind(stories: Stories, clickListener: (Stories, ImageView) -> Unit) {

            binding.model = stories
            binding.root.setOnClickListener {
                clickListener.invoke(stories, binding.imgPhoto)
            }
        }
    }

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
        getItem(position)?.let { holder.onbind(it, clickListener) }
    }
}