package id.radenyaqien.storyapp.ui.addstoryscreen

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import id.radenyaqien.storyapp.databinding.AddStoryFragmentBinding
import id.radenyaqien.storyapp.util.launchAndCollectIn

class AddStoryFragment : Fragment() {
    companion object {

        private val REQUIRED_PERMISSIONS = Manifest.permission.CAMERA

    }

    private val viewmodel: AddStoryViewModel by viewModels()
    private var _binding: AddStoryFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)
    private lateinit var currentPhotoPath: String
    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val imageBitmap = it.data?.extras?.get("data") as Bitmap

        }
    }

    private val permissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                Toast.makeText(
                    requireContext(),
                    "Permission Granted",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Permission Denied",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AddStoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun allPermissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            REQUIRED_PERMISSIONS
        ) == PackageManager.PERMISSION_GRANTED


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!allPermissionsGranted()) {
            permissionLauncher.launch(REQUIRED_PERMISSIONS)
        }

        viewmodel.state.launchAndCollectIn(viewLifecycleOwner) {
            if (it.isLoading) {

            }
            if (it.isSuccess) {
                findNavController().navigate(AddStoryFragmentDirections.actionAddStoryFragmentToListFragment())
            }
            if (it.errorMessage != null) {
                Toast.makeText(requireContext(), it.errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun startTakePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(requireActivity().packageManager)
        java.io.File.createTempFile("IMG-", "jpg", requireActivity().cacheDir).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                requireActivity(),
                "id.radenyaqien.storyapp.fileprovider",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)
        }

    }

}