package br.com.alexandre_salgueirinho.iquefome_restaurante

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_restaurante_logado.*

class RestauranteLogado : AppCompatActivity() {

    lateinit var mToolbar: Toolbar
    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_logado)

        mToolbar = findViewById(R.id.logado_Toolbar)
        setSupportActionBar(mToolbar)

        logado_Button_Add.setOnClickListener {
            goToAddFood()
        }

        carregaPratos()
    }

    companion object{
        val PRATO_KEY = "PRATO_KEY"
    }

    private fun carregaPratos() {
        logado_ProgressBar.visibility = View.GONE
        val ref = FirebaseDatabase.getInstance().getReference("/pratos/clientes")
        ref.addListenerForSingleValueEvent(object : ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach {
                    Log.d("RestauranteLogado", it.toString())
                    val pratoItem = it.getValue(Pratos::class.java)

                    if (pratoItem != null) {
                        adapter.add(PratoItem(pratoItem))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    logado_ProgressBar.visibility = View.VISIBLE

                    val pratoI = item as PratoItem

//                    val intent = Intent(view.context, ClientePratoComposicao::class.java)
//                    intent.putExtra(PRATO_KEY, pratoI.prato)

                    logado_ProgressBar.visibility = View.GONE
//                    startActivity(intent)

                    Toast.makeText(this@RestauranteLogado, "Em desenvolvimento, aguarde", Toast.LENGTH_SHORT).show()
                }
                logado_RecyclerView.adapter = adapter
            }
            override fun onCancelled(p0: DatabaseError) {}
        })
    }

    override fun onStart() {
        super.onStart()

        if (mAuth.currentUser == null){
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
