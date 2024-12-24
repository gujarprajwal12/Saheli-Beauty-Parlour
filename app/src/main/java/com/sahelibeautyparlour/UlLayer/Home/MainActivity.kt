package com.sahelibeautyparlour.UlLayer.Home

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.sahelibeautyparlour.DataLayer.Constants
import com.sahelibeautyparlour.DomainLayer.Retrofit.RetrofitInstance
import com.sahelibeautyparlour.R
import com.sahelibeautyparlour.UlLayer.Home.Adapter.ImageAdapter
import com.sahelibeautyparlour.UlLayer.Home.Dataclass.DriveFilesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchImages(recyclerView)
    }

    private fun fetchImages(recyclerView: RecyclerView) {
        val query = "'${Constants.FOLDER_ID}' in parents and mimeType contains 'image/'"
        RetrofitInstance.api.listFiles(query, Constants.API_KEY).enqueue(object : Callback<DriveFilesResponse> {
            override fun onResponse(call: Call<DriveFilesResponse>, response: Response<DriveFilesResponse>) {
                if (response.isSuccessful) {
                    val files = response.body()?.files ?: emptyList()
                    recyclerView.adapter = ImageAdapter(files)
                } else {
                    Log.e("MainActivity", "API Error: ${response.errorBody()?.string()}")
                    Toast.makeText(this@MainActivity, "Api Error  ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DriveFilesResponse>, t: Throwable) {
                Log.e("MainActivity", "Network Error: ${t.message}")
                Toast.makeText(this@MainActivity, "Network Error : ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }


}