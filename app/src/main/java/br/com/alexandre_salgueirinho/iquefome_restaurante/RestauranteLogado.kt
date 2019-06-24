package br.com.alexandre_salgueirinho.iquefome_restaurante

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.Toolbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_restaurante_logado.*

class RestauranteLogado : AppCompatActivity() {

    lateinit var mToolbar: Toolbar
    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_logado)

        mToolbar = findViewById(R.id.logado_Toolbar)
        setSupportActionBar(mToolbar)

        logado_Button_Add.setOnClickListener {
            goToAddFood()
        }

        carregaPratos()
    }

    private fun carregaPratos() {
        logado_ProgressBar.visibility = View.GONE
//        val ref = FirebaseDatabase.getInstance().getReference("/pratos/")
    }

    override fun onStart() {
        super.onStart()

        if (mAuth.currentUser == null){
            startActivity(Intent(this, RestauranteLogin::class.java))
            finish()
        }
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
                startActivity(Intent(this, RestauranteMeusDados::class.java))
            }
        }
        return true
    }
}
