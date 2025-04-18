package com.example.superheroe.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.superheroe.data.Superheroe
import com.example.superheroe.databinding.ItemSuperheroeBinding
import com.squareup.picasso.Picasso

class SuperHeroeAdapter(
    var items: List<Superheroe>,
    //capturamos el click en el adapter y para ello vamos a crear una funcion
    val onItemClick: (position: Int) -> Unit
): Adapter<SuperheroeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperheroeViewHolder {
        // val view = LayoutInflater.from(parent.context).inflate(R.layout.item_superheroe, parent, false)

        val binding = ItemSuperheroeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SuperheroeViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SuperheroeViewHolder, position: Int) {
        val superheroe = items[position]
        holder.render(superheroe)
        holder.itemView.setOnClickListener {
            onItemClick(position)
        }
    }

    fun updateItems(items: List<Superheroe>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class  SuperheroeViewHolder(val binding: ItemSuperheroeBinding) : ViewHolder(binding.root) {

    fun render(superheroe: Superheroe) {
        binding.nameTextView.text = superheroe.name
        Picasso.get().load(superheroe.image.url).into(binding.avatarImageView)
    }
}