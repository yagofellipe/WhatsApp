package com.yagofellipe.whatsapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.yagofellipe.whatsapp.databinding.ItemContatosBinding
import com.yagofellipe.whatsapp.model.Usuario

class ContatosAdapter(
    private val onClick: (Usuario) -> Unit
) : RecyclerView.Adapter<ContatosAdapter.ContatosViewHolder>() {

    private var listaContatos = emptyList<Usuario>()
    fun adicionarLista(lista: List<Usuario>){
        listaContatos = lista
        notifyDataSetChanged()
    }

    inner class ContatosViewHolder(private val binding: ItemContatosBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(usuario: Usuario) {
            binding.textContatoNome.text = usuario.nome
            Picasso.get()
                .load(usuario.foto)
                .into(binding.imageContatoPerfil)

            binding.clItemContato.setOnClickListener {
                onClick(usuario)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContatosViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemContatosBinding.inflate(inflater, parent, false)
        return ContatosViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return listaContatos.size
    }

    override fun onBindViewHolder(holder: ContatosViewHolder, position: Int) {
        val usuario = listaContatos[position]
        holder.bind(usuario)
    }
}