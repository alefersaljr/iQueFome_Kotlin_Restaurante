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
import kotlinx.android.synthetic.main.activity_restaurante_meus_dados.*

class RestauranteMeusDados : AppCompatActivity() {

    lateinit var mToolbar: Toolbar
    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_meus_dados)

        mToolbar = findViewById(R.id.meusDados_Toolbar)
        setSupportActionBar(mToolbar)

        carregaDadosUsuario()

        meusDados_Button_EditarDados.setOnClickListener {
            Log.d("MeusDados", "Botão editar foi acionado")
            Toast.makeText(this, "Edição de Dados em desenvolvimento, aguarde", Toast.LENGTH_SHORT).show()
        }
    }

    private fun carregaDadosUsuario() {
        val tipoUsuario = "Gerente"

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
            R.id.action_menu_icon_logout -> {
                mAuth.signOut()
                startActivity(Intent(this, RestauranteLogin::class.java))
                finish()
            }
        }
        return true
    }
}
