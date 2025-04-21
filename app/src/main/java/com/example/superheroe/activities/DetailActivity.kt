package com.example.superheroe.activities

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.superheroe.R
import com.example.superheroe.data.Superheroe
import com.example.superheroe.databinding.ActivityDetailBinding
import com.example.superheroe.utils.SuperHeroeService
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailActivity : AppCompatActivity() {

    companion object {
        const val SUPERHEROE_ID = "SUPERHEROE_ID"
    }

    lateinit var binding: ActivityDetailBinding

    lateinit var superheroe: Superheroe

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val id = intent.getStringExtra(SUPERHEROE_ID)!!
        getSuperheroeById(id)

        binding.navigationView.setOnItemSelectedListener { menuItem ->
            binding.contentBiography.visibility = View.GONE
            binding.contentAppearance.visibility = View.GONE
            binding.contentStats.visibility = View.GONE

            when (menuItem.itemId) {
                R.id.menu_biography -> binding.contentBiography.visibility = View.VISIBLE
                R.id.menu_appearance -> binding.contentAppearance.visibility = View.VISIBLE
                R.id.menu_stats -> binding.contentStats.visibility = View.VISIBLE
            }
            true
        }
        binding.navigationView.selectedItemId = R.id.menu_biography
    }

    fun getSuperheroeById(id: String) {
        //LLamada en un hilo secundario
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = SuperHeroeService.getInstance()
                superheroe = service.findSuperheroById(id)

                //volvemos al hilo principal
                CoroutineScope(Dispatchers.Main).launch {
                    loadData()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadData() {
        supportActionBar?.title = superheroe.name
        supportActionBar?.subtitle = superheroe.biography.realNAme
        Picasso.get().load(superheroe.image.url).into(binding.avatarImageView)

        binding.publisherTextView.text = superheroe.biography.publisher
        binding.placeOfBirthTextView.text = superheroe.biography.placeOfBirth
        binding.alignmentTextView.text = superheroe.biography.alignment
    }
}