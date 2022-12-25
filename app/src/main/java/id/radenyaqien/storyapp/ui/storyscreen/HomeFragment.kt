package id.radenyaqien.storyapp.ui.storyscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigator
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.radenyaqien.storyapp.databinding.HomeFragmentBinding

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewmodel: HomeViewModel by viewModels()
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val mAdapter: StoriesAdapter by lazy {
        StoriesAdapter { stories, imgView ->
            val extras = FragmentNavigatorExtras(Pair(imgView, stories.id))
            val action =
                HomeFragmentDirections.actionListFragmentToDetilStoryFragment(stories)
            navigate(action, extras)
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

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(
            RESULT_ADD_STORIES
        )?.observe(viewLifecycleOwner) { isAddSuccessfully ->
            if (isAddSuccessfully) {
                mAdapter.refresh()
            }
        }
        setupView()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {

            viewmodel.data.flowWithLifecycle(viewLifecycleOwner.lifecycle).collect {
                mAdapter.submitData(viewLifecycleOwner.lifecycle, it)
                binding.shimmerLoading.isVisible = false
            }
        }

    }

    private fun setupView() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        binding.btnToAddStory.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections
                    .actionListFragmentToAddStoryFragment()
            )

        }
    }

    private fun navigate(destination: NavDirections, extraInfo: FragmentNavigator.Extras) =
        with(findNavController()) {
            // 1
            currentDestination?.getAction(destination.actionId)
                ?.let {
                    navigate(destination, extraInfo) //2 }
                }
        }

    companion object {
        const val RESULT_ADD_STORIES = "jkdsbfjhsdbfjhsdbjkf"
    }

}