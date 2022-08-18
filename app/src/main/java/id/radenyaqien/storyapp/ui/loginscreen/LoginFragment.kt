package id.radenyaqien.storyapp.ui.loginscreen

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import id.radenyaqien.storyapp.databinding.LoginFragmentBinding
import id.radenyaqien.storyapp.util.launchAndCollectIn


@AndroidEntryPoint
class LoginFragment : Fragment(), View.OnClickListener {

    private val viewModel: LoginViewModel by viewModels()
    private var _binding: LoginFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LoginFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnRegister.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        collectState()
        playAnimation()

    }

    private fun collectState() {
        viewModel.loginState.launchAndCollectIn(viewLifecycleOwner) {
            showMessage(it.error)
            checkLogin(it.isLoggedIn)
            showLoading(it.isloading)
        }
    }

    private fun playAnimation() {
        val email = ObjectAnimator.ofFloat(binding.tilEmail, View.ALPHA, 0f, 1f).setDuration(500)
        val password =
            ObjectAnimator.ofFloat(binding.tilPassword, View.ALPHA, 0f, 1f).setDuration(500)
        val btnlogin = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 0f, 1f).setDuration(500)
        val btnRegister =
            ObjectAnimator.ofFloat(binding.btnRegister, View.ALPHA, 0f, 1f)
                .setDuration(500)
        AnimatorSet().apply {
            playSequentially(email, password, btnlogin, btnRegister)
            startDelay = 200
            start()
        }
    }

    private fun showLoading(isloading: Boolean) {
        binding.progressCircular.isVisible = isloading
        binding.btnLogin.isEnabled = !isloading
    }

    private fun showMessage(error: String?) {
        error?.let {
            Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
            viewModel.setMessage(null)
        }

    }

    private fun navigateToRegisterScreen() {
        findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToRegisterFragment())
    }

    private fun checkLogin(isLoginSuccess: Boolean) {
        if (isLoginSuccess) {
            findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToListFragment())
        }
    }

    override fun onClick(p0: View?) {
        when (p0) {
            binding.btnLogin -> {
                viewModel.login(
                    binding.tilEmail.editText?.text.toString(),
                    binding.tilPassword.editText?.text.toString()
                )
            }
            binding.btnRegister -> {
                navigateToRegisterScreen()
            }
        }
    }

}