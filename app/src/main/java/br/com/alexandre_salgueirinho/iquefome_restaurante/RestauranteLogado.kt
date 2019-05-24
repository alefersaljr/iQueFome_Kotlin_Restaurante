package br.com.alexandre_salgueirinho.iquefome_restaurante

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_restaurante_logado.*

class RestauranteLogado : AppCompatActivity() {

    lateinit var mToolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_logado)

        mToolbar = findViewById(R.id.logado_Toolbar)
        setSupportActionBar(mToolbar)

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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu_account, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_menu_icon_account -> {
                startActivity(Intent(this, MeusDados::class.java))
            }
        }
        return true
    }
}
