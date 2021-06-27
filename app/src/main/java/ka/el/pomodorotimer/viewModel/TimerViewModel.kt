package ka.el.pomodorotimer.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import ka.el.pomodorotimer.R

class TimerViewModel(application: Application): AndroidViewModel(application) {

    private var _hasTick = MutableLiveData<Boolean>(true)
    val hasTick: LiveData<Boolean> get() = _hasTick

    fun toggleHasTick() {
        _hasTick.value = !_hasTick.value!!
    }

    private fun getString(stringId: Int): String? {
        return getApplication<Application>().resources.getString(stringId)
    }

    private var _mode = MutableLiveData<String>()
    val mode: LiveData<String> get() = _mode

    private var _currentTimer = MutableLiveData<Int>()
    val currentTimer: LiveData<Int> get() = _currentTimer

    private var _lastTimer = MutableLiveData<Int>()
    val lastTimerSeconds: LiveData<Int> get() = _lastTimer
    val lastTimer: LiveData<String> = Transformations.map(_lastTimer) {
        v -> convertSecondsToString(v) }

    private fun convertSecondsToString(v: Int): String {
        val minutes = v / 60
        val seconds = v - minutes * 60
        
        return "${addZero(minutes)}:${addZero(seconds)}"
    }

    private fun addZero(num: Int): String {
        return if (num < 10) {
            "0${num}"
        }  else {
            num.toString()
        }
    }



    private var _allPomodoros = MutableLiveData<Int>()
    val allPomodoros: LiveData<Int> get() = _allPomodoros

    fun setAllPomodoros(pomodors: Int)  {
        _allPomodoros.value = pomodors
    }

    private var _countOfPomodoros = MutableLiveData<Int>()

    private var _currentPomodoro = MutableLiveData<Int>()
    val currentPomodoro: LiveData<Int> get() = _currentPomodoro

    private var _percent = MutableLiveData<Int>()
    val percent: LiveData<Int> get() = _percent

    private var _pomodoro = MutableLiveData<Int>()
    val pomodoro: LiveData<Int> get() = _pomodoro

    private var _shortBreak = MutableLiveData<Int>()
    val shortBreak: LiveData<Int> get() = _shortBreak

    private var _longBreak = MutableLiveData<Int>()
    val longBreak: LiveData<Int> get() = _longBreak

    private var _timerStarted = MutableLiveData<Boolean>()
    val timerStarted: LiveData<Boolean> get() = _timerStarted

    private val POMODORO = getString(R.string.pomodoro_title)
    private val SHORT_BREAK = getString(R.string.short_break_title)
    private val LONG_BREAK = getString(R.string.long_break_title)
    private val END = getString(R.string.end_work)

    fun initTimer(pomodoroSec: Int, shortBreakSec: Int, longBreakSec: Int) {
        _pomodoro.value = pomodoroSec
        _shortBreak.value = shortBreakSec
        _longBreak.value = longBreakSec

        updateTimerTime(POMODORO)
    }

    fun goNextStage(): String {
        var newMode = ""

        when (_mode.value) {
            POMODORO -> {
                Log.d("goNextStage", "countOfPomodoros.value: ${_countOfPomodoros.value}")

                if (_countOfPomodoros.value == 4) {
                    updateTimerTime(LONG_BREAK)
                    newMode = LONG_BREAK!!
                } else {
                    updateTimerTime(SHORT_BREAK)
                    newMode = SHORT_BREAK!!
                }
            }

            SHORT_BREAK -> {

                if (_countOfPomodoros.value == 4) {
                    updateTimerTime(LONG_BREAK)
                    newMode = LONG_BREAK!!
                } else {
                    updateTimerTime(POMODORO)
                    newMode = POMODORO!!
                    _countOfPomodoros.value = _countOfPomodoros.value?.plus(1)
                }
            }

            LONG_BREAK -> {
                if ((_currentPomodoro.value!! + 1) <= _allPomodoros.value!!) {
                    _currentPomodoro.value = _currentPomodoro.value!!.plus(1)
                    newMode = POMODORO!!
                } else {
                    newMode = END!!
                    Log.d("goNextStage", "END!")
                }
            }
        }


        _mode.value = newMode
        updateTimerTime(newMode)
        return newMode
    }

    private fun updateTimerTime(newMode: String?) {
        when (newMode) {
            POMODORO -> {
                _currentTimer.value = _pomodoro.value
                _lastTimer.value = _pomodoro.value
            }

            SHORT_BREAK -> {
                _currentTimer.value = _shortBreak.value
                _lastTimer.value = _shortBreak.value
            }

            LONG_BREAK -> {
                _currentTimer.value = _longBreak.value
                _lastTimer.value = _longBreak.value

                _countOfPomodoros.value = 1
            }

        }
    }

    init {
        _mode.value = POMODORO

        _currentPomodoro.value = 1
        _countOfPomodoros.value = 1

        _percent.value = 0

        _timerStarted.value = false
    }

    fun startTimer() {
        _timerStarted.value = true
    }

    fun stopTimer() {
        _timerStarted.value = false
    }

    fun updateTimer(secondsUntilFinished: Int) {
        _lastTimer.value = secondsUntilFinished
        _percent.value = ((_currentTimer.value!!.toDouble() - _lastTimer.value!!.toDouble()) / _currentTimer.value!!.toDouble() * 100).toInt()
    }
}