package com.example.superheroe.adaoters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.superheroe.R
import com.example.superheroe.data.Superheroe
import com.squareup.picasso.Picasso

class SuperHeroeAdapter(
    var items: List<Superheroe>
): Adapter<SuperheroeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SuperheroeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_superheroe, parent, false)
        return SuperheroeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SuperheroeViewHolder, position: Int) {
        val superheroe = items[position]
        holder.render(superheroe)
    }

    fun updateItems(items: List<Superheroe>) {
        this.items = items
        notifyDataSetChanged()
    }
}

class  SuperheroeViewHolder(view: View) : ViewHolder(view) {

    var nameTextView: TextView = view.findViewById(R.id.nameTextView)
    var avatarImageView: ImageView = view.findViewById(R.id.avatarImageView)

    fun render(superheroe: Superheroe) {
        nameTextView.text = superheroe.name
        Picasso.get().load(superheroe.image.url).into(avatarImageView)
    }
}