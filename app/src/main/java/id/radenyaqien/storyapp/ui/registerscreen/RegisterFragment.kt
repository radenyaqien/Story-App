package id.radenyaqien.storyapp.ui.registerscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.radenyaqien.storyapp.databinding.RegisterFragmentBinding
import id.radenyaqien.storyapp.util.launchAndCollectIn

@AndroidEntryPoint
class RegisterFragment : Fragment(), View.OnClickListener {

    private val viewmodel: RegisterViewModel by viewModels()
    private var _binding: RegisterFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = RegisterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnRegister.setOnClickListener(this)
        collectState()
    }

    private fun collectState() {
        viewmodel.registerState.launchAndCollectIn(viewLifecycleOwner) {
            showLoading(it.isloading)
            showMessage(it.error)
            if (it.isRegisterSuccess) {
                navigateToLoginScreen()
            }
        }
    }

    private fun navigateToLoginScreen() {
        findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
    }

    private fun showMessage(error: String?) {
        error?.let {
            Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT)
                .show()
        }
        viewmodel.setMessage(null)
    }

    private fun showLoading(isloading: Boolean) {
        binding.progressCircular.isVisible = isloading
        binding.btnRegister.isEnabled = isloading.not()
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnRegister -> {
                viewmodel.register(
                    binding.tilUsername.editText?.text.toString(),
                    binding.tilEmail.editText?.text.toString(),
                    binding.tilPassword.editText?.text.toString()
                )
            }
        }
    }

}