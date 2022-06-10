package id.radenyaqien.storyapp.ui.detilscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.AndroidEntryPoint
import id.radenyaqien.storyapp.databinding.DetilStoryFragmentBinding
import id.radenyaqien.storyapp.domain.model.Stories

@AndroidEntryPoint
class DetilStoryFragment : Fragment() {
    private lateinit var savedStateHandle: SavedStateHandle
    private val viemodel: DetilStoryViewModel by viewModels()
    private var _binding: DetilStoryFragmentBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = DetilStoryFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedStateHandle = SavedStateHandle()

        val story = arguments?.getParcelable<Stories>("Stories")
    }
}