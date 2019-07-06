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
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Pratos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_restaurante_gerente_logado.*

class RestauranteGerenteLogado : AppCompatActivity() {

    lateinit var mToolbar: Toolbar
    var mAuth = FirebaseAuth.getInstance()
    var currentUser = mAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_gerente_logado)

        logado_ProgressBar.visibility = View.VISIBLE

        mToolbar = findViewById(R.id.logado_Toolbar)
        setSupportActionBar(mToolbar)

        logado_Button_Add.setOnClickListener {
            goToAddFood()
        }

        logado_swipeRefresh.setOnRefreshListener {
            Handler(Looper.getMainLooper()).postDelayed({
                carregaPratos()
                logado_RecyclerView.adapter?.notifyDataSetChanged()
                logado_swipeRefresh.isRefreshing = false
            }, 3000)
        }

        carregaPratos()
    }

    override fun onResume() {
        super.onResume()

        carregaPratos()
    }

    companion object{
        val PRATO_KEY = "PRATO_KEY"
    }

    private fun carregaPratos() {
        val ref = FirebaseDatabase.getInstance().getReference("/pratos/restaurantes/${currentUser?.uid}")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("RestauranteGerenteLogad", it.toString())
                    val pratoItem = it.getValue(Pratos::class.java)

                    if (pratoItem != null) {
                        adapter.add(PratoItem(pratoItem))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    logado_ProgressBar.visibility = View.VISIBLE

                    val pratoI = item as PratoItem

                    val intent = Intent(view.context, RestauranteGerenteDetalhesPrato::class.java)
                    intent.putExtra(PRATO_KEY, pratoI.prato)

                    logado_ProgressBar.visibility = View.GONE
                    startActivity(intent)

//                    Toast.makeText(this@RestauranteGerenteLogado, "Em desenvolvimento, aguarde", Toast.LENGTH_SHORT).show()
                }
                logado_RecyclerView.adapter = adapter

                logado_ProgressBar.visibility = View.GONE
            }
            override fun onCancelled(p0: DatabaseError) {
                logado_ProgressBar.visibility = View.GONE }
        })
    }

    override fun onStart() {
        super.onStart()

        if (currentUser == null){
            startActivity(Intent(this, RestauranteLogin::class.java))
            finish()
        }
    }

    private fun goToAddFood() {
        val intent = Intent (this, MainActivity::class.java)
        startActivity(intent)
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
