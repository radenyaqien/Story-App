package id.radenyaqien.storyapp.ui.loginscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.radenyaqien.storyapp.databinding.LoginFragmentBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginFragment : Fragment(), View.OnClickListener {

    private val viewModel: LoginViewModel by viewModels()
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)
    private lateinit var savedStateHandle: SavedStateHandle
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedStateHandle = findNavController().previousBackStackEntry!!.savedStateHandle
        savedStateHandle.set(LOGIN_SUCCESSFUL, false)
        binding.btnToregister.setOnClickListener(this)
        binding.btnSignin.setOnClickListener(this)
        lifecycleScope.launchWhenStarted {
            viewModel.loginState.onEach {
                if (it.error != null) {
                    Snackbar.make(binding.root, it.error.toString(), Snackbar.LENGTH_SHORT)
                        .show()
                }

                if (it.user != null) {

                    savedStateHandle.set(LOGIN_SUCCESSFUL, true)
                    findNavController().popBackStack()
                }
                if (it.isloading) {

                }
            }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .launchIn(this)

        }


    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnSignin -> {
                viewModel.login(
                    binding.tilUsername.editText?.text.toString(),
                    binding.tilPassword.editText?.text.toString()
                )
            }
            binding.btnToregister -> {
                findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
            }
        }

    }

    companion object {
        const val LOGIN_SUCCESSFUL: String = "LOGIN_SUCCESSFUL"
    }
}