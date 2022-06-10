package id.radenyaqien.storyapp.ui.registerscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.radenyaqien.storyapp.databinding.RegisterFragmentBinding
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

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
        lifecycleScope.launchWhenStarted {
            viewmodel.registerState.onEach {
                if (it.isloading) {

                }
                if (!it.error.isNullOrBlank()) {
                    Snackbar.make(binding.root, it.error.toString(), Snackbar.LENGTH_SHORT)
                        .show()
                }

                if (it.user != null) {
                    findNavController().navigate(RegisterFragmentDirections.actionRegisterFragmentToLoginFragment())
                }

            }.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .launchIn(this)

        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnRegister -> {
                lifecycleScope.launchWhenStarted {
                    viewmodel.register(
                        binding.tilUsername.editText?.text.toString(),
                        binding.tilEmail.editText?.text.toString(),
                        binding.tilPassword.editText?.text.toString()
                    )
                }

            }
        }
    }

}