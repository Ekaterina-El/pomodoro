package ka.el.pomodorotimer.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ka.el.pomodorotimer.R
import ka.el.pomodorotimer.databinding.FragmentSettingsBinding
import ka.el.pomodorotimer.viewModel.PomodoroSettingsViewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: PomodoroSettingsViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_settings, container, false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            settingsFragment = this@SettingsFragment
            viewModel = this@SettingsFragment.viewModel
        }

        return binding.root
    }

    fun goWork() {
        findNavController().navigate(R.id.action_settingsFragment_to_timerFragment)
    }

}