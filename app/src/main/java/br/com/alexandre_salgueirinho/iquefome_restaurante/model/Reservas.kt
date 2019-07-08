package br.com.alexandre_salgueirinho.iquefome_restaurante.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class Reservas(
    val reserva_Id: String,
    val cliente_Id: String,
    val reserva_Hora: String,
    val cliente_Nome: String,
    val cliente_Sobrenome: String,
    val cliente_Alteracao: String,
    val cliente_pontos: String,
    val prato_Nome: String,
    val prato_Preco: String,
    val restaurante_Nome: String,
    val restaurante_Id: String
): Parcelable {
    constructor() : this("", "", "", "", "", "", "", "", "", "", "")
}