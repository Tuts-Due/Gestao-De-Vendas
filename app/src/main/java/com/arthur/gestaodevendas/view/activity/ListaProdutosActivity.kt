package com.arthur.gestaodevendas.view.activity

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arthur.gestaodevendas.R
import com.arthur.gestaodevendas.viewmodel.ProdutoViewModel
import com.arthur.gestaodevendas.view.adapter.ProdutoAdapter

class ListaProdutosActivity : AppCompatActivity(), ProdutoAdapter.OnHeaderClickListener {

    private val produtoViewModel: ProdutoViewModel by viewModels()
    private lateinit var adapter: ProdutoAdapter
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_produtos)

        searchEditText = findViewById(R.id.editTextSearch)
        recyclerView = findViewById(R.id.recyclerViewProdutos)
        val buttonExportCSV: Button = findViewById(R.id.buttonExportCSV)

        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ProdutoAdapter(this)
        recyclerView.adapter = adapter

        produtoViewModel.filteredList.observe(this, Observer {
            adapter.submitList(it)
        })

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                produtoViewModel.searchProdutos(s.toString())
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        buttonExportCSV.setOnClickListener {
            produtoViewModel.exportarCSV()
        }
    }

    override fun onIdHeaderClick() {
        produtoViewModel.sortById()
    }

    override fun onDescricaoHeaderClick() {
        produtoViewModel.sortByDescricao()
    }

    override fun onValorUnitarioHeaderClick() {
        produtoViewModel.sortByValorUnitario()
    }

    override fun onQuantidadeHeaderClick() {
        produtoViewModel.sortByQuantidadeEstoque()
    }
}
