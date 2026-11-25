package com.jlara.ud06cp01

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.snackbar.Snackbar
import com.jlara.ud06cp01.adapter.PeliculaAdapter
import com.jlara.ud06cp01.model.Pelicula
import com.jlara.ud06cp01.provider.PeliculaProvider

class MainActivity : AppCompatActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var swipe: SwipeRefreshLayout
    private lateinit var adapter: PeliculaAdapter

    private val peliculas = mutableListOf<Pelicula>()
    private var posicionSeleccionada = -1

    private val editarLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val nombre = result.data?.getStringExtra(EditarActivity.EXTRA_NOMBRE)
                if (nombre != null && posicionSeleccionada in peliculas.indices) {
                    peliculas[posicionSeleccionada].nombre = nombre
                    adapter.notifyItemChanged(posicionSeleccionada)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        aplicarInsets(findViewById(R.id.raiz))

        recycler = findViewById(R.id.recyclerPeliculas)
        swipe = findViewById(R.id.swipeRefresh)

        adapter = PeliculaAdapter(
            peliculas,
            onClick = { abrirDetalle(it) },
            onLongClickPosition = { posicionSeleccionada = it }
        )
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        swipe.setOnRefreshListener {
            recargarLista()
            swipe.isRefreshing = false
        }

        if (savedInstanceState == null) cargarPeliculas()
        else {
            @Suppress("UNCHECKED_CAST", "DEPRECATION")
            val guardadas = savedInstanceState.getSerializable(KEY_LISTA) as? ArrayList<Pelicula>
            peliculas.addAll(guardadas ?: emptyList())
            adapter.notifyDataSetChanged()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putSerializable(KEY_LISTA, ArrayList(peliculas))
    }

    private fun cargarPeliculas() {
        // Copias, para que editar un nombre no altere la lista original del provider
        peliculas.clear()
        peliculas.addAll(PeliculaProvider.listaCarga.map { it.copy() })
        adapter.notifyDataSetChanged()
    }

    private fun recargarLista() {
        cargarPeliculas()
        Snackbar.make(recycler, R.string.lista_recargada, Snackbar.LENGTH_SHORT).show()
    }

    private fun abrirDetalle(pelicula: Pelicula) {
        val intent = Intent(this, DetalleActivity::class.java)
        intent.putExtra(DetalleActivity.EXTRA_PELICULA, pelicula)
        startActivity(intent)
    }

    // ---------- Menú general ----------
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.menu_recargar -> {
            recargarLista()
            true
        }
        R.id.menu_limpiar -> {
            confirmarLimpiar()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun confirmarLimpiar() {
        AlertDialog.Builder(this)
            .setTitle(R.string.eliminar_todas)
            .setMessage(R.string.confirmar_limpiar)
            .setNegativeButton(R.string.cerrar, null)
            .setPositiveButton(R.string.aceptar) { _, _ ->
                peliculas.clear()
                adapter.notifyDataSetChanged()
                Snackbar.make(recycler, R.string.lista_eliminada, Snackbar.LENGTH_SHORT).show()
            }
            .show()
    }

    // ---------- Menú contextual ----------
    override fun onContextItemSelected(item: MenuItem): Boolean {
        val pos = posicionSeleccionada
        if (pos !in peliculas.indices) return super.onContextItemSelected(item)
        return when (item.itemId) {
            R.id.menu_eliminar -> {
                confirmarEliminar(pos)
                true
            }
            R.id.menu_editar -> {
                val intent = Intent(this, EditarActivity::class.java)
                intent.putExtra(EditarActivity.EXTRA_PELICULA, peliculas[pos])
                editarLauncher.launch(intent)
                true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun confirmarEliminar(pos: Int) {
        val pelicula = peliculas[pos]
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.eliminar_pelicula, pelicula.nombre))
            .setMessage(getString(R.string.confirmar_eliminar, pelicula.nombre))
            .setNegativeButton(R.string.cerrar, null)
            .setPositiveButton(R.string.aceptar) { _, _ ->
                peliculas.removeAt(pos)
                adapter.notifyItemRemoved(pos)
                adapter.notifyItemRangeChanged(pos, peliculas.size - pos)
            }
            .show()
    }

    companion object {
        private const val KEY_LISTA = "lista_peliculas"
    }
}
