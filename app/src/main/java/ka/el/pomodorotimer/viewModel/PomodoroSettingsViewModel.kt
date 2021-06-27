package ka.el.pomodorotimer.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ka.el.pomodorotimer.data.Constants

class PomodoroSettingsViewModel: ViewModel() {
    private var _currentPomodoro = MutableLiveData<Int>()
    val currentPomodoro: LiveData<Int> get() = _currentPomodoro

    private var _currentShortBreak = MutableLiveData<Int>()
    val currentShortBreak: LiveData<Int> get() = _currentShortBreak

    private var _currentLongBreak = MutableLiveData<Int>()
    val currentLongBreak: LiveData<Int> get() = _currentLongBreak

    fun incrementValue(title: String) {
        when (title) {
            Constants.POMODORO -> {
                _currentPomodoro.value = _currentPomodoro.value?.plus(1)
            }

            Constants.SHORT_BREAK -> {
                _currentShortBreak.value = _currentShortBreak.value?.plus(1)
            }

            Constants.LONG_BREAK -> {
                _currentLongBreak.value = _currentLongBreak.value?.plus(1)
            }
        }
    }

    fun decrementValue(title: String) {
        when (title) {
            Constants.POMODORO -> {
                if (_currentPomodoro.value!! > 10 ) {
                    _currentPomodoro.value = _currentPomodoro.value?.minus(1)
                }
            }

            Constants.SHORT_BREAK -> {
                if (_currentShortBreak.value!! > 2 ) {
                    _currentShortBreak.value = _currentShortBreak.value?.minus(1)
                }
            }

            Constants.LONG_BREAK -> {
                if (_currentLongBreak.value!! > 5 ) {
                    _currentLongBreak.value = _currentLongBreak.value?.minus(1)
                }
            }
        }
    }

    init {
        _currentPomodoro.value = 25
        _currentShortBreak.value = 5
        _currentLongBreak.value = 15
    }

}