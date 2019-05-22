package br.com.alexandre_salgueirinho.iquefome_restaurante

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.parcel.Parcelize
import java.util.*

class MainActivity : AppCompatActivity() {

    var uriImagemSelecionada: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        FirebaseAuth.getInstance().signInWithEmailAndPassword("a.salgueirinho@cinq.com.br", "123456")

        cadastro_Button_Image.setOnClickListener {
            Log.d("ClienteCadastroActivity", "Try to show photo")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

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
                    }
            }
        }
    }

    private fun validaCompos(): Boolean {
        if (login_EditText_nome.text.isEmpty()) {
            Toast.makeText(this, "Informe o nome do prato", Toast.LENGTH_SHORT).show()
        } else if (login_EditText_rest.text.isEmpty()) {
            Toast.makeText(this, "Informe o nome do restaurante", Toast.LENGTH_SHORT).show()
        } else if (login_EditText_Preco.text.isEmpty()) {
            Toast.makeText(this, "Informe o preço do prato", Toast.LENGTH_SHORT).show()
        } else if (login_EditText_Tipo.text.isEmpty()) {
            Toast.makeText(this, "Informe o tipo do prato", Toast.LENGTH_SHORT).show()
        } else if (login_EditText_TipoComida.text.isEmpty()) {
            Toast.makeText(this, "Informe o tipo da comida", Toast.LENGTH_SHORT).show()
        } else if (login_EditText_Descricao.text.isEmpty()) {
            Toast.makeText(this, "Informe a descrição do prato", Toast.LENGTH_SHORT).show()
        } else return true
        return false
    }

    private fun savePratoToFirebaseDatabase(urlImagemPerfil: String) {
        val uid = UUID.randomUUID().toString()
        val ref = FirebaseDatabase.getInstance().getReference("/pratos/$uid")

        val prato = Pratos(
            uid,
            login_EditText_nome.text.toString(),
            login_EditText_Preco.text.toString(),
            login_EditText_rest.text.toString(),
            urlImagemPerfil,
            login_EditText_Descricao.text.toString(),
            login_EditText_Tipo.text.toString(),
            login_EditText_TipoComida.text.toString()
        )

        ref.setValue(prato).addOnSuccessListener {
            Log.d("ClienteCadastroActivity", "Finalmente deu boa")
            Toast.makeText(this, "sucesso", Toast.LENGTH_SHORT).show()
            finish()
        }.addOnFailureListener {
            Toast.makeText(this, "${it.message}", Toast.LENGTH_SHORT).show()
        }
    }
}

@Parcelize
class Pratos(
    val pratoId: String,
    val pratoNome: String,
    val pratoPreco: String,
    val pratoRestaurante: String,
    val pratoUrlFoto: String,
    val pratoDescricao: String,
    val pratoTipo: String,
    val pratoTipoComida: String
) : Parcelable {
    constructor() : this("", "", "", "", "", "", "", "")
}


