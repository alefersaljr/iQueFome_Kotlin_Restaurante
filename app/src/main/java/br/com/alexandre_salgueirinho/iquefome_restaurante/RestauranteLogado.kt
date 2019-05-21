package br.com.alexandre_salgueirinho.iquefome_restaurante

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_restaurante_logado.*

class RestauranteLogado : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_logado)

        logado_Button_Add.setOnClickListener {
            goToAddFood()
        }
    }

    private fun goToAddFood() {
        val intent = Intent (this, MainActivity::class.java)
        startActivity(intent)
    }
}
