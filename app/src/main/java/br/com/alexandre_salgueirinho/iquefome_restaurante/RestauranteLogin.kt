package br.com.alexandre_salgueirinho.iquefome_restaurante

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_restaurante_login.*

class RestauranteLogin : AppCompatActivity() {

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
            startActivity(Intent(this, RestauranteLogado::class.java))
        }
    }

    private fun doLogin() {
//        Toast.makeText(this, "Login em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show()

        val email = login_Edit_Email.text.toString()
        val password = login_Edit_Password.text.toString()

        if (validaCampos(email, password)) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    val uid = FirebaseAuth.getInstance().currentUser?.uid
                    val intent = Intent(this, RestauranteLogado::class.java)

                    startActivity(intent)
                    Log.d("ClienteLoginActivity", "Usuário $uid logado")
                    finish()
                }
                .addOnFailureListener {
                    Log.d("ClienteLoginActivity", "${it.message}")
                    Toast.makeText(this, "${it.message}", Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun goToForgotPassword() {
        Toast.makeText(this, "Recuperação em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show()
    }

    private fun goToRegister() {
        Toast.makeText(this, "Cadastro em desenvolvimento, aguarde.", Toast.LENGTH_SHORT).show()
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