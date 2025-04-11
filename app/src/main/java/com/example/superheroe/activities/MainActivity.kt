package com.example.superheroe.activities

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.SearchView.OnQueryTextListener
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.superheroe.R
import com.example.superheroe.adapters.SuperHeroeAdapter
import com.example.superheroe.data.Superheroe
import com.example.superheroe.utils.SuperHeroeService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var recyclerView: RecyclerView
    lateinit var adapter: SuperHeroeAdapter

    var superheroeList: List<Superheroe> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView =  findViewById(R.id.recyclerView)

        adapter = SuperHeroeAdapter(superheroeList) { position: Int ->
            val superheroe = superheroeList[position]

            val  intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.SUPERHEROE_ID, superheroe.id)
            startActivity(intent)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        searchSuperheroes("a")
    }
    // creacion del menu o la busqueda, despues del creara el icono
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.activities_main_menu, menu)

        val menuItem = menu.findItem(R.id.menu_search)
        val searchView = menuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                searchSuperheroes(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {

                return false
            }
        })

        return true
    }
    
    fun searchSuperheroes(query: String) {
        //LLamada en un hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = SuperHeroeService.getInstance()
                val response = service.findSuperheroesByName(query)
                superheroeList = response.results

                //volvemos al hilo principal
                CoroutineScope(Dispatchers.Main).launch {
                    adapter.updateItems(superheroeList)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}


