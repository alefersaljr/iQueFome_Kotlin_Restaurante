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
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Gerente_Intermediario
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Operador
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Gerente
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_restaurante_meus_dados.*
import java.lang.Exception

class RestauranteMeusDados : AppCompatActivity() {

    val idBackButton = 16908332
    lateinit var mToolbar: Toolbar
    var mAuth = FirebaseAuth.getInstance()
    var db = FirebaseDatabase.getInstance()
    var tipoUsuario = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_meus_dados)

        mToolbar = findViewById(R.id.meusDados_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        meusDados_ProgressBar.visibility = View.VISIBLE

        carregaDadosUsuario()

        meusDados_Button_EditarDados.setOnClickListener {
            Log.d("MeusDados", "Botão editar foi acionado")

            val intent = Intent(this, RestauranteEditarDados::class.java)
            intent.putExtra("tipoUser", tipoUsuario)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()

        carregaDadosUsuario()
    }

    private fun carregaDadosUsuario() {
        val userId = mAuth.currentUser?.uid
        val ref = db.getReference("/users/cadastros/restaurantes/geral/$userId")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val userIntermediario = p0.getValue(Gerente_Intermediario::class.java)

                try {
                    if (userIntermediario != null) {
                        tipoUsuario = userIntermediario.tipoUser

                        if (tipoUsuario.equals("Operador")) {
                            val userOperador = p0.getValue(Operador::class.java)

                            if (userOperador != null) {
                                meusDados_Layout_Operador.visibility = View.VISIBLE
                                Log.d("MeusDados", "Usuário do tipo: $tipoUsuario")

                                meusDados_Operador_Data_Nome.text = userOperador.operadorNome
                                meusDados_Operador_Data_Sobrenome.text = userOperador.operadorSobrenome
                                meusDados_Operador_Data_Celular.text = userOperador.operadorCelular
                                meusDados_Operador_Data_Cargo.text = userOperador.operadorCargo
                                meusDados_Operador_Data_Nome_Restaurante.text = userOperador.operadorNomeRestautante
                                meusDados_Operador_Data_Email.text = userOperador.operadorEmail
                            }
                        } else if (tipoUsuario.equals("Gerente")) {
                            val userGerente = p0.getValue(Gerente::class.java)

                            if (userGerente != null) {
                                meusDados_Layout_Gerente.visibility = View.VISIBLE
                                Log.d("MeusDados", "Usuário do tipo: $tipoUsuario")

                                meusDados_Gerente_Data_Nome.text = userGerente.gerente_Nome
                                meusDados_Gerente_Data_Sobrenome.text = userGerente.gerente_Sobrenome
                                meusDados_Gerente_Data_Celular.text = userGerente.gerente_Celular
                                meusDados_Gerente_Data_Email.text = userGerente.gerente_Email
                                meusDados_Restaurante_Data_Nome.text = userGerente.restaurante_Nome
                                meusDados_Restaurante_Data_Celular.text = userGerente.restaurante_Celular
                                meusDados_Restaurante_Data_CEP.text = userGerente.restaurante_CEP
                                meusDados_Restaurante_Data_Rua.text = userGerente.restaurante_Rua
                                meusDados_Restaurante_Data_Cidade.text = userGerente.restaurante_Cidade
                                meusDados_Restaurante_Data_Numero.text = userGerente.restaurante_Complemento
                                meusDados_Restaurante_Data_Email.text = userGerente.restaurante_Email
                            }
                        }

                        meusDados_ProgressBar.visibility = View.GONE
                    }
                } catch (ex: Exception) {
                    Toast.makeText(this@RestauranteMeusDados, "Erro. ${ex.message}", Toast.LENGTH_SHORT).show()
                    meusDados_ProgressBar.visibility = View.GONE
                    return
                }
            }
        })


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
