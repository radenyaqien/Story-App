package id.radenyaqien.storyapp.ui.storyscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import id.radenyaqien.storyapp.databinding.HomeFragmentBinding
import id.radenyaqien.storyapp.ui.loginscreen.LoginFragment
import id.radenyaqien.storyapp.util.launchAndCollectIn

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val viewmodel: ListViewModel by viewModels()
    private var _binding: HomeFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val navController by lazy { findNavController() }
    private val mAdapter: StoriesAdapter by lazy {
        StoriesAdapter {
            val action = HomeFragmentDirections.actionListFragmentToDetilStoryFragment()
                .setStories(it.id)
            findNavController().navigate(action)
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
        getSavedstate()
        setrv()

        viewmodel.currentUser.launchAndCollectIn(viewLifecycleOwner) {
            if (it.token.isEmpty()) {
                navController.navigate(HomeFragmentDirections.actionListFragmentToLoginFragment())

            } else {
                collectdata()
            }
        }


    }

    private fun getSavedstate() {

        val currentBackStackEntry = requireNotNull(navController.currentBackStackEntry)
        val savedStateHandle = currentBackStackEntry.savedStateHandle
        savedStateHandle.getLiveData<Boolean>(LoginFragment.LOGIN_SUCCESSFUL)
            .observe(currentBackStackEntry) { success ->
                if (!success) {
                    val startDestination = navController.graph.startDestinationId
                    val navOptions = NavOptions.Builder()
                        .setPopUpTo(startDestination, true)
                        .build()
                    navController.navigate(startDestination, null, navOptions)
                }
            }
    }

    private fun collectdata() {
        viewmodel.datastate.launchAndCollectIn(viewLifecycleOwner) {
            if (it.isloading) {

            }

            if (it.error != null) {

            }
            if (it.user.isNotEmpty()) {
                mAdapter.submitList(it.user)
            }
        }
    }

    private fun setrv() {
        binding.recyclerView.apply {
            setHasFixedSize(true)
            adapter = mAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
    }

}