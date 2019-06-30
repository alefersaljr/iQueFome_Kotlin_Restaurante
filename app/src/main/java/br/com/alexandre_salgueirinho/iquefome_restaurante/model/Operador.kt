package br.com.alexandre_salgueirinho.iquefome_restaurante.model

class Operador (
    val Id: String,
    val operadorNome: String,
    val operadorSobrenome: String,
    val operadorCelular: String,
    val operadorCargo: String,
    val operadorNomeRestautante: String,
    val operadorEmail: String,
    val operadorSenha: String,
    val tipoUser: String
) {
    constructor() : this ("", "", "", "", "", "", "", "", "")
}