package br.com.alexandre_salgueirinho.iquefome_restaurante

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Reservas
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_restaurante_operador_logado.*

class RestauranteOperadorLogado : AppCompatActivity() {

    lateinit var mToolbar: Toolbar
    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_operador_logado)

        reservas_ProgressBar.visibility = View.VISIBLE

        mToolbar = findViewById(R.id.reservas_recebidas_Toolbar)
        setSupportActionBar(mToolbar)

        reservar_recebidas_Refresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                carregaReservas()
                reservas_RecyclerView.adapter?.notifyDataSetChanged()
                reservar_recebidas_Refresh.isRefreshing = false
            }, 3000)
        }

        carregaReservas()
    }

    private fun carregaReservas() {
        val ref = FirebaseDatabase.getInstance().getReference("/reservas/restaurante")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("Reservas", it.toString())
                    val reservaItem = it.getValue(Reservas::class.java)

                    if (reservaItem != null) {
                        adapter.add(ReservaItem(reservaItem))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    reservas_ProgressBar.visibility = View.VISIBLE

                    val reservaIt = item as ReservaItem

                    val intent = Intent(view.context, RestauranteOperadorDetalhesReserva::class.java)
                    intent.putExtra(RESERVA_KEY, reservaIt.reserva)

                    reservas_ProgressBar.visibility = View.GONE
                    startActivity(intent)

//                    Toast.makeText(this@RestauranteGerenteLogado, "Em desenvolvimento, aguarde", Toast.LENGTH_SHORT).show()
                }
                reservas_RecyclerView.adapter = adapter

                reservas_ProgressBar.visibility = View.GONE
            }
            override fun onCancelled(p0: DatabaseError) {
                reservas_ProgressBar.visibility = View.GONE }
        })
    }

    companion object{
        val RESERVA_KEY = "RESERVA_KEY"
    }

    override fun onResume() {
        super.onResume()

        carregaReservas()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu_account, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_menu_icon_account -> {
                startActivity(Intent(this, RestauranteMeusDados::class.java))
            }
        }
        return true
    }
}
