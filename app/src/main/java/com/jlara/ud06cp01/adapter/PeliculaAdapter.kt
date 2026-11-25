package com.jlara.ud06cp01.adapter

import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.jlara.ud06cp01.R
import com.jlara.ud06cp01.model.Pelicula

class PeliculaAdapter(
    private val peliculas: MutableList<Pelicula>,
    private val onClick: (Pelicula) -> Unit,
    private val onLongClickPosition: (Int) -> Unit
) : RecyclerView.Adapter<PeliculaAdapter.PeliculaViewHolder>() {

    inner class PeliculaViewHolder(view: View) : RecyclerView.ViewHolder(view),
        View.OnCreateContextMenuListener {
        val imagen: ImageView = view.findViewById(R.id.ivPoster)
        val nombre: TextView = view.findViewById(R.id.tvNombre)

        init {
            view.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            // Guardamos la posición antes de que se muestre el menú
            onLongClickPosition(bindingAdapterPosition)
            menu.setHeaderTitle(peliculas[bindingAdapterPosition].nombre)
            menu.add(Menu_GRUPO, R.id.menu_eliminar, 0, R.string.eliminar)
            menu.add(Menu_GRUPO, R.id.menu_editar, 1, R.string.editar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeliculaViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pelicula, parent, false)
        return PeliculaViewHolder(view)
    }

    override fun getItemCount(): Int = peliculas.size

    override fun onBindViewHolder(holder: PeliculaViewHolder, position: Int) {
        val pelicula = peliculas[position]
        holder.nombre.text = pelicula.nombre
        holder.imagen.setImageResource(pelicula.imagen)
        holder.itemView.setOnClickListener { onClick(pelicula) }
    }

    companion object {
        private const val Menu_GRUPO = 0
    }
}
