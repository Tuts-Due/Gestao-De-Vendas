package com.arthur.gestaodevendas.viewmodel

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.arthur.gestaodevendas.R
import com.arthur.gestaodevendas.model.ProdutosVendidos
import com.arthur.gestaodevendas.utils.CSVExporter
import com.arthur.gestaodevendas.utils.SortOrder
import org.json.JSONArray
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.io.InputStream
import java.util.Locale

class VendasViewModel(application: Application) : AndroidViewModel(application) {

    private val _vendas = mutableListOf<ProdutosVendidos>()
    private val _filteredList = MutableLiveData<List<ProdutosVendidos>>()
    val filteredList: LiveData<List<ProdutosVendidos>> get() = _filteredList

    private val csvExporter: CSVExporter = CSVExporter(application)

    private var idSortOrder = SortOrder.ASCENDING
    private var descricaoSortOrder = SortOrder.ASCENDING
    private var codigoVendaSortOrder = SortOrder.ASCENDING
    private var valorUnitarioSortOrder = SortOrder.ASCENDING
    private var quantidadeVendidaSortOrder = SortOrder.ASCENDING

    init {
        loadSampleVendasFromJson()
    }

    private fun loadSampleVendasFromJson() {
        val inputStream: InputStream =
            getApplication<Application>().resources.openRawResource(R.raw.vendas_mock)
        val json = inputStream.bufferedReader().use { it.readText() }
        val jsonArray = JSONArray(json)

        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val id = jsonObject.getInt("id")
            val descricao = jsonObject.getString("descricao")
            val codigoVenda = jsonObject.getString("codigoVenda")
            val valorUnitario = jsonObject.getDouble("valorUnitario")
            val quantidadeVendida = jsonObject.getInt("quantidadeVendida")
            _vendas.add(
                ProdutosVendidos(
                    id,
                    descricao,
                    codigoVenda,
                    valorUnitario,
                    quantidadeVendida
                )
            )
        }
        _filteredList.value = _vendas
    }

    fun searchVendas(searchTerm: String) {
        val filtered = if (searchTerm.isNotBlank()) {
            val term = searchTerm.trim().lowercase()
            _vendas.filter { venda ->
                venda.id.toString().startsWith(term) ||
                        venda.descricao.lowercase().contains(term) ||
                        venda.codigoVenda.lowercase().contains(term)
            }
        } else {
            _vendas.toList()
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

    fun sortByCodigoVenda() {
        codigoVendaSortOrder = toggleSortOrder(codigoVendaSortOrder)
        _filteredList.value = if (codigoVendaSortOrder == SortOrder.ASCENDING) {
            _filteredList.value?.sortedBy { it.codigoVenda }
        } else {
            _filteredList.value?.sortedByDescending { it.codigoVenda }
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

    fun sortByQuantidadeVendida() {
        quantidadeVendidaSortOrder = toggleSortOrder(quantidadeVendidaSortOrder)
        _filteredList.value = if (quantidadeVendidaSortOrder == SortOrder.ASCENDING) {
            _filteredList.value?.sortedBy { it.quantidadeVendida }
        } else {
            _filteredList.value?.sortedByDescending { it.quantidadeVendida }
        }
    }

    private fun toggleSortOrder(order: SortOrder): SortOrder {
        return if (order == SortOrder.ASCENDING) SortOrder.DESCENDING else SortOrder.ASCENDING
    }

    fun exportarCSV() {
        val cabecalho = "ID,Descrição,Código da Venda,Valor Unitário,Quantidade Vendida\n"
        val linhas = _filteredList.value?.joinToString("\n") {
            "${it.id},${it.descricao},${it.codigoVenda},${it.valorUnitario},${it.quantidadeVendida}"
        } ?: ""

        val fileName = "vendas.csv"

        try {

            csvExporter.exportarCSV(fileName, cabecalho, linhas.split("\n"))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }


}