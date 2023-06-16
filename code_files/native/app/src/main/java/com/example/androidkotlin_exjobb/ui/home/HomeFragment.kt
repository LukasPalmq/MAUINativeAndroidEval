package com.example.androidkotlin_exjobb.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.androidkotlin_exjobb.databinding.FragmentHomeBinding
import kotlinx.coroutines.*
import java.io.File
import kotlin.math.pow
import java.util.concurrent.TimeUnit
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlin.system.measureTimeMillis
import android.location.LocationListener
import android.location.LocationManager
import android.content.Context
import android.content.Intent
import com.example.androidkotlin_exjobb.ui.SecondActivity
import java.io.BufferedInputStream
import java.net.HttpURLConnection
import java.net.URL


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val REQUEST_LOCATION_PERMISSION = 1

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
                ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.btnMultiThread.setOnClickListener { performMultithreadedTask() }
        binding.btnFileTest.setOnClickListener { fileReadWriteOperations() }
        binding.btnGps.setOnClickListener { getLocationTime() }
        binding.btnSecondPage.setOnClickListener {
            val startTime = System.currentTimeMillis()
            val intent = Intent(requireContext(), SecondActivity::class.java)
            intent.putExtra("startTime", startTime)
            startActivity(intent)
        }
        binding.btnDownload.setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                downloadFileAsync()
            }
        }

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                REQUEST_LOCATION_PERMISSION
            )
        }

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getLocationTime() {
        try {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val startTime = System.nanoTime()

                val locationListener = object : LocationListener {
                    override fun onLocationChanged(location: Location) {
                        val latitude = location.latitude
                        val longitude = location.longitude
                        Log.d("LocationFragment", "Latitude: $latitude, Longitude: $longitude")

                        val endTime = System.nanoTime()
                        val timeElapsed = TimeUnit.MILLISECONDS.convert(endTime - startTime, TimeUnit.NANOSECONDS)
                        Log.d("LocationFragment", "Time taken: $timeElapsed ms")

                        // Remove location updates after receiving the first one
                        locationManager.removeUpdates(this)
                        Toast.makeText(context, "$timeElapsed ms", Toast.LENGTH_LONG).show()
                    }

                    override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
                }

                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, null)
            }
        } catch (ex: SecurityException) {
            Log.d("LocationFragment", "Error: ${ex.message}")
        }
    }


    private fun performMultithreadedTask() {
        val startTime = SystemClock.elapsedRealtime()
        for (x in 0 until 50) {
            val tasks = mutableListOf<Deferred<Double>>()
            val scope = CoroutineScope(Dispatchers.Default)
            val iterations = 1_000_000 // Number of iterations to approximate pi

            fun approximatePi(iterations: Int): Double {
                var sum = 0.0
                for (i in 0 until iterations) {
                    val term = (-1.0).pow(i) / (2 * i + 1)
                    sum += term
                }
                return sum * 4
            }

            val elapsedTime = measureTimeMillis {
                runBlocking {
                    for (i in 0 until 10) {
                        tasks.add(scope.async {
                            val pi = approximatePi(iterations)
                            pi
                        })
                    }
                    tasks.awaitAll()
                }
            }
        }
        val endTime = SystemClock.elapsedRealtime()
        Toast.makeText(context, "${endTime - startTime} ms", Toast.LENGTH_LONG).show()
    }


    private fun fileReadWriteOperations() {
        val startTime = SystemClock.elapsedRealtime()
        for (i in 0 until 100) {
            val context = requireContext()
            val filePath = File(context.filesDir, "largeFile.txt")

            val data = ByteArray(1024 * 1024) // 1 MB of data

            // Write to file
            filePath.writeBytes(data)

            // Read from file
            val fileBytes = filePath.readBytes()

            // Delete file
            filePath.delete()
            val deleteEndTime = SystemClock.elapsedRealtime()
        }
        val endTime = SystemClock.elapsedRealtime()
        Toast.makeText(context, "${endTime - startTime} ms", Toast.LENGTH_LONG).show()
    }


    private suspend fun downloadFileAsync() {
        val urlString = "https://speed.cloudflare.com/__down?bytes=1048576"
        var realElapsed : Long = 0;
        try {
            val elapsedTime = withContext(Dispatchers.IO) {
                measureTimeMillis {
                    val url = URL(urlString)
                    val connection = url.openConnection() as HttpURLConnection

                    connection.requestMethod = "GET"

                    if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                        // Handle error
                        return@measureTimeMillis
                    }

                    val inputStream = BufferedInputStream(connection.inputStream)
                    val data = inputStream.readBytes()

                    inputStream.close()
                    connection.disconnect()
                }
            }
            realElapsed = elapsedTime;

            Log.d("DownloadFile", "File download took: $elapsedTime ms")
        } catch (ex: Exception) {
            // Handle exceptions
            Log.e("DownloadFile", "Error downloading file", ex)
        }
        Toast.makeText(context, "$realElapsed ms", Toast.LENGTH_LONG).show()
    }
}