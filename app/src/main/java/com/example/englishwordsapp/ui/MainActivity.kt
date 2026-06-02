package com.example.englishwordsapp.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import com.example.englishwordsapp.R
import com.example.englishwordsapp.databinding.ActivityMainBinding
import com.example.englishwordsapp.model.Category
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i(
            "MainActivity",
            "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}"
        )

        threadPool.submit {
            Log.i("MainActivity", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

            try {
                val url = URL("https://recipes.androidsprint.ru/api/category")
                val connection = url.openConnection() as HttpURLConnection

                try {
                    connection.connect()

                    val jsonString = connection.inputStream.bufferedReader().readText()

                    Log.i("!!!", "responseCode: ${connection.responseCode}")
                    Log.i("!!!", "responseMessage: ${connection.responseMessage}")
                    Log.i("!!!", "Body: $jsonString")

                    val categories = Json.decodeFromString<List<Category>>(jsonString)
                    Log.i("MainActivity", "Получено категорий: ${categories.size}")

                    val categoryIds = categories.map { it.id }

                    categoryIds.forEach { categoryId ->
                        threadPool.submit {
                            Log.i(
                                "MainActivity",
                                "Запрос рецептов категории $categoryId на потоке: ${Thread.currentThread().name}"
                            )

                            try {
                                val recipesUrl =
                                    URL("https://recipes.androidsprint.ru/api/category/$categoryId/recipe")
                                val recipesConnection =
                                    recipesUrl.openConnection() as HttpURLConnection

                                try {
                                    recipesConnection.connect()

                                    val recipesJson =
                                        recipesConnection.inputStream.bufferedReader().readText()

                                    Log.i(
                                        "MainActivity",
                                        "Рецепты категории $categoryId: $recipesJson"
                                    )
                                } finally {
                                    recipesConnection.disconnect()
                                }
                            } catch (e: Exception) {
                                Log.e(
                                    "MainActivity",
                                    "Ошибка при получении рецептов категории $categoryId",
                                    e
                                )
                            }
                        }
                    }
                } finally {
                    connection.disconnect()
                }
            } catch (e: Exception) {
                Log.e(
                    "MainActivity",
                    "Ошибка при выполнении запроса",
                    e
                )
            }
        }

        binding.btnCategories.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
        }

        binding.btnFavorites.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favoritesFragment)
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }
    }
}
