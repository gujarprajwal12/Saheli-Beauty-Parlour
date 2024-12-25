package com.sahelibeautyparlour.UlLayer.Home

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
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
import com.sahelibeautyparlour.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var progressBar: ProgressBar
    private lateinit var imageAdapter: ImageAdapter
    private var currentPage = 1
    private var isLoading = false
    private var isDataAvailable = true  // Flag to track if more data is available
    private val loadedFileIds = mutableSetOf<String>()  // To track already loaded file IDs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        progressBar = findViewById(R.id.progressBar)
        val recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)
        imageAdapter = ImageAdapter(mutableListOf()) // Initialize with an empty list
        recyclerView.adapter = imageAdapter

        // Setup scroll listener for infinite scrolling
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                // Check if we've reached the bottom of the list
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                if (!isLoading && isDataAvailable && layoutManager.findLastVisibleItemPosition() == imageAdapter.itemCount - 1) {
                    // If we're at the end of the list and more data is available, fetch more data
                    loadMoreData()
                }
            }
        })

        // Initial data load
        fetchImages()
    }

    private fun fetchImages() {
        progressBar.visibility = View.VISIBLE

        val query = "'${Constants.FOLDER_ID}' in parents and (mimeType='image/jpeg' or mimeType='image/png' or mimeType='image/gif' or mimeType='image/webp')"

        RetrofitInstance.api.listFiles(query, Constants.API_KEY).enqueue(object : Callback<DriveFilesResponse> {
            override fun onResponse(call: Call<DriveFilesResponse>, response: Response<DriveFilesResponse>) {
                progressBar.visibility = View.GONE
                if (response.isSuccessful) {
                    val files = response.body()?.files?.filter { !loadedFileIds.contains(it.id) } ?: emptyList()
                    if (files.isNotEmpty()) {
                        imageAdapter.updateData(files)
                        files.forEach { loadedFileIds.add(it.id) }  // Add the file IDs to the set
                    } else {
                        Toast.makeText(this@MainActivity, "No data found", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@MainActivity, "API Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<DriveFilesResponse>, t: Throwable) {
                progressBar.visibility = View.GONE
                Toast.makeText(this@MainActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadMoreData() {
        if (isDataAvailable) {
            isLoading = true
            progressBar.visibility = View.VISIBLE

            val query = "'${Constants.FOLDER_ID}' in parents and (mimeType='image/jpeg' or mimeType='image/png' or mimeType='image/gif' or mimeType='image/webp')"

            RetrofitInstance.api.listFiles(query, Constants.API_KEY).enqueue(object : Callback<DriveFilesResponse> {
                override fun onResponse(call: Call<DriveFilesResponse>, response: Response<DriveFilesResponse>) {
                    progressBar.visibility = View.GONE
                    isLoading = false

                    if (response.isSuccessful) {
                        val files = response.body()?.files?.filter { !loadedFileIds.contains(it.id) } ?: emptyList()
                        if (files.isNotEmpty()) {
                            imageAdapter.addMoreData(files)
                            files.forEach { loadedFileIds.add(it.id) }  // Add the file IDs to the set
                        } else {
                            isDataAvailable = false
                            Toast.makeText(this@MainActivity, "Up to date", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@MainActivity, "API Error: ${response.errorBody()?.string()}", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<DriveFilesResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    isLoading = false
                    Toast.makeText(this@MainActivity, "Network Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}

