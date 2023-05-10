package wook.example.protodatastorestudy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import wook.example.protodatastorestudy.databinding.FragmentProtoDatstoreBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class ProtoDatastoreFragment : Fragment() {

    private var _binding: FragmentProtoDatstoreBinding? = null

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    private lateinit var protoSettingsManager: ProtoSettingsManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProtoDatstoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        protoSettingsManager = ProtoSettingsManager(requireContext())
        binding.buttonBlack.setOnClickListener {
            updateSettings(UserSettings.BgColor.COLOR_BLACK, "#000000")
        }
        binding.buttonWhite.setOnClickListener {
            updateSettings(UserSettings.BgColor.COLOR_WHITE, "#FFFFFF")
        }
        readSettings()
    }

    private fun updateSettings(bgColor: UserSettings.BgColor, colorText: String) {
        lifecycleScope.launch {
            protoSettingsManager.updateUserSettings(bgColor, colorText)
        }
    }

    private fun readSettings() {
        lifecycleScope.launch {
            protoSettingsManager.userSettingsFlow.collect {
                binding.textviewColor.text = it.colorText
                binding.layoutParent.setBackgroundColor(
                    ContextCompat.getColor(
                        requireContext(),
                        when (it.bgColor) {
                            UserSettings.BgColor.COLOR_WHITE -> android.R.color.white
                            UserSettings.BgColor.COLOR_BLACK -> android.R.color.black
                            else -> throw IllegalStateException()
                        }
                    )
                )
            }
        }
    }
}