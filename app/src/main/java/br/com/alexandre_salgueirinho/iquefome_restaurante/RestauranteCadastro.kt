package br.com.alexandre_salgueirinho.iquefome_restaurante

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Operador
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Restaurante
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_restaurante_cadastro.*
import java.lang.Exception

class RestauranteCadastro : AppCompatActivity() {

    var tipoCadastro = "Funcionário"
    lateinit var mToolbar: Toolbar
    var mAuth = FirebaseAuth.getInstance()
    var mDatabase = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_cadastro)

        mToolbar = findViewById(R.id.cadastro_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        cadastro_Switch_Button.setOnClickListener {
            chooseUserType()
        }

        cadastro_Button_Cadastrar.setOnClickListener {
            doCadastro()
        }
    }

    private fun chooseUserType() {
        if (cadastro_Switch_Button.isChecked) {
            tipoCadastro = "Restaurante"
            Toast.makeText(this, "$tipoCadastro", Toast.LENGTH_SHORT).show()

            cadastro_Layout_Funcionario.visibility = View.GONE
            cadastro_Layout_Restaurante.visibility = View.VISIBLE
        } else {
            cadastro_Layout_Restaurante.visibility = View.GONE
            cadastro_Layout_Funcionario.visibility = View.VISIBLE

            tipoCadastro = "Funcionário"
            Toast.makeText(this, "$tipoCadastro", Toast.LENGTH_SHORT).show()
        }
    }

    private fun doCadastro() {
        try {
            if (validaCampos(tipoCadastro)) {
                if (tipoCadastro == "Funcionário") {
                    val email = cadastro_Funcionario_Text_Email.text.toString()
                    val senha = cadastro_Funcionario_Text_Senha.text.toString()

                    Log.d("Cadastros", "Tipo: $tipoCadastro, Email: $email, Senha: $senha")

                    novoCadastro(email, senha)
                }

                if (tipoCadastro == "Restaurante") {
                    val email = cadastro_Restaurante_Text_Email.text.toString()
                    val senha = cadastro_Restaurante_Text_Senha.text.toString()

                    Log.d("Cadastros", "Tipo: $tipoCadastro, Email: $email, Senha: $senha")

                    novoCadastro(email, senha)
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(this, "${ex.message}", Toast.LENGTH_SHORT).show()
        }
    }

    private fun novoCadastro(email: String, senha: String) {
        try {

            mAuth.createUserWithEmailAndPassword(email, senha)
                .addOnSuccessListener {
                    Log.d("Cadastros", "Sucesso em criar usuário no Firebase")
                    uploadImage()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                    Log.d("Cadastros", "ERROR, ${it.message}")
                }

        } catch (ex: Exception) {
            Toast.makeText(this, "${ex.message}", Toast.LENGTH_SHORT).show()
            Log.d("Cadastros", "ERROR, ${ex.message}")
        }
    }

    private fun uploadImage() {
        saveUserToFirebaseDatabase()
    }

    private fun saveUserToFirebaseDatabase() {
        val uid = mAuth.uid ?: ""
        val ref = mDatabase.getReference("/$tipoCadastro/$uid")

        try {
            val user = if (tipoCadastro == "Operador") {
                Operador()
            } else {
                Restaurante()
            }

            ref.setValue(user)
                .addOnSuccessListener {
                    Log.d("Cadastros", "Cadastro total de usuário completo")
                }.addOnFailureListener {
                    Log.d("Cadastros", "${it.message}")
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                }
        } catch (ex: Exception) {
            Log.d("Cadastros", "${ex.message}")
        }
    }

    private fun validaCampos(tipoCadastro: String): Boolean {
        if (tipoCadastro == "Restaurante") {
            if (!(cadastro_Restaurante_Text_Nome.text.isEmpty() ||
                        cadastro_Restaurante_Text_CEP.text.isEmpty() ||
                        cadastro_Restaurante_Text_Cidade.text.isEmpty() ||
                        cadastro_Restaurante_Text_Rua.text.isEmpty() ||
                        cadastro_Restaurante_Text_Numero.text.isEmpty() ||
                        cadastro_Restaurante_Text_Email.text.isEmpty() ||
                        cadastro_Restaurante_Text_Senha.text.isEmpty())
            ) return true
        } else if (tipoCadastro == "Funcionário") {
            if (!(cadastro_Funcionario_Text_Nome.text.isEmpty() ||
                        cadastro_Funcionario_Text_Sobrenome.text.isEmpty() ||
                        cadastro_Funcionario_Text_Celular.text.isEmpty() ||
                        cadastro_Funcionario_Text_Cargo.text.isEmpty() ||
                        cadastro_Funcionario_Text_Nome_Restaurante.text.isEmpty() ||
                        cadastro_Funcionario_Text_Email.text.isEmpty() ||
                        cadastro_Funcionario_Text_Senha.text.isEmpty())
            ) return true
        }
        return false
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
