package id.radenyaqien.storyapp.ui.addstoryscreen

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.FileProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import id.radenyaqien.storyapp.BuildConfig
import id.radenyaqien.storyapp.databinding.AddStoryFragmentBinding
import id.radenyaqien.storyapp.ui.storyscreen.HomeFragment
import id.radenyaqien.storyapp.util.getFile
import id.radenyaqien.storyapp.util.launchAndCollectIn
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

private const val MAX_UPLOAD_SIZE = 1 * 1024 * 1024

@AndroidEntryPoint
class AddStoryFragment : Fragment(), View.OnClickListener {
    private val viewmodel: AddStoryViewModel by viewModels()
    private var _binding: AddStoryFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { uri ->
            viewmodel.fotoUri = uri
            setImage(uri)

        }
    }
    private val fotoLauncher = registerForActivityResult(
        ActivityResultContracts
            .TakePicture()
    ) { result ->
        if (result) {
            viewmodel.fotoUri?.let { uri ->
                setImage(uri)
            }
        } else {
            Snackbar.make(binding.imageView, "gagal mengambil gambar", Snackbar.LENGTH_SHORT).show()
        }
    }
    private val permLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            if (it) {
                openGallery()
            } else {
                Snackbar.make(
                    binding.root,
                    "berikan izin untuk mengambil gambar",
                    Snackbar.LENGTH_SHORT
                )
                    .show()
            }
        }

    private val cameraPerm =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                startTakePhoto()
            } else {
                shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddStoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            btnCamera.setOnClickListener(this@AddStoryFragment)
            btnSubmit.setOnClickListener(this@AddStoryFragment)
            btnGallery.setOnClickListener(this@AddStoryFragment)
        }
        viewmodel.state.launchAndCollectIn(viewLifecycleOwner) {
            showLoading(it.isLoading)
            onAddStoryResult(it.isSuccess)
            setMessage(it.errorMessage)
        }

    }

    private fun setMessage(errorMessage: String?) {
        errorMessage?.let {
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            viewmodel.setMessage(null)
        }
    }

    private fun onAddStoryResult(isSuccess: Boolean) {
        if (isSuccess) {
            findNavController().previousBackStackEntry?.savedStateHandle?.set(
                HomeFragment.RESULT_ADD_STORIES,
                true
            )
            Snackbar.make(binding.btnSubmit, "Berhasil Menambah Story", Snackbar.LENGTH_SHORT)
                .show()
            findNavController().popBackStack()
        }
    }

    private fun showLoading(loading: Boolean) {
        binding.progressCircular.isVisible = loading
        binding.btnSubmit.isEnabled = !loading
    }


    private fun startTakePhoto() {
        lifecycleScope.launchWhenStarted {
            getTmpFileUri().let { uri ->
                viewmodel.fotoUri = uri
                fotoLauncher.launch(uri)
            }
        }
    }

    private fun openGallery() {

        galleryLauncher.launch("image/*")

    }

    private fun getTmpFileUri(): Uri {
        val tmpFile =
            File.createTempFile("tmp_image_file", ".png", requireContext().cacheDir).apply {
                createNewFile()
                deleteOnExit()
            }
        return FileProvider.getUriForFile(
            requireContext(),
            "${BuildConfig.APPLICATION_ID}.fileprovider",
            tmpFile
        )
    }

    private fun setImage(uri: Uri?) {
        binding.imageView.setImageURI(uri)
    }

    private fun isTooLargeFile(filesize: Long) = filesize > (MAX_UPLOAD_SIZE)

    override fun onClick(v: View?) {
        when (v) {
            binding.btnCamera -> {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    cameraPerm.launch(
                        Manifest.permission.CAMERA
                    )
                } else {
                    startTakePhoto()
                }

            }
            binding.btnGallery -> {
                if (ActivityCompat.checkSelfPermission(
                        requireContext(),
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    permLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                    )
                } else {
                    openGallery()
                }
            }
            binding.btnSubmit -> {
                val file = getFile(requireContext(), viewmodel.fotoUri)
                if (file == null) {
                    viewmodel.setMessage("file tidak di temukan")
                    return
                }
                if (isTooLargeFile(file.length())) {
                    viewmodel.setMessage("maximal ukuran file 1mb")
                } else {
                    viewmodel.addStory(
                        binding.tilDesciption.editText?.text?.toString()
                            ?.toRequestBody(MultipartBody.FORM),
                        file
                    )
                }

            }
        }
    }

}