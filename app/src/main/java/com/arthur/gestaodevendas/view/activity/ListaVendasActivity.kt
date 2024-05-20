package com.arthur.gestaodevendas.view.activity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arthur.gestaodevendas.R
import com.arthur.gestaodevendas.viewmodel.VendasViewModel
import com.arthur.gestaodevendas.view.adapter.VendasAdapter
import com.arthur.gestaodevendas.model.ProdutosVendidos

class ListaVendasActivity : AppCompatActivity(), VendasAdapter.OnHeaderClickListener {

    private val vendasViewModel: VendasViewModel by viewModels()
    private lateinit var adapter: VendasAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var textTotalVendas: TextView
    private lateinit var buttonExportCSV: Button
    private lateinit var searchEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_produtos_vendidos)

        recyclerView = findViewById(R.id.recyclerViewProdutosVendidos)
        textTotalVendas = findViewById(R.id.textViewTotal)
        buttonExportCSV = findViewById(R.id.buttonExportCSV)
        searchEditText = findViewById(R.id.editTextSearch)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = VendasAdapter(this)
        recyclerView.adapter = adapter

        vendasViewModel.filteredList.observe(this, Observer { produtosVendidosList: List<ProdutosVendidos> ->
            adapter.submitList(produtosVendidosList)

            // Somar o total dos valores dos produtos vendidos
            var totalVendas = 0.0
            for (produtoVendido in produtosVendidosList) {
                totalVendas += produtoVendido.valorUnitario * produtoVendido.quantidadeVendida
            }

            // Atualizar o TextView com o total das vendas
            textTotalVendas.text = getString(R.string.total_venda, totalVendas)
        })

        // Adiciona um listener para o botão Export CSV
        buttonExportCSV.setOnClickListener {
            vendasViewModel.exportarCSV()
        }

        // Adiciona um TextWatcher para o EditText de busca
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                vendasViewModel.searchVendas(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }

    override fun onIdHeaderClick() {
        vendasViewModel.sortById()
    }

    override fun onDescricaoHeaderClick() {
        vendasViewModel.sortByDescricao()
    }

    override fun onCodigoVendaHeaderClick() {
        vendasViewModel.sortByCodigoVenda()
    }

    override fun onValorUnitarioHeaderClick() {
        vendasViewModel.sortByValorUnitario()
    }

    override fun onQuantidadeVendidaHeaderClick() {
        vendasViewModel.sortByQuantidadeVendida()
    }
}
