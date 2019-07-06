package br.com.alexandre_salgueirinho.iquefome_restaurante

import br.com.alexandre_salgueirinho.iquefome_restaurante.model.Pratos
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_prato.view.*

class PratoItem (val prato: Pratos): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val precoPrato = "R\$ " + prato.pratoPreco
        viewHolder.itemView.textView_Nome_Prato.text = prato.pratoNome
        viewHolder.itemView.textView_TipoPrato.text = prato.pratoTipo
        viewHolder.itemView.textView_TipoComida.text = prato.pratoTipoComida
        viewHolder.itemView.textView_Preco.text = precoPrato

        Picasso.get().load(prato.pratoUrlFoto).into(viewHolder.itemView.circleImage_Perfil)
    }

    override fun getLayout(): Int {
        return R.layout.item_prato
    }
}