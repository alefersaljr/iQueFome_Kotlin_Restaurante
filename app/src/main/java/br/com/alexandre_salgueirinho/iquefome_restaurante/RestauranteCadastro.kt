package br.com.alexandre_salgueirinho.iquefome_restaurante

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Operador
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Gerente
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_restaurante_cadastro.*
import java.lang.Exception

class RestauranteCadastro : AppCompatActivity() {

    var tipoCadastro = "Operador"
    lateinit var mToolbar: Toolbar
    var mAuth = FirebaseAuth.getInstance()
    var mDatabase = FirebaseDatabase.getInstance()

    lateinit var option : Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_cadastro)

        mToolbar = findViewById(R.id.cadastro_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        singupType()

        cadastro_Button_Cadastrar.setOnClickListener {
            doCadastro()
        }
    }

    private fun singupType() {
        option = this.findViewById(R.id.cadastro_Spinner)

        val options = arrayOf("Operador", "Gerente")

        option.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                tipoCadastro = "Operador"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tipoCadastro = options.get(position)

                if(tipoCadastro == "Gerente"){
                    cadastro_Layout_Funcionario.visibility = View.GONE
                    cadastro_Layout_Restaurante.visibility = View.VISIBLE
                }else {

                    cadastro_Layout_Restaurante.visibility = View.GONE
                    cadastro_Layout_Funcionario.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun doCadastro() {
        try {
            if (validaCampos(tipoCadastro)) {
                cadastro_ProgressBar.visibility = View.VISIBLE
                if (tipoCadastro == "Operador") {
                    val email = cadastro_Funcionario_Text_Email.text.toString()
                    val senha = cadastro_Funcionario_Text_Senha.text.toString()

                    Log.d("Cadastros", "Tipo: $tipoCadastro, Email: $email, Senha: $senha")

                    novoCadastro(email, senha)
                }

                if (tipoCadastro == "Gerente") {
                    val email = cadastro_Gerente_Text_Email.text.toString()
                    val senha = cadastro_Gerente_Text_Senha.text.toString()

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
                    cadastro_ProgressBar.visibility = View.GONE
                }

        } catch (ex: Exception) {
            Toast.makeText(this, "${ex.message}", Toast.LENGTH_SHORT).show()
            Log.d("Cadastros", "ERROR, ${ex.message}")
            cadastro_ProgressBar.visibility = View.GONE
        }
    }

    private fun uploadImage() {
        saveUserToFirebaseDatabase()
    }

    private fun saveUserToFirebaseDatabase() {
        val userId = mAuth.uid ?: ""
        val uName = if (tipoCadastro == "Operador") (cadastro_Funcionario_Text_Nome.text.toString() + " " + cadastro_Funcionario_Text_Sobrenome.text.toString()) else (cadastro_Restaurante_Text_Nome.text.toString())
        val ref = mDatabase.getReference("/users/cadastros/restaurantes/$tipoCadastro/$userId")
        val refGeral = mDatabase.getReference("/users/cadastros/restaurantes/geral/$userId")

        try {
            if (tipoCadastro == "Operador") {
                goToOperadorBuilder(userId, ref, refGeral)
            } else {
                goToGerenteBuilder(userId, ref, refGeral)
            }
        } catch (ex: Exception) {
            Log.d("Cadastros", "Catch - ${ex.message}, $uName")
            cadastro_ProgressBar.visibility = View.GONE
        }
    }

    private fun goToGerenteBuilder(userId: String, ref: DatabaseReference, refGeral: DatabaseReference) {
        val user = Gerente(
            userId,
            cadastro_Gerente_Text_Nome.text.toString(),
            cadastro_Gerente_Text_Sobrenome.text.toString(),
            cadastro_Gerente_Text_Celular.text.toString(),
            cadastro_Gerente_Text_Email.text.toString(),
            cadastro_Gerente_Text_Senha.text.toString(),
            cadastro_Restaurante_Text_Nome.text.toString(),
            cadastro_Restaurante_Text_Celular.text.toString(),
            cadastro_Restaurante_Text_CEP.text.toString(),
            cadastro_Restaurante_Text_Cidade.text.toString(),
            cadastro_Restaurante_Text_Rua.text.toString(),
            cadastro_Restaurante_Text_Numero.text.toString(),
            cadastro_Restaurante_Text_Email.text.toString(),
            tipoCadastro
        )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Cadastros", "Sucesso - Cadastro total de usuário completo")
                cadastro_ProgressBar.visibility = View.GONE
                finish()
            }.addOnFailureListener {
                Log.d("Cadastros", "Fail - ${it.message}")
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }

        refGeral.setValue(user)
            .addOnSuccessListener {
                Log.d("Cadastros", "Sucesso - Cadastro total de usuário completo")
                cadastro_ProgressBar.visibility = View.GONE
                finish()
            }.addOnFailureListener {
                Log.d("Cadastros", "Fail - ${it.message}")
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }

        cadastro_ProgressBar.visibility = View.GONE
    }

    private fun goToOperadorBuilder(userId: String, ref: DatabaseReference, refGeral: DatabaseReference) {
        val user = Operador(
            userId,
            cadastro_Funcionario_Text_Nome.text.toString(),
            cadastro_Funcionario_Text_Sobrenome.text.toString(),
            cadastro_Funcionario_Text_Celular.text.toString(),
            tipoCadastro,
            cadastro_Funcionario_Text_Nome_Restaurante.text.toString(),
            cadastro_Funcionario_Text_Email.text.toString(),
            cadastro_Funcionario_Text_Senha.text.toString(),
            tipoCadastro
        )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Cadastros", "Sucesso - Cadastro total de usuário completo")
                cadastro_ProgressBar.visibility = View.GONE
                finish()
            }.addOnFailureListener {
                Log.d("Cadastros", "Fail - ${it.message}")
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }

        refGeral.setValue(user)
            .addOnSuccessListener {
                Log.d("Cadastros", "Sucesso - Cadastro total de usuário completo")
                cadastro_ProgressBar.visibility = View.GONE
                finish()
            }.addOnFailureListener {
                Log.d("Cadastros", "Fail - ${it.message}")
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }

        cadastro_ProgressBar.visibility = View.GONE
    }

    private fun validaCampos(tipoCadastro: String): Boolean {

        if (tipoCadastro == "Gerente") {
            if (!(cadastro_Gerente_Text_Nome.text.isEmpty() ||
                        cadastro_Gerente_Text_Sobrenome.text.isEmpty() ||
                        cadastro_Gerente_Text_Celular.text.isEmpty() ||
                        cadastro_Gerente_Text_Email.text.isEmpty() ||
                        cadastro_Gerente_Text_Senha.text.isEmpty() ||
                        cadastro_Restaurante_Text_Nome.text.isEmpty() ||
                        cadastro_Restaurante_Text_CEP.text.isEmpty() ||
                        cadastro_Restaurante_Text_Cidade.text.isEmpty() ||
                        cadastro_Restaurante_Text_Rua.text.isEmpty() ||
                        cadastro_Restaurante_Text_Numero.text.isEmpty() ||
                        cadastro_Restaurante_Text_Email.text.isEmpty())
            ) return true
        } else if (tipoCadastro == "Operador") {
            if (!(cadastro_Funcionario_Text_Nome.text.isEmpty() ||
                        cadastro_Funcionario_Text_Sobrenome.text.isEmpty() ||
                        cadastro_Funcionario_Text_Celular.text.isEmpty() ||
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



//https://maps.googleapis.com/maps/api/directions/json?origin=Disneyland&destination=Universal+Studios+Hollywood&key=YOUR_API_KEY