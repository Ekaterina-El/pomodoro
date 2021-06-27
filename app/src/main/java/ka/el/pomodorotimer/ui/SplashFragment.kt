package ka.el.pomodorotimer.ui

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import ka.el.pomodorotimer.R

class SplashFragment : Fragment() {
    val loadingTime = 1000L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Thread(Runnable {
            Thread.sleep(loadingTime)
            activity?.runOnUiThread {
                goNext()
            }
        }).start()
    }

    private fun goNext() {
        findNavController().navigate(R.id.action_splashFragment_to_settingsFragment)
    }

}