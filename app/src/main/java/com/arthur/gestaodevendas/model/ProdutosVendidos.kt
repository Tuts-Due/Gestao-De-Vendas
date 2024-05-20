package com.arthur.gestaodevendas.model

data class ProdutosVendidos (
    val id: Int,
    val descricao: String,
    val codigoVenda: String,
    val valorUnitario:Double,
    val quantidadeVendida:Int
)