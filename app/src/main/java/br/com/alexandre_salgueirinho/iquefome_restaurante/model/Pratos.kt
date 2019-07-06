package br.com.alexandre_salgueirinho.iquefome_restaurante.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Pratos(
    val pratoId: String,
    val pratoNome: String,
    val pratoPreco: String,
    val pratoUrlFoto: String,
    val pratoDescricao: String,
    val pratoTipo: String,
    val pratoTipoComida: String,
    val prato_Rest_Nome: String,
    val prato_Rest_Id: String
): Parcelable {
    constructor() : this("", "", "", "", "", "", "", "", "")
}