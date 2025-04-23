package com.example.superheroe.activities

import android.os.Bundle
import android.view.MenuItem
import android.view.View
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

        supportActionBar?.setDisplayUseLogoEnabled(true)

        val id = intent.getStringExtra(SUPERHEROE_ID)!!

        getSuperheroeById(id)

        binding.navigationView.setOnItemSelectedListener { menuItem ->
            binding.contentBiography.root.visibility = View.GONE
            binding.contentAppearance.root.visibility = View.GONE
            binding.contentStats.root.visibility = View.GONE

            when (menuItem.itemId) {
                R.id.menu_biography -> binding.contentBiography.root.visibility = View.VISIBLE
                R.id.menu_appearance -> binding.contentAppearance.root.visibility = View.VISIBLE
                R.id.menu_stats -> binding.contentStats.root.visibility = View.VISIBLE
            }
            true
        }

        binding.navigationView.selectedItemId = R.id.menu_biography
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
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
       supportActionBar?.subtitle = superheroe.biography.realName
        Picasso.get().load(superheroe.image.url).into(binding.avatarImageView)

        // Biography
        binding.contentBiography.publisherTextView.text = superheroe.biography.publisher
        binding.contentBiography.placeOfBirthTextView.text = superheroe.biography.placeOfBirth
        binding.contentBiography.alignmentTextView.text = superheroe.biography.alignment
        binding.contentBiography.alignmentTextView.setTextColor(getColor(superheroe.getAlignmentColor()))
        binding.contentBiography.alignmentTextView.text = superheroe.work.occupation
        binding.contentBiography.alignmentTextView.text = superheroe.work.base

        // Appearance

        binding.contentAppearance.genderTextView.text = superheroe.appearance.gender
        binding.contentAppearance.raceTextView.text = superheroe.appearance.race
        binding.contentAppearance.eyeColorTextView.text = superheroe.appearance.eyeColor
        binding.contentAppearance.hairColorTextView.text = superheroe.appearance.hairColor
        binding.contentAppearance.weightTextView.text = superheroe.appearance.weight[1]
        binding.contentAppearance.heightTextView.text = superheroe.appearance.height[1]

        // Stats
        with(superheroe.stats) {
            binding.contentStats.intelligenceTextView.text = "$intelligence"
            binding.contentStats.intelligenceProgress.progress = intelligence
            binding.contentStats.strengthTextView.text = "$strength"
            binding.contentStats.strengthProgress.progress = strength
            binding.contentStats.speedTextView.text = "$speed"
            binding.contentStats.strengthProgress.progress = speed
            binding.contentStats.durabilityTextView.text = "$durability"
            binding.contentStats.durabilityProgress.progress = durability
            binding.contentStats.PowerTextView.text = "$power"
            binding.contentStats.PowerProgress.progress = power
            binding.contentStats.combatTextView.text = "$combat"
            binding.contentStats.combatProgress.progress = combat

       }
    }
}