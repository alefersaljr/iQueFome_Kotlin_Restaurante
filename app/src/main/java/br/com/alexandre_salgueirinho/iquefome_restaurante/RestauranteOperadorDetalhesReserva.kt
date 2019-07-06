package br.com.alexandre_salgueirinho.iquefome_restaurante

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Reservas
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_restaurante_gerente_detalhes_prato.*
import kotlinx.android.synthetic.main.activity_restaurante_operador_detalhes_reserva.*

class RestauranteOperadorDetalhesReserva : AppCompatActivity() {

    private lateinit var mToolbar: androidx.appcompat.widget.Toolbar

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

    private fun goToExclude(reserva_Id: String) {

        val builder = AlertDialog.Builder(this@RestauranteOperadorDetalhesReserva)
        builder.setTitle("Deseja mesmo excluir esta reserva???")
        builder.setMessage("!!! CUIDADO !!!                                    Ela será excluida permanentemente")
        builder.setPositiveButton("Excluir") { dialog: DialogInterface, id: Int ->
            val ref = FirebaseDatabase.getInstance().getReference("/reservas/restaurante/$reserva_Id")

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

                        reserva_Cliente_Data_Hora.text = reserva.reserva_Hora
                        reserva_Cliente_Data_Nome_Cliente.text = reserva.cliente_Nome
                        reserva_Cliente_Data_Sobrenome.text = reserva.cliente_Sobrenome
                        reserva_Cliente_Data_Nome_Prato.text = reserva.prato_Nome
                        reserva_Cliente_Data_Preco_Prato.text = reserva.prato_Preco
                        reserva_alteracao_Data.text = reserva.cliente_Alteracao
                        reserva_Cliente_Numero_Pontuacao.text = reserva.cliente_pontos


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
