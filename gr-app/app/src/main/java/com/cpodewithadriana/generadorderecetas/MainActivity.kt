package com.cpodewithadriana.generadorderecetas

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val ingredientes = arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val etIngredient: EditText = findViewById(R.id.etIngredient)
        val btnAddIngredient: Button = findViewById(R.id.btnAddIngredient)
        val btnPrepare: Button = findViewById(R.id.btnPrepare)
        val llSelectedIngredients: LinearLayout = findViewById(R.id.llSelectedIngredients)

        btnPrepare.setOnClickListener {
            if (ingredientes.isNotEmpty()) {
                val query = ingredientes.joinToString(",rice") // Une los ingredientes seleccionados
                val apiKey = "2ca07eb55c694695b78f4f51333dec67" // Tu clave de API

                // Realiza la búsqueda de recetas antes de abrir la segunda actividad
                RetrofitInstance.api.searchRecipes(query, apiKey).enqueue(object : Callback<RecipeResponse> {
                    override fun onResponse(call: Call<RecipeResponse>, response: Response<RecipeResponse>) {
                        if (response.isSuccessful) {
                            val recipes = response.body()?.results ?: emptyList()
                            val recipeTitles = recipes.map { "${it.title} - ${it.id}" } // Preparamos los datos para la segunda pantalla
                            val intent = Intent(this@MainActivity, IngredientesActivity::class.java)
                            intent.putStringArrayListExtra("RECIPES", ArrayList(recipeTitles)) // Pasamos las recetas
                            startActivity(intent)
                        } else {
                            Toast.makeText(this@MainActivity, "Error al buscar recetas", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RecipeResponse>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Por favor, agrega al menos un ingrediente", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
