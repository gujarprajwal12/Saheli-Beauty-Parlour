package com.jobassignmentproject.PresentationLayer.Ui.OpenWeather.OpenWeatherVeiwModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jobassignmentproject.DataLayer.OpenWeather.WeatherResponse
import com.jobassignmentproject.PresentationLayer.Ui.OpenWeather.Respostiry.WeatherRepository

class WeatherViewModel : ViewModel() {
    private val repository = WeatherRepository()
    private val _weather = MutableLiveData<WeatherResponse?>()
    val weather: LiveData<WeatherResponse?> get() = _weather

    fun fetchWeather(city: String, apiKey: String) {
        repository.getWeather(city, apiKey) { response ->
            _weather.postValue(response)
        }
    }
}
