package br.com.alexandre_salgueirinho.iquefome_restaurante

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_restaurante_cadastrar_prato.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_restaurante_cadastro.*
import java.util.*

class MainActivity : AppCompatActivity() {

    var uriImagemSelecionada: Uri? = null
    lateinit var tipoPratoOption : Spinner
    lateinit var tipoComidaOption : Spinner
    var tipoPrato = "Selecione"
    var tipoComida = "Selecione"
    var mAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurante_cadastrar_prato)

        FirebaseAuth.getInstance().signInWithEmailAndPassword("a.salgueirinho@cinq.com.br", "123456")

        cadastro_Button_Image.setOnClickListener {
            Log.d("ClienteCadastroActivity", "Try to show photo")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        getOptionsSelected()

        pratoFotoRegister()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        cadastro_Button_Image.visibility = View.GONE

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            Log.d("ClienteCadastroActivity", "Photo was selected")

            uriImagemSelecionada = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uriImagemSelecionada)

            cadastro_CircleImage_Prato_Foto.setImageBitmap(bitmap)
        }
    }

    private fun pratoFotoRegister() {

        cadastrar_Button.setOnClickListener {
            if (uriImagemSelecionada == null) {
                Toast.makeText(this, "Selecione a imagem do prato", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            } else if (validaCompos()) {

                cadastrar_ProgressBar.visibility = View.VISIBLE

                val nomeArquivo = UUID.randomUUID().toString()
                val ref = FirebaseStorage.getInstance().getReference("images/$nomeArquivo")

                ref.putFile(uriImagemSelecionada!!)
                    .addOnSuccessListener {
                        ref.downloadUrl.addOnSuccessListener {
                            Log.d("ClienteCadastroActivity", "Link do arquivo: $it")

                            savePratoToFirebaseDatabase(it.toString())
                        }
                    }.addOnFailureListener {
                        Log.d("ClienteCadastroActivity", "Erro no upload")
                        Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
                        cadastrar_ProgressBar.visibility = View.GONE
                    }
            }
        }
    }

    private fun validaCompos(): Boolean {

        if (login_EditText_nome.text.isEmpty()) {
            Toast.makeText(this, "Informe o nome do prato", Toast.LENGTH_SHORT).show()
        } else if (login_EditText_Preco.text.isEmpty()) {
            Toast.makeText(this, "Informe o preço do prato", Toast.LENGTH_SHORT).show()
        } else if (tipoPrato == "Selecione") {
            Toast.makeText(this, "Informe o tipo do prato", Toast.LENGTH_SHORT).show()
        } else if (tipoComida == "Selecione") {
            Toast.makeText(this, "Informe o tipo da comida", Toast.LENGTH_SHORT).show()
        } else if (login_EditText_Descricao.text.isEmpty()) {
            Toast.makeText(this, "Informe a descrição do prato", Toast.LENGTH_SHORT).show()
        } else return true
        return false
    }

    private fun getOptionsSelected() {
        val pratoOptions = arrayOf("Selecione", "Entrada", "Prato Principal", "Sobremesa")
        val comidaOptions = arrayOf("Selecione", "Carnes", "Peixes", "Pizzas", "Sorvetes", "Pratos Quentes", "Sopas", "Saladas")

        tipoPratoOption = findViewById(R.id.login_EditText_Tipo)
        tipoComidaOption = findViewById(R.id.login_EditText_TipoComida)

        tipoPratoOption.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, pratoOptions)
        tipoComidaOption.adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, comidaOptions)

        tipoPratoOption.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@MainActivity, "Por favor, selecione um tipo de prato", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tipoPrato = pratoOptions.get(position)
            }
        }

        tipoComidaOption.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
                Toast.makeText(this@MainActivity, "Por favor, selecione um tipo de comida", Toast.LENGTH_SHORT).show()
            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                tipoComida = comidaOptions.get(position)
            }
        }
    }

    private fun savePratoToFirebaseDatabase(urlImagemPerfil: String) {
        val pratoId = UUID.randomUUID().toString()
        val restId = mAuth.currentUser?.uid
        val refGeral = FirebaseDatabase.getInstance().getReference("/pratos/clientes/$pratoId")
        val refRestaurante = FirebaseDatabase.getInstance().getReference("/pratos/restaurantes/$restId/$pratoId")

        val prato = Pratos(
            pratoId,
            login_EditText_nome.text.toString(),
            login_EditText_Preco.text.toString(),
            urlImagemPerfil,
            login_EditText_Descricao.text.toString(),
            tipoPrato,
            tipoComida
        )

        refGeral.setValue(prato).addOnSuccessListener {
            Log.d("ClienteCadastroActivity", "Finalmente deu boa")
            Toast.makeText(this, "sucesso", Toast.LENGTH_SHORT).show()
            cadastrar_ProgressBar.visibility = View.GONE
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
        }

        refRestaurante.setValue(prato).addOnSuccessListener {
            Log.d("ClienteCadastroActivity", "Finalmente deu boa")
            Toast.makeText(this, "sucesso", Toast.LENGTH_SHORT).show()
            cadastrar_ProgressBar.visibility = View.GONE
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
        }
        cadastrar_ProgressBar.visibility = View.GONE
    }
}

//@Parcelize
//class Pratos(
//    val pratoId: String,
//    val pratoNome: String,
//    val pratoPreco: String,
//    val pratoRestaurante: String,
//    val pratoUrlFoto: String,
//    val pratoDescricao: String,
//    val pratoTipo: String,
//    val pratoTipoComida: String
//) : Parcelable {
//    constructor() : this("", "", "", "", "", "", "", "")
//}


