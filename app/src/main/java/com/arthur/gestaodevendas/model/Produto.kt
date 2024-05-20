package com.arthur.gestaodevendas.model

data class Produto(
    val id: Int,
    val descricao: String,
    val valorUnitario: Double,
    val quantidadeEstoque: Int
)