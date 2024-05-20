package com.arthur.gestaodevendas.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arthur.gestaodevendas.R
import com.arthur.gestaodevendas.model.Produto
import com.arthur.gestaodevendas.utils.CSVExporter
import com.arthur.gestaodevendas.utils.SortOrder
import org.json.JSONArray
import java.io.InputStream
import java.util.Locale

class ProdutoViewModel(application: Application) : AndroidViewModel(application) {

    private val _produtos = mutableListOf<Produto>()
    private val _filteredList = MutableLiveData<List<Produto>>()
    val filteredList: LiveData<List<Produto>> get() = _filteredList

    private val csvExporter: CSVExporter = CSVExporter(application)

    private var idSortOrder = SortOrder.ASCENDING
    private var descricaoSortOrder = SortOrder.ASCENDING
    private var valorUnitarioSortOrder = SortOrder.ASCENDING
    private var quantidadeSortOrder = SortOrder.ASCENDING

    init {
        loadSampleProdutosFromJson()
    }

    private fun loadSampleProdutosFromJson() {
        val inputStream: InputStream = getApplication<Application>().resources.openRawResource(R.raw.produtos_mock)
        val json = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(json)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.getInt("id")
            val descricao = jsonObject.getString("descricao")
            val valorUnitario = jsonObject.getDouble("valorUnitario")
            val quantidadeEstoque = jsonObject.getInt("quantidadeEstoque")
            _produtos.add(Produto(id, descricao, valorUnitario, quantidadeEstoque))
        }
        _filteredList.value = _produtos
    }

    fun searchProdutos(searchTerm: String) {
        val filtered = if (searchTerm.isNotBlank()) {
            val term = searchTerm.trim().lowercase(Locale.getDefault())
            _produtos.filter { produto ->
                produto.id.toString().startsWith(term) ||
                        produto.descricao.lowercase(Locale.getDefault()).contains(term)
            }
        } else {
            _produtos.toList()
        }

        _filteredList.value = filtered
    }


    fun sortById() {
        idSortOrder = toggleSortOrder(idSortOrder)
        _filteredList.value = if (idSortOrder == SortOrder.ASCENDING) {
            _filteredList.value?.sortedBy { it.id }
        } else {
            _filteredList.value?.sortedByDescending { it.id }
        }
    }

    fun sortByDescricao() {
        descricaoSortOrder = toggleSortOrder(descricaoSortOrder)
        _filteredList.value = if (descricaoSortOrder == SortOrder.ASCENDING) {
            _filteredList.value?.sortedBy { it.descricao }
        } else {
            _filteredList.value?.sortedByDescending { it.descricao }
        }
    }

    fun sortByValorUnitario() {
        valorUnitarioSortOrder = toggleSortOrder(valorUnitarioSortOrder)
        _filteredList.value = if (valorUnitarioSortOrder == SortOrder.ASCENDING) {
            _filteredList.value?.sortedBy { it.valorUnitario }
        } else {
            _filteredList.value?.sortedByDescending { it.valorUnitario }
        }
    }

    fun sortByQuantidadeEstoque() {
        quantidadeSortOrder = toggleSortOrder(quantidadeSortOrder)
        _filteredList.value = if (quantidadeSortOrder == SortOrder.ASCENDING) {
            _filteredList.value?.sortedBy { it.quantidadeEstoque }
        } else {
            _filteredList.value?.sortedByDescending { it.quantidadeEstoque }
        }
    }

    private fun toggleSortOrder(order: SortOrder): SortOrder {
        return if (order == SortOrder.ASCENDING) SortOrder.DESCENDING else SortOrder.ASCENDING
    }

    fun exportarCSV() {
        val cabecalho = "ID,Descrição,Valor Unitário,Quantidade em Estoque"
        val linhas = _filteredList.value?.map {
            "${it.id},${it.descricao},${it.valorUnitario},${it.quantidadeEstoque}"
        } ?: emptyList()
        csvExporter.exportarCSV("produtos.csv", cabecalho, linhas)
    }
}
