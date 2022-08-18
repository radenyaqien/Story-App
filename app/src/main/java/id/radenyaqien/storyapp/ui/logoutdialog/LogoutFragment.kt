package id.radenyaqien.storyapp.ui.logoutdialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import id.radenyaqien.storyapp.databinding.FragmentLogoutBinding
import id.radenyaqien.storyapp.domain.model.User
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class LogoutFragment : DialogFragment() {
    private val viewModel: LogoutViewModel by viewModels()
    private var _binding: FragmentLogoutBinding? = null
    private val binding get() = requireNotNull(_binding)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLogoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.user.flowWithLifecycle(viewLifecycleOwner.lifecycle).collectLatest {
                checkUser(it)
            }
        }

        binding.btnLogout.setOnClickListener {
            viewModel.logout()
        }
        binding.btnCancel.setOnClickListener {
            dismiss()
        }

    }

    private fun checkUser(user: User?) {
        val isNotLoggedIn = user == null
        if (isNotLoggedIn) {
            navigateToMain()
        }
    }

    private fun navigateToMain() {
        findNavController().navigate(LogoutFragmentDirections.actionLogoutFragmentToLoginFragment())
    }

}