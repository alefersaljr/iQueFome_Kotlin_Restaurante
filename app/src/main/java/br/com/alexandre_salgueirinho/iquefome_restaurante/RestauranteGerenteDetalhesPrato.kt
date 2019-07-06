package br.com.alexandre_salgueirinho.iquefome_restaurante

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.View
import android.widget.Toast
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Pratos
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_restaurante_gerente_detalhes_prato.*

class RestauranteGerenteDetalhesPrato : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_gerente_detalhes_prato)

        mToolbar = findViewById(R.id.detalhes_prato_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val prato = intent.getParcelableExtra<Pratos>(RestauranteGerenteLogado.PRATO_KEY)

        getPrato(prato.pratoId)

//        detalhes_prato_Edit.setOnClickListener {
//            Toast.makeText(this, "Em desenvolvimento, aguarde", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun getPrato(pratoID: String) {

        val ref = FirebaseDatabase.getInstance().getReference("/pratos/restaurantes/${mAuth.currentUser?.uid}/$pratoID")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) { }

            override fun onDataChange(p0: DataSnapshot) {
                val prato = p0.getValue(Pratos::class.java)

                try {
                    if (prato != null) {
                        val preco = "R\$" + prato.pratoPreco

                        detalhes_prato_Prato_Nome.text = prato.pratoNome
                        detalhes_prato_Prato_Descricao_Data.text = prato.pratoDescricao
                        detalhes_prato_Prato_Preco.text = preco
                        tag_TipoPrato.text = prato.pratoTipo
                        tag_TipoComida.text = prato.pratoTipoComida

                        Picasso.get().load(prato.pratoUrlFoto).into(detalhes_prato_Prato_Foto)

                        val handler = Handler()
                        handler.postDelayed(object : Runnable {
                            override fun run() {
                                detalhes_prato_ProgressBar.visibility = View.GONE
                            }
                        }, 2000)

                    } else {
                        Toast.makeText(this@RestauranteGerenteDetalhesPrato, "Erro", Toast.LENGTH_SHORT).show()
                        detalhes_prato_ProgressBar.visibility = View.GONE
                    }
                } catch (ex: Exception) {
                    Toast.makeText(this@RestauranteGerenteDetalhesPrato, "${ex.message}", Toast.LENGTH_SHORT).show()
                    detalhes_prato_ProgressBar.visibility = View.GONE
                }
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        finish()
        return true
    }

}