package com.arthur.gestaodevendas.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.arthur.gestaodevendas.R
import com.arthur.gestaodevendas.model.ProdutosVendidos

class VendasAdapter(private val headerClickListener: OnHeaderClickListener) :
    ListAdapter<ProdutosVendidos, RecyclerView.ViewHolder>(VendaDiffCallback()) {

    interface OnHeaderClickListener {
        fun onIdHeaderClick()
        fun onDescricaoHeaderClick()
        fun onCodigoVendaHeaderClick()
        fun onValorUnitarioHeaderClick()
        fun onQuantidadeVendidaHeaderClick()
    }

    // ViewHolder para o cabeçalho
    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        init {
            itemView.findViewById<TextView>(R.id.headerId).setOnClickListener { headerClickListener.onIdHeaderClick() }
            itemView.findViewById<TextView>(R.id.headerDescricao).setOnClickListener { headerClickListener.onDescricaoHeaderClick() }
            itemView.findViewById<TextView>(R.id.headerCodigoVenda).setOnClickListener { headerClickListener.onCodigoVendaHeaderClick() }
            itemView.findViewById<TextView>(R.id.headerValorUnitario).setOnClickListener { headerClickListener.onValorUnitarioHeaderClick() }
            itemView.findViewById<TextView>(R.id.headerQuantidadeVendida).setOnClickListener { headerClickListener.onQuantidadeVendidaHeaderClick() }
        }
    }

    // ViewHolder para os itens de venda
    inner class VendaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textId: TextView = itemView.findViewById(R.id.textId)
        val textDescricao: TextView = itemView.findViewById(R.id.textDescricao)
        val textCodigoVenda: TextView = itemView.findViewById(R.id.textCodigoVenda)
        val textValorUnitario: TextView = itemView.findViewById(R.id.textValorUnitario)
        val textQuantidadeVendida: TextView = itemView.findViewById(R.id.textQuantidadeVendida)
        val textTotal: TextView = itemView.findViewById(R.id.textTotal)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) R.layout.header_vendas else R.layout.item_vendido
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return if (viewType == R.layout.header_vendas) HeaderViewHolder(itemView) else VendaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is VendaViewHolder) {
            val venda = getItem(position - 1)
            holder.textId.text = venda.id.toString()
            holder.textDescricao.text = venda.descricao
            holder.textCodigoVenda.text = venda.codigoVenda
            holder.textValorUnitario.text = venda.valorUnitario.toString()
            holder.textQuantidadeVendida.text = venda.quantidadeVendida.toString()

            val total = venda.valorUnitario * venda.quantidadeVendida
            holder.textTotal.text = total.toString()
        }
    }

    override fun getItemCount(): Int {
        return super.getItemCount() + 1
    }

    class VendaDiffCallback : DiffUtil.ItemCallback<ProdutosVendidos>() {
        override fun areItemsTheSame(oldItem: ProdutosVendidos, newItem: ProdutosVendidos): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ProdutosVendidos, newItem: ProdutosVendidos): Boolean {
            return oldItem == newItem
        }
    }
}
