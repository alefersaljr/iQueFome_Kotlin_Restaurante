package br.com.alexandre_salgueirinho.iquefome_restaurante

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_restaurante_logado.*

class RestauranteLogado : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_logado)

        logado_Button_Add.setOnClickListener {
            goToAddFood()
        }

        logado_Button_logout.setOnClickListener {
            logout()
        }
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        startActivity(Intent(this, RestauranteLogin::class.java))
        finish()
    }

    private fun goToAddFood() {
        val intent = Intent (this, MainActivity::class.java)
        startActivity(intent)
    }
}
