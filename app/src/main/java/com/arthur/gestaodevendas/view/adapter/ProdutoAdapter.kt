package com.arthur.gestaodevendas.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arthur.gestaodevendas.R
import com.arthur.gestaodevendas.model.Produto

class ProdutoAdapter(private val headerClickListener: OnHeaderClickListener) :
    ListAdapter<Produto, RecyclerView.ViewHolder>(ProdutoDiffCallback()) {

    interface OnHeaderClickListener {
        fun onIdHeaderClick()
        fun onDescricaoHeaderClick()
        fun onValorUnitarioHeaderClick()
        fun onQuantidadeHeaderClick()
    }


    // ViewHolder para o cabeçalho
    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.findViewById<TextView>(R.id.headerId).setOnClickListener { headerClickListener.onIdHeaderClick() }
            itemView.findViewById<TextView>(R.id.headerDescricao).setOnClickListener { headerClickListener.onDescricaoHeaderClick() }
            itemView.findViewById<TextView>(R.id.headerValorUnitario).setOnClickListener { headerClickListener.onValorUnitarioHeaderClick() }
            itemView.findViewById<TextView>(R.id.headerQuantidade).setOnClickListener { headerClickListener.onQuantidadeHeaderClick() }
        }
    }

    // ViewHolder para os itens de produto
    inner class ProdutoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textId: TextView = itemView.findViewById(R.id.textId)
        val textDescricao: TextView = itemView.findViewById(R.id.textDescricao)
        val textValorUnitario: TextView = itemView.findViewById(R.id.textValorUnitario)
        val textQuantidade: TextView = itemView.findViewById(R.id.textQuantidade)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) R.layout.header_produto else R.layout.item_produto
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return if (viewType == R.layout.header_produto) HeaderViewHolder(itemView) else ProdutoViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProdutoViewHolder) {
            val produto = getItem(position - 1) // Ajuste devido ao cabeçalho
            holder.textId.text = produto.id.toString()
            holder.textDescricao.text = produto.descricao
            holder.textValorUnitario.text = produto.valorUnitario.toString()
            holder.textQuantidade.text = produto.quantidadeEstoque.toString()
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1 // Ajuste devido ao cabeçalho
    }

    class ProdutoDiffCallback : DiffUtil.ItemCallback<Produto>() {
        override fun areItemsTheSame(oldItem: Produto, newItem: Produto): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Produto, newItem: Produto): Boolean {
            return oldItem == newItem
        }
    }
}
