package id.radenyaqien.storyapp.ui.mapscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.ktx.addMarker
import dagger.hilt.android.AndroidEntryPoint
import id.radenyaqien.storyapp.R
import id.radenyaqien.storyapp.databinding.FragmentMapsBinding
import id.radenyaqien.storyapp.domain.model.Stories

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {
    private val boundsBuilder = LatLngBounds.Builder()
    private lateinit var mMap: GoogleMap
    private var _binding: FragmentMapsBinding? = null
    private val binding get() = requireNotNull(_binding)
    private val viewModel: MapsViewModel by viewModels()
    private val bounds: LatLngBounds by lazy { boundsBuilder.build() }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        viewModel.getMapStorys()
        super.onStart()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.maps_fragment) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun collect() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.state.flowWithLifecycle(viewLifecycleOwner.lifecycle)
                .collect { state ->
                    setData(state.data)
                    setmsg(state.msg)
                }
        }
    }

    private fun setmsg(msg: String?) {
        msg?.let {
            Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
        }

    }

    private fun setData(data: List<Stories>) {
        if (data.isNotEmpty()) {
            mMap.clear()
            data.forEach {
                val latLng = LatLng(it.lat ?: 0.0, it.lon ?: 0.0)
                mMap.addMarker {
                    position(latLng)
                    title(it.name)
                    snippet(it.description)
                }
                boundsBuilder.include(latLng)
            }

            mMap.animateCamera(
                CameraUpdateFactory.newLatLngBounds(
                    bounds,
                    resources.displayMetrics.widthPixels,
                    resources.displayMetrics.heightPixels,
                    300
                )
            )
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
        collect()

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.isCompassEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
    }

}