package com.example.weather
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.TextView
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val cityText: TextView = findViewById(R.id.city_text)
        val temp: TextView = findViewById((R.id.temp))
        val city: EditText = findViewById(R.id.city)
        val find: Button = findViewById(R.id.submit)
        val tempToggle: Switch = findViewById(R.id.temp_toggle)

        find.setOnClickListener{
            val textView: TextView = findViewById(R.id.city_text)
            val client = OkHttpClient()
            val apiKey = "31564b45765641eea0b174320231912"
            var city = city.text
            val url = "https://api.weatherapi.com/v1/current.json?key=$apiKey&q=$city"
            val request = Request.Builder().url(url).build()
            client.newCall(request).enqueue(object :Callback{
                override fun onFailure(call: Call, e: IOException) {
                    e.printStackTrace()
                }

                override fun onResponse(call: Call, response: Response) {
                    if(response.isSuccessful){
                        val jsonResponse = response.body!!.string()
                        runOnUiThread {
                            handleJsonResponse(jsonResponse, cityText, temp, tempToggle)
                        }

                    }
                }
            })
        }
    }
}
private fun handleJsonResponse(jsonResponse: String, city: TextView,temp: TextView, tempToggle: Switch ) {
    // Parse JSON using Gson into WeatherResponse
    val gson = Gson()
    val weatherResponse: WeatherResponse = gson.fromJson(jsonResponse, WeatherResponse::class.java)

    // Access specific fields
    val location = weatherResponse.location
    val current = weatherResponse.current

    // Now you can use location and current objects
    city.text = "${location.name}"
    if(tempToggle.isChecked()){
        temp.text = "${current.temp_f}F"
    }
    else{
        temp.text = "${current.temp_c}C"
    }

}
