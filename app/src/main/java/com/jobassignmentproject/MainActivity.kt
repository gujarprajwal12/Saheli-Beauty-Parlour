package com.jobassignmentproject

import android.os.Bundle
import android.service.controls.ControlsProviderService.TAG
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import com.jobassignmentproject.PresentationLayer.Ui.OpenWeather.OpenWeatherVeiwModel.WeatherViewModel
import com.jobassignmentproject.PresentationLayer.utils.NetworkObserver
import com.jobassignmentproject.PresentationLayer.utils.SnackbarUtil
import com.jobassignmentproject.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: WeatherViewModel by viewModels()
    private val apiKey = "9b36de759e6d947adc9d0f7aba64bdad"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initview()
        ApiCall()

    }

    private fun ApiCall() {
        viewModel.weather.observe(this, Observer { weather ->
            weather?.let {
                println("City: ${it.name}")
                println("Temperature: ${it.main.temp}°C")
                println("Humidity: ${it.main.humidity}%")
                println("Condition: ${it.weather.firstOrNull()?.description}")


                Log.d(TAG, "ApiCall:  temp  ${it.main.temp}°C  Humidity: ${it.main.humidity}%")
            } ?: println("Failed to get weather data")

        })

        viewModel.fetchWeather("Shevgaon", apiKey)
    }



    private fun initview() {

        SnackbarUtil.showShort(binding.root, "Welcome to the app!")

        NetworkObserver.networkStatus.observe(this) { status ->
            if (status == "Slow Internet Connection" || status == "No Internet Connection") {
                SnackbarUtil.showShort(binding.root, status)
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        NetworkObserver.unregister()
    }

}