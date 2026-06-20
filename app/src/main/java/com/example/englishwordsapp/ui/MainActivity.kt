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
import com.example.englishwordsapp.model.Recipe
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val threadPool: ExecutorService = Executors.newFixedThreadPool(10)
    private val logging = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
    private val client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.i(
            "MainActivity",
            "Метод onCreate() выполняется на потоке: ${Thread.currentThread().name}"
        )

        threadPool.execute {
            Log.i("MainActivity", "Выполняю запрос на потоке: ${Thread.currentThread().name}")

            try {
                val categoryRequest: Request =
                    Request.Builder().url("https://recipes.androidsprint.ru/api/category").build()
                client.newCall(categoryRequest).execute().use { categoryResponse ->
                    val jsonString = categoryResponse.body?.string() ?: ""

                    val categories = Json.decodeFromString<List<Category>>(jsonString)
                    Log.i("MainActivity", "Получено категорий: ${categories.size}")

                    val categoryIds = categories.map { it.id }

                    categoryIds.forEach { categoryId ->
                        threadPool.execute {
                            Log.i(
                                "MainActivity",
                                "Запрос рецептов категории $categoryId на потоке: ${Thread.currentThread().name}"
                            )

                            try {
                                val recipesRequest: Request = Request.Builder()
                                    .url("https://recipes.androidsprint.ru/api/category/$categoryId/recipe")
                                    .build()

                                client.newCall(recipesRequest).execute().use { recipesResponse ->
                                    val recipesJson =
                                        recipesResponse.body?.string() ?: ""
                                    val recipes: List<Recipe> = Json.decodeFromString(recipesJson)

                                    Log.i(
                                        "MainActivity",
                                        "Категория $categoryId: получено рецептов ${recipes.size}"
                                    )
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

    override fun onDestroy() {
        super.onDestroy()
        threadPool.shutdown()
    }
}
