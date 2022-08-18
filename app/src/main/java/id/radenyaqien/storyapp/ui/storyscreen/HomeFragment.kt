package id.radenyaqien.storyapp.ui.storyscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.radenyaqien.storyapp.databinding.HomeFragmentBinding
import id.radenyaqien.storyapp.domain.model.Stories
import id.radenyaqien.storyapp.util.launchAndCollectIn

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewmodel: HomeViewModel by viewModels()
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val mAdapter: StoriesAdapter by lazy {
        StoriesAdapter { stories, imgView ->
            val extras = FragmentNavigatorExtras(imgView to "image_small")
            val action =
                HomeFragmentDirections.actionListFragmentToDetilStoryFragment(stories)
            findNavController().navigate(action, extras)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenResult()
        setupView()
        collectState()
    }

    private fun listenResult() {
        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            RESULT_ADD_STORIES
        )
            ?.observe(
                viewLifecycleOwner
            ) { result ->
                if (result) {
                    viewmodel.stories()
                }
            }
    }

    private fun collectState() {
        viewmodel.datastate.launchAndCollectIn(viewLifecycleOwner) {
            showLoading(it.isLoading)
            showMessage(it.error)
            showData(it.listData)
        }
    }

    private fun showLoading(isloading: Boolean) {
        binding.shimmerLoading.isVisible = isloading
        binding.recyclerView.isVisible = !isloading
    }

    private fun showData(user: List<Stories>) {

        mAdapter.submitList(user)

    }

    private fun showMessage(str: String?) {
        str?.let {
            Snackbar.make(binding.recyclerView, str.toString(), Snackbar.LENGTH_SHORT).show()
            viewmodel.setMessage(null)
        }

    }

    private fun setupView() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.btnToAddStory.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionListFragmentToAddStoryFragment())
        }
    }

    companion object {
        const val RESULT_ADD_STORIES = "jkdsbfjhsdbfjhsdbjkf"
    }

}