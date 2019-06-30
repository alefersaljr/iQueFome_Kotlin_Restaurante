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
import kotlinx.android.synthetic.main.activity_restaurante_meus_dados.*

class RestauranteMeusDados : AppCompatActivity() {

    val idBackButton = 16908332
    lateinit var mToolbar: Toolbar
    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_meus_dados)

        mToolbar = findViewById(R.id.meusDados_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        carregaDadosUsuario()

        meusDados_Button_EditarDados.setOnClickListener {
            Log.d("MeusDados", "Botão editar foi acionado")
            Toast.makeText(this, "Edição de Dados em desenvolvimento, aguarde", Toast.LENGTH_SHORT).show()
        }
    }

    private fun carregaDadosUsuario() {
//        var db = FirebaseDatabase.getInstance().getReference("/users/cadastros/restaurantes")
//        var users = db.orderByChild("Id").equalTo(mAuth.currentUser?.uid).limitToFirst(1)
        var tipoUsuario = "Operadors"

//        users.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onCancelled(p0: DatabaseError) { }
//
//            override fun onDataChange(p0: DataSnapshot) {
//                p0.getValue()
//            }
//        })

        if (tipoUsuario == "Operador"){
            meusDados_Layout_Operador.visibility = View.VISIBLE
            Log.d("MeusDados", "Usuário do tipo: $tipoUsuario")
        } else if (tipoUsuario == "Gerente"){
            meusDados_Layout_Gerente.visibility = View.VISIBLE
            Log.d("MeusDados", "Usuário do tipo: $tipoUsuario")
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu_logout, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            idBackButton -> onBackPressed()
            R.id.action_menu_icon_logout -> {
                mAuth.signOut()
                startActivity(Intent(this, RestauranteLogin::class.java))
                finish()
            }
        }
        return true
    }
}
