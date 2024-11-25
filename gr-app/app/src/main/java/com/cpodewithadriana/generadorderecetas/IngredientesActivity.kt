package com.cpodewithadriana.generadorderecetas

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IngredientesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingredientes)

        val tvIngredientes: TextView = findViewById(R.id.tvIngredientes)
        val tvResultados: TextView = findViewById(R.id.tvResultados)
        val btnBuscar: Button = findViewById(R.id.btnBuscar)

        // Obtener los ingredientes seleccionados desde el intent
        val ingredientes = intent.getStringArrayListExtra("INGREDIENTES") ?: arrayListOf()

        // Mostrar los ingredientes seleccionados
        tvIngredientes.text = "Has elegido:\n${ingredientes.joinToString("\n")}"

        btnBuscar.setOnClickListener {
            val apiKey = "2ca07eb55c694695b78f4f51333dec67" // Reemplaza con tu clave de la API de Spoonacular
            val query = ingredientes.joinToString(",") // Convierte la lista de ingredientes en un string separado por comas

            // Llamar a la API usando Retrofit
            RetrofitInstance.api.searchRecipes(query, apiKey).enqueue(object : Callback<List<Recipe>> {
                override fun onResponse(call: Call<List<Recipe>>, response: Response<List<Recipe>>) {
                    if (response.isSuccessful) {
                        val recipes = response.body()
                        if (!recipes.isNullOrEmpty()) {
                            // Muestra los títulos de las recetas
                            val resultados = recipes.joinToString("\n") { "Receta: ${it.title}" }
                            tvResultados.text = resultados
                        } else {
                            tvResultados.text = getString(R.string.recipe_error) // Mensaje si no hay recetas
                        }
                    } else {
                        tvResultados.text = "Error: ${response.errorBody()?.string()}"
                    }
                }

                override fun onFailure(call: Call<List<Recipe>>, t: Throwable) {
                    tvResultados.text = getString(R.string.network_error, t.message)
                }
            })
        }
    }
}
