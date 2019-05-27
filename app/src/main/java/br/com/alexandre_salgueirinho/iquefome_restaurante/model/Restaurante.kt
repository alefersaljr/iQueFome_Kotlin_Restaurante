package br.com.alexandre_salgueirinho.iquefome_restaurante.model

class Restaurante (
    val restaurante_Id: String,
    val restaurante_Nome: String,
    val restaurante_CEP: String,
    val restaurante_Cidade: String,
    val restaurante_Rua: String,
    val restaurante_Complemento: String,
    val restaurante_Email: String,
    val restaurante_Senha: String,
    val tipoUser: String
) {
    constructor () : this ("", "", "", "", "","", "", "", "")
}