package br.com.alexandre_salgueirinho.iquefome_restaurante

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Reservas
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Usuário
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_restaurante_gerente_detalhes_prato.*
import kotlinx.android.synthetic.main.activity_restaurante_operador_detalhes_reserva.*

class RestauranteOperadorDetalhesReserva : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar
    lateinit var refCliente: DatabaseReference

    //region Cliente Fields

    lateinit var cliente: Usuário

    var cliente_Id = ""
    var cliente_Nome = ""
    var cliente_Sobrenome = ""
    var cliente_Email = ""
    var cliente_Password = ""
    var cliente_Celular = ""
    var cliente_Indicado = ""
    var cliente_UrlImagemPerfil = ""
    var cliente_Ponto = "0"
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_operador_detalhes_reserva)

        mToolbar = findViewById(R.id.reserva_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        val reserva = intent.getParcelableExtra<Reservas>(RestauranteOperadorLogado.RESERVA_KEY)

        getReserva(reserva.reserva_Id)

        reserva_Button_Excluir.setOnClickListener {
            goToExclude(reserva.reserva_Id)
        }
    }

    private fun getClienteData(reserva_Id: String) {
        refCliente = FirebaseDatabase.getInstance().getReference("/users/cadastros/clientes/$cliente_Id")

        refCliente.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                cliente = p0.getValue(Usuário::class.java)!!

                try {
                        cliente_Nome = cliente.cliente_Nome
                        cliente_Sobrenome = cliente.cliente_Sobrenome
                        cliente_Email = cliente.cliente_Email
                        cliente_Password = cliente.cliente_Password
                        cliente_Celular = cliente_Celular
                        cliente_Indicado = cliente.cliente_Indicado
                        cliente_UrlImagemPerfil = cliente.cliente_UrlImagemPerfil

                } catch (ex: Exception) {
                    Toast.makeText(applicationContext, "${ex.message}", Toast.LENGTH_SHORT).show()
//                    composition_ProgressBar.visibility = View.GONE
                }
            }
        })

    }

    private fun goToExclude(reserva_Id: String) {

        val builder = AlertDialog.Builder(this@RestauranteOperadorDetalhesReserva)
        builder.setTitle("Deseja mesmo excluir esta reserva???")
        builder.setMessage("!!! CUIDADO !!!                                    Ela será excluida permanentemente")
        builder.setPositiveButton("Excluir") { dialog: DialogInterface, id: Int ->
            Toast.makeText(this, "Excluindo Reserva...", Toast.LENGTH_SHORT).show()

            val ref = FirebaseDatabase.getInstance().getReference("/reservas/restaurante/$reserva_Id")

            cliente.cliente_Pontos = cliente_Ponto

            refCliente.setValue(cliente)
                .addOnSuccessListener {
                    Log.d("Cadastros", "Sucesso - Cadastro total de usuário completo")
                }.addOnFailureListener {
                    Log.d("Cadastros", "Fail - ${it.message}")
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }

            ref.removeValue()
            Toast.makeText(this, "Reserva Excluida com sucesso", Toast.LENGTH_SHORT).show()
            finish()
        }
        builder.setNegativeButton("Cancelar") { dialog: DialogInterface, id: Int ->
            dialog.dismiss()
        }

        builder.show()
    }

    private fun getReserva(reserva_Id: String) {
        val ref = FirebaseDatabase.getInstance().getReference("/reservas/restaurante/$reserva_Id")

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val reserva = p0.getValue(Reservas::class.java)

                try {
                    if (reserva != null) {

                        cliente_Id = reserva.cliente_Id
                        reserva_Cliente_Data_Hora.text = reserva.reserva_Hora
                        reserva_Cliente_Data_Nome_Cliente.text = reserva.cliente_Nome
                        reserva_Cliente_Data_Sobrenome.text = reserva.cliente_Sobrenome
                        reserva_Cliente_Data_Nome_Prato.text = reserva.prato_Nome
                        reserva_Cliente_Data_Preco_Prato.text = reserva.prato_Preco
                        reserva_alteracao_Data.text = reserva.cliente_Alteracao
                        reserva_Cliente_Numero_Pontuacao.text = reserva.cliente_pontos

                        getClienteData(reserva_Id)


                        val handler = Handler()
                        handler.postDelayed(object : Runnable {
                            override fun run() {
                                reserva_ProgressBar.visibility = View.GONE
                            }
                        }, 2000)

                    } else {
                        Toast.makeText(this@RestauranteOperadorDetalhesReserva, "Erro", Toast.LENGTH_SHORT).show()
                        reserva_ProgressBar.visibility = View.GONE
                    }
                } catch (ex: Exception) {
                    Toast.makeText(this@RestauranteOperadorDetalhesReserva, "${ex.message}", Toast.LENGTH_SHORT).show()
                    reserva_ProgressBar.visibility = View.GONE
                }
            }
        })
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
