package br.com.alexandre_salgueirinho.iquefome_restaurante

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Intermediario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_restaurante_login.*
import kotlinx.android.synthetic.main.popup_recuperar.view.*
import java.lang.Exception

class RestauranteLogin : AppCompatActivity() {

    val mAuth = FirebaseAuth.getInstance()
    var db = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_login)


        login_Button_Login.setOnClickListener {
            doLogin()
        }

        login_Button_Recuperar.setOnClickListener {
            goToForgotPassword()
        }

        login_Button_Cadastro.setOnClickListener {
            goToRegister()
        }
    }

    override fun onStart() {
        super.onStart()

        if (FirebaseAuth.getInstance().currentUser != null) {
            login_ProgressBar.visibility = View.VISIBLE
            getUserType()
//            finish()
//            startActivity(Intent(this, RestauranteGerenteLogado::class.java))
        }
    }

    private fun getUserType() {
        val userId = mAuth.currentUser?.uid
        val ref = db.getReference("/users/cadastros/restaurantes/geral/$userId")
        var tipoUsuario: String

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(p0: DataSnapshot) {
                val userIntermediario = p0.getValue(Intermediario::class.java)

                try {
                    if (userIntermediario != null) {
                        tipoUsuario = userIntermediario.tipoUser

                        if (tipoUsuario.equals("Gerente")) {
                            finish()
                            startActivity(Intent(this@RestauranteLogin, RestauranteGerenteLogado::class.java))
                        } else if (tipoUsuario.equals("Operador")) {
                            finish()
//                            var intent = Intent(this, RestauranteGerenteLogado::class.java)
//                            intent.putExtra("tipoUser", tipoUsuario)
                            startActivity(Intent(this@RestauranteLogin, RestauranteOperadorLogado::class.java))
                        } else Toast.makeText(this@RestauranteLogin, "ERRO", Toast.LENGTH_SHORT).show()

                    }
                } catch (ex: Exception) {
                    Toast.makeText(this@RestauranteLogin, "Erro. ${ex.message}", Toast.LENGTH_SHORT).show()
                    login_ProgressBar.visibility = View.GONE
                    return
                }
            }
        })
    }

    private fun doLogin() {

        login_ProgressBar.visibility = View.VISIBLE

        val email = login_Edit_Email.text.toString()
        val password = login_Edit_Password.text.toString()

        if (validaCampos(email, password)) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
//                    val intent = Intent(this, RestauranteGerenteLogado::class.java)
                    getUserType()

                    startActivity(intent)
                    Log.d("ClienteLoginActivity", "Usuário $uid logado")
                    login_ProgressBar.visibility = View.GONE
                    finish()
                }
                .addOnFailureListener {
                    Log.d("ClienteLoginActivity", "${it.message}")
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
                    login_ProgressBar.visibility = View.GONE
                }
        } else login_ProgressBar.visibility = View.GONE
    }

    private fun goToForgotPassword() {
//        Toast.makeText(this, "Recuperação em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show()

        val mDialog = LayoutInflater.from(this).inflate(R.layout.popup_recuperar, null)
        val mBuilder = AlertDialog.Builder(this).setView(mDialog)
        val mAlertDialog = mBuilder.show()

        mDialog.popup_Button_Enviar.setOnClickListener {
            val email = mDialog.popup_EditText_Email.text.toString()

            mDialog.recuperar_ProgressBar.visibility = View.VISIBLE


            if (!email.isNullOrEmpty()) {
                Log.d("Popup", "Email não vazio")
                mAuth.sendPasswordResetEmail(email)
                    .addOnSuccessListener {
                        Log.d("Popup", "sucesso")
                        Toast.makeText(
                            this,
                            "Um userEmail de recuperação de senha foi enviado para você",
                            Toast.LENGTH_SHORT
                        ).show()

                        mDialog.recuperar_ProgressBar.visibility = View.GONE
                        mAlertDialog.dismiss()

                    }.addOnFailureListener {
                        mDialog.recuperar_ProgressBar.visibility = View.GONE
                        Log.d("Popup", "falha")
                        Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                    }
            } else {
                mDialog.recuperar_ProgressBar.visibility = View.GONE
                Log.d("Popup", "Erro")
                Toast.makeText(
                    this,
                    "Informe um userEmail para que lhe seja enviado a recuperação de senha",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        mDialog.popup_Button_Close.setOnClickListener {
            mAlertDialog.dismiss()
        }
    }

    private fun goToRegister() {
//        Toast.makeText(this, "Cadastro em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show()

        startActivity(Intent(this, RestauranteCadastro::class.java))
    }

    private fun validaCampos(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            if (email.isEmpty()) {
                login_Fild_Email.error = "É necessário informar um email"
                return false
            } else if (password.isEmpty()) {
                login_Fild_Password.error = "É necessário informar uma senha"
                return false
            }
        }
        return true
    }
}