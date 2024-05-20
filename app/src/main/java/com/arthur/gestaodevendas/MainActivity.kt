package com.arthur.gestaodevendas

import com.arthur.gestaodevendas.view.activity.ListaVendasActivity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.arthur.gestaodevendas.view.activity.ListaProdutosActivity


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnEstoque: Button = findViewById(R.id.btnEstoque)
        val btnVendas: Button = findViewById(R.id.btnVendas)

        btnEstoque.setOnClickListener {
            abrirTelaEstoque()

        }
        btnVendas.setOnClickListener {
            abrirTelaVendas()
        }
    }


    private fun abrirTelaEstoque() {
        startActivity(Intent(this, ListaProdutosActivity::class.java))
    }
    private fun abrirTelaVendas(){

            startActivity(Intent(this, ListaVendasActivity::class.java))

    }
}