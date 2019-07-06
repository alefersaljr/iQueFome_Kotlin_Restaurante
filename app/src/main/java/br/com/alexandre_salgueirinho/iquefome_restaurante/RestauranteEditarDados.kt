package br.com.alexandre_salgueirinho.iquefome_restaurante

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Gerente
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Operador
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_restaurante_editar_dados.*
import java.lang.Exception

class RestauranteEditarDados : AppCompatActivity() {

    //    val idBackButton = 16908332
    lateinit var mToolbar: Toolbar
    var mAuth = FirebaseAuth.getInstance()
    var db = FirebaseDatabase.getInstance()
    var tipoUser = ""

//region var init operador

    var operador_Nome = ""
    var operador_Sobrenome = ""
    var operador_Celular = ""
    var operador_NomeRestaurante = ""
    var operador_Email = ""
    var operador_Senha = ""

// endregion

//region var init gerente

    var gerente_Nome = ""
    var gerente_Sobrenome = ""
    var gerente_Celular = ""
    var gerente_Email = ""
    var gerente_Senha = ""
    var restaurante_Nome = ""
    var restaurante_Celular = ""
    var restaurante_CEP = ""
    var restaurante_Cidade = ""
    var restaurante_Rua = ""
    var restaurante_Complemento = ""
    var restaurante_Email = ""

// endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_editar_dados)

        mToolbar = findViewById(R.id.editar_dados_Toolbar)
        setSupportActionBar(mToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tipoUser = intent.getStringExtra("tipoUser")

        val userId = mAuth.currentUser?.uid
        val refGeral = db.getReference("/users/cadastros/restaurantes/geral/$userId")
        val ref = db.getReference("/users/cadastros/restaurantes/$tipoUser/$userId")

        if (tipoUser.equals("Gerente")) {
            editar_dados_Layout_Gerente.visibility = View.VISIBLE
        } else if (tipoUser.equals("Operador")) {
            editar_dados_Layout_Funcionario.visibility = View.VISIBLE
        }

        getInformacoesUser(tipoUser, refGeral)

        editar_dados_Button_Editar.setOnClickListener {
            editar_dados_ProgressBar.visibility = View.VISIBLE
            getDataToUpdate()
            salvarDados(tipoUser, userId, ref, refGeral)
        }
    }

    private fun getDataToUpdate() {
        if (editar_dados_Funcionario_Text_Nome.text.toString().isNotEmpty()) {
            operador_Nome = editar_dados_Funcionario_Text_Nome.text.toString()
        }
        if (editar_dados_Funcionario_Text_Sobrenome.text.toString().isNotEmpty()){
            operador_Sobrenome = editar_dados_Funcionario_Text_Sobrenome.text.toString()
        }
        if (editar_dados_Funcionario_Text_Celular.text.toString().isNotEmpty()){
            operador_Celular = editar_dados_Funcionario_Text_Celular.text.toString()
        }
        if (editar_dados_Funcionario_Text_Nome_Restaurante.text.toString().isNotEmpty()){
            operador_NomeRestaurante = editar_dados_Funcionario_Text_Nome_Restaurante.text.toString()
        }
        if (editar_dados_Funcionario_Text_Email.text.toString().isNotEmpty()){
            operador_Email = editar_dados_Funcionario_Text_Email.text.toString()
        }
        if (editar_dados_Funcionario_Text_Senha.text.toString().isNotEmpty()){
            operador_Senha = editar_dados_Funcionario_Text_Senha.text.toString()
        }

        if (editar_dados_Gerente_Text_Nome.text.toString().isNotEmpty()){
            gerente_Nome = editar_dados_Gerente_Text_Nome.text.toString()
        }
        if (editar_dados_Gerente_Text_Sobrenome.text.toString().isNotEmpty()){
            gerente_Sobrenome = editar_dados_Gerente_Text_Sobrenome.text.toString()
        }
        if (editar_dados_Gerente_Text_Celular.text.toString().isNotEmpty()){
            gerente_Celular = editar_dados_Gerente_Text_Celular.text.toString()
        }
        if (editar_dados_Gerente_Text_Email.text.toString().isNotEmpty()){
            gerente_Email = editar_dados_Gerente_Text_Email.text.toString()
        }
        if (editar_dados_Gerente_Text_Senha.text.toString().isNotEmpty()){
            gerente_Senha = editar_dados_Gerente_Text_Senha.text.toString()
        }
        if (editar_dados_Restaurante_Text_Nome.text.toString().isNotEmpty()){
            restaurante_Nome = editar_dados_Restaurante_Text_Nome.text.toString()
        }
        if (editar_dados_Restaurante_Text_Celular.text.toString().isNotEmpty()){
            restaurante_Celular = editar_dados_Restaurante_Text_Celular.text.toString()
        }
        if (editar_dados_Restaurante_Text_CEP.text.toString().isNotEmpty()){
            restaurante_CEP = editar_dados_Restaurante_Text_CEP.text.toString()
        }
        if (editar_dados_Restaurante_Text_Cidade.text.toString().isNotEmpty()){
            restaurante_Cidade = editar_dados_Restaurante_Text_Cidade.text.toString()
        }
        if (editar_dados_Restaurante_Text_Rua.text.toString().isNotEmpty()){
            restaurante_Rua = editar_dados_Restaurante_Text_Rua.text.toString()
        }
        if (editar_dados_Restaurante_Text_Numero.text.toString().isNotEmpty()){
            restaurante_Complemento = editar_dados_Restaurante_Text_Numero.text.toString()
        }
        if (editar_dados_Restaurante_Text_Email.text.toString().isNotEmpty()){
            restaurante_Email = editar_dados_Restaurante_Text_Email.text.toString()
        }
    }

    private fun getInformacoesUser(tipoUser: String, refGeral: DatabaseReference) {
        val refGeral1 = db.getReference("/users/cadastros/restaurantes/geral/${mAuth.currentUser?.uid}")

        refGeral1.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                try {
                    if (tipoUser.equals("Operador")) {
                        val userOperador = p0.getValue(Operador::class.java)

                        if (userOperador != null) {
                            Log.d("MeusDados", "Usuário do tipo: $tipoUser")

                            operador_Nome = userOperador.operadorNome
                            operador_Sobrenome = userOperador.operadorSobrenome
                            operador_Celular = userOperador.operadorCelular
                            operador_NomeRestaurante = userOperador.operadorNomeRestautante
                            operador_Email = userOperador.operadorEmail
                            operador_Senha = userOperador.operadorSenha
                        }
                    } else if (tipoUser.equals("Gerente")) {
                        val userGerente = p0.getValue(Gerente::class.java)

                        if (userGerente != null) {
                            Log.d("MeusDados", "Usuário do tipo: $tipoUser")

                            gerente_Nome = userGerente.gerente_Nome
                            gerente_Sobrenome = userGerente.gerente_Sobrenome
                            gerente_Celular = userGerente.gerente_Celular
                            gerente_Email = userGerente.gerente_Email
                            gerente_Senha = userGerente.gerente_Senha
                            restaurante_Nome = userGerente.restaurante_Nome
                            restaurante_Celular = userGerente.restaurante_Celular
                            restaurante_CEP = userGerente.restaurante_CEP
                            restaurante_Rua = userGerente.restaurante_Rua
                            restaurante_Cidade = userGerente.restaurante_Cidade
                            restaurante_Complemento = userGerente.restaurante_Complemento
                            restaurante_Email = userGerente.restaurante_Email
                        }
                    }

                } catch (ex: Exception) {
                    Toast.makeText(this@RestauranteEditarDados, "Erro. ${ex.message}", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        })
    }

    private fun salvarDados(tipoUser: String, userId: String?, ref: DatabaseReference, refGeral: DatabaseReference) {

        editar_dados_ProgressBar.visibility = View.VISIBLE


        try {
            if (tipoUser == "Operador") {
                if (userId != null) {
                    goToOperadorBuilder(userId, ref, refGeral)
                }
            } else {
                if (userId != null) {
                    goToGerenteBuilder(userId, ref, refGeral)
                }
            }
        } catch (ex: Exception) {
            Log.d("Cadastros", "Catch - ${ex.message}")
        }
    }

    private fun goToGerenteBuilder(userId: String, ref: DatabaseReference, refGeral: DatabaseReference) {
        val user = Gerente(
            userId,
            gerente_Nome,
            gerente_Sobrenome,
            gerente_Celular,
            gerente_Email,
            gerente_Senha,
            restaurante_Nome,
            restaurante_Celular,
            restaurante_CEP,
            restaurante_Cidade,
            restaurante_Rua,
            restaurante_Complemento,
            restaurante_Email,
            tipoUser
        )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Cadastros", "Sucesso - Cadastro total de usuário completo")
                editar_dados_ProgressBar.visibility = View.GONE
                finish()
            }.addOnFailureListener {
                Log.d("Cadastros", "Fail - ${it.message}")
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }

        refGeral.setValue(user)
            .addOnSuccessListener {
                Log.d("Cadastros", "Sucesso - Cadastro total de usuário completo")
                editar_dados_ProgressBar.visibility = View.GONE
                finish()
            }.addOnFailureListener {
                Log.d("Cadastros", "Fail - ${it.message}")
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }

        editar_dados_ProgressBar.visibility = View.GONE
    }

    private fun goToOperadorBuilder(userId: String, ref: DatabaseReference, refGeral: DatabaseReference) {
        val user = Operador(
            userId,
            operador_Nome,
            operador_Sobrenome,
            operador_Celular,
            tipoUser,
            operador_NomeRestaurante,
            operador_Email,
            operador_Senha,
            tipoUser
        )

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("Cadastros", "Sucesso - Cadastro total de usuário completo")
                editar_dados_ProgressBar.visibility = View.GONE
                finish()
            }.addOnFailureListener {
                Log.d("Cadastros", "Fail - ${it.message}")
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }

        refGeral.setValue(user)
            .addOnSuccessListener {
                Log.d("Cadastros", "Sucesso - Cadastro total de usuário completo")
                editar_dados_ProgressBar.visibility = View.GONE
                finish()
            }.addOnFailureListener {
                Log.d("Cadastros", "Fail - ${it.message}")
                Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
            }

        editar_dados_ProgressBar.visibility = View.GONE
    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when (item?.itemId) {
//            idBackButton -> onBackPressed()
//        }
//
//        return true
//    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
