package br.com.alexandre_salgueirinho.iquefome_restaurante.model

class Gerente(
    val Id: String,
    val gerente_Nome: String,
    val gerente_Sobrenome: String,
    val gerente_Celular: String,
    val gerente_Email: String,
    val gerente_Senha: String,
    val restaurante_Nome: String,
    val restaurante_Celular: String,
    val restaurante_CEP: String,
    val restaurante_Cidade: String,
    val restaurante_Rua: String,
    val restaurante_Complemento: String,
    val restaurante_Email: String,
    val tipoUser: String
) {
    constructor () : this("", "", "", "", "",
        "", "", "", "", "",
        "", "", "", "")
}