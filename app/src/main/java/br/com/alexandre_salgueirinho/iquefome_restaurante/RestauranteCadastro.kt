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
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Restaurante
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_restaurante_cadastro.*
import java.lang.Exception

class RestauranteCadastro : AppCompatActivity() {

    var tipoCadastro = "Funcionário"
    lateinit var mToolbar: Toolbar
    var mAuth = FirebaseAuth.getInstance()
    var mDatabase = FirebaseDatabase.getInstance()

    lateinit var option : Spinner
    lateinit var cargoOption : Spinner
    var cargo = "Selecione"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_cadastro)

        mToolbar = findViewById(R.id.cadastro_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        singupType()

        getCargoOptions()

        cadastro_Button_Cadastrar.setOnClickListener {
            doCadastro()
        }
    }

    private fun singupType() {
        option = findViewById(R.id.cadastro_Spinner)

        val options = arrayOf("Funcionário", "Estabelecimento")

        option.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options)

        option.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                tipoCadastro = "Funcionário"
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tipoCadastro = options.get(position)

                if(tipoCadastro == "Estabelecimento"){
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
                if (tipoCadastro == "Funcionário") {
                    val email = cadastro_Funcionario_Text_Email.text.toString()
                    val senha = cadastro_Funcionario_Text_Senha.text.toString()

                    Log.d("Cadastros", "Tipo: $tipoCadastro, Email: $email, Senha: $senha")

                    novoCadastro(email, senha)
                }

                if (tipoCadastro == "Estabelecimento") {
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
        val uName = if (tipoCadastro == "Funcionário") (cadastro_Funcionario_Text_Nome.text.toString() + " " + cadastro_Funcionario_Text_Sobrenome.text.toString()) else (cadastro_Restaurante_Text_Nome.text.toString())
        val ref = mDatabase.getReference("/users/cadastros/restaurantes/$tipoCadastro/$uName")

        try {
            if (tipoCadastro == "Funcionário") {
                goToOperadorBuilder(uid, ref)
            } else {
                goToRestauranteBuilder(uid, ref)
            }
        } catch (ex: Exception) {
            Log.d("Cadastros", "Catch - ${ex.message}, $uName")
        }
    }

    private fun goToRestauranteBuilder(uid: String, ref: DatabaseReference) {
        val user = Restaurante(
            uid,
            cadastro_Restaurante_Text_Nome.text.toString(),
            cadastro_Restaurante_Text_CEP.text.toString(),
            cadastro_Restaurante_Text_Cidade.text.toString(),
            cadastro_Restaurante_Text_Rua.text.toString(),
            cadastro_Restaurante_Text_Numero.text.toString(),
            cadastro_Restaurante_Text_Email.text.toString(),
            cadastro_Restaurante_Text_Senha.text.toString(),
            tipoCadastro
        )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Cadastros", "Sucesso - Cadastro total de usuário completo")
            }.addOnFailureListener {
                Log.d("Cadastros", "Fail - ${it.message}")
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun goToOperadorBuilder(uid: String, ref: DatabaseReference) {
        val user = Operador(
            uid,
            cadastro_Funcionario_Text_Nome.text.toString(),
            cadastro_Funcionario_Text_Sobrenome.text.toString(),
            cadastro_Funcionario_Text_Celular.text.toString(),
            cargo,
            cadastro_Funcionario_Text_Nome_Restaurante.text.toString(),
            cadastro_Funcionario_Text_Email.text.toString(),
            cadastro_Funcionario_Text_Senha.text.toString(),
            tipoCadastro
        )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Cadastros", "Sucesso - Cadastro total de usuário completo")
            }.addOnFailureListener {
                Log.d("Cadastros", "Fail - ${it.message}")
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun validaCampos(tipoCadastro: String): Boolean {

        if (tipoCadastro == "Estabelecimento") {
            if (!(cadastro_Restaurante_Text_Nome.text.isEmpty() ||
                        cadastro_Restaurante_Text_CEP.text.isEmpty() ||
                        cadastro_Restaurante_Text_Cidade.text.isEmpty() ||
                        cadastro_Restaurante_Text_Rua.text.isEmpty() ||
                        cadastro_Restaurante_Text_Numero.text.isEmpty() ||
                        cadastro_Restaurante_Text_Email.text.isEmpty() ||
                        cadastro_Restaurante_Text_Senha.text.isEmpty())
            ) return true
        } else if (tipoCadastro == "Funcionário") {
            if (cargo == "Selecione") Toast.makeText(this, "Selecione um cargo", Toast.LENGTH_SHORT).show()
            else if (!(cadastro_Funcionario_Text_Nome.text.isEmpty() ||
                        cadastro_Funcionario_Text_Sobrenome.text.isEmpty() ||
                        cadastro_Funcionario_Text_Celular.text.isEmpty() ||
                        cadastro_Funcionario_Text_Nome_Restaurante.text.isEmpty() ||
                        cadastro_Funcionario_Text_Email.text.isEmpty() ||
                        cadastro_Funcionario_Text_Senha.text.isEmpty())
            ) return true
        }
        return false
    }

    private fun getCargoOptions() {
        val cargoOptions = arrayOf("Selecione", "Operador", "Gerente")

        cargoOption = findViewById(R.id.cadastro_Funcionario_Text_Cargo)

        cargoOption.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cargoOptions)

        cargoOption.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@RestauranteCadastro, "Por favor, selecione um cargo", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                cargo = cargoOptions.get(position)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}



//https://maps.googleapis.com/maps/api/directions/json?origin=Disneyland&destination=Universal+Studios+Hollywood&key=YOUR_API_KEY