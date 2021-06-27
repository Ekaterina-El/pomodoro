package ka.el.pomodorotimer.ui

import android.media.MediaPlayer
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import ka.el.pomodorotimer.R
import ka.el.pomodorotimer.databinding.FragmentTimerBinding
import ka.el.pomodorotimer.viewModel.PomodoroSettingsViewModel
import ka.el.pomodorotimer.viewModel.TimerViewModel

class TimerFragment : Fragment() {
    private lateinit var binding: FragmentTimerBinding

    private val settingsViewModel: PomodoroSettingsViewModel by activityViewModels()
    private val viewModel: TimerViewModel by viewModels()

    private var doneMediaPlayer: MediaPlayer? = null
    private var tickMediaPlayer: MediaPlayer? = null

    private var timer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_timer, container, false)


        binding.apply {
            lifecycleOwner = viewLifecycleOwner
            timerFragment = this@TimerFragment
            viewModel = this@TimerFragment.viewModel
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.initTimer(
            settingsViewModel.currentPomodoro.value!! * 60,
            settingsViewModel.currentShortBreak.value!! * 60,
            settingsViewModel.currentLongBreak.value!! * 60
        )
        setPomodoros()
        loadSounds()
        createTimer()
    }


    private fun setPomodoros() {
        val pomodoros = 4  // temp
        viewModel.setAllPomodoros(pomodoros)
    }

    private fun loadSounds() {
        loadDoneSound()
        loadTickSound()
    }

    private fun loadTickSound() {
        tickMediaPlayer = MediaPlayer.create(context, R.raw.tick)
    }

    private fun loadDoneSound() {
        doneMediaPlayer = MediaPlayer.create(context, R.raw.done)
    }

    private fun createTimer() {
        val milisLnFuture = viewModel.lastTimerSeconds.value!! * 1000L

        timer?.cancel()

        timer = object : CountDownTimer(milisLnFuture, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                viewModel.updateTimer((millisUntilFinished / 1000).toInt())

                if (viewModel.hasTick.value == true) {
                    tickMediaPlayer?.start()
                }
            }

            override fun onFinish() {
                viewModel.stopTimer()
                doneMediaPlayer?.start()

                // start again if it isn`t end
                if (viewModel.goNextStage() != getString(R.string.end_work)) {
                    createTimer()
                }
            }

        }
        (timer as CountDownTimer).start()
        viewModel.startTimer()
    }

    fun toggleTimer() {
        if (viewModel.timerStarted.value == true) {
            pauseTimer()
        } else {
            playTimer()
        }
    }

    private fun pauseTimer() {
        timer!!.cancel()
//        stopAllSound()
        viewModel.stopTimer()
    }

    private fun playTimer() {
        viewModel.startTimer()
        createTimer()
    }

    fun stopTimer() {
        pauseTimer()
        findNavController().navigateUp()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("onDestroyView", "onDestroyView")

        stopAllSound()
        timer?.cancel()
    }

    private fun stopAllSound() {
        doneMediaPlayer?.reset()
        tickMediaPlayer?.reset()
    }

}