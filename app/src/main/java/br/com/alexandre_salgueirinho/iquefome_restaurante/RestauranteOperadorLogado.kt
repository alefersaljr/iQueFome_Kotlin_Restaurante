package br.com.alexandre_salgueirinho.iquefome_restaurante

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth

class RestauranteOperadorLogado : AppCompatActivity() {

    lateinit var mToolbar: Toolbar
    var mAuth = FirebaseAuth.getInstance()
    var currentUser = mAuth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_operador_logado)

//        logado_ProgressBar.visibility = View.VISIBLE

        mToolbar = findViewById(R.id.reservas_recebidas_Toolbar)
        setSupportActionBar(mToolbar)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.action_menu_account, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.action_menu_icon_account -> {
                startActivity(Intent(this, RestauranteMeusDados::class.java))
            }
        }
        return true
    }
}
