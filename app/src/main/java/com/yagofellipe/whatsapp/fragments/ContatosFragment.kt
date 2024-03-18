package com.yagofellipe.whatsapp.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.toObject
import com.yagofellipe.whatsapp.R
import com.yagofellipe.whatsapp.activities.MensagensActivity
import com.yagofellipe.whatsapp.adapters.ContatosAdapter
import com.yagofellipe.whatsapp.databinding.FragmentContatosBinding
import com.yagofellipe.whatsapp.model.Usuario
import com.yagofellipe.whatsapp.utils.Constantes

class ContatosFragment : Fragment() {
    private lateinit var contatosAdapter: ContatosAdapter
    private lateinit var binding: FragmentContatosBinding
    private val firebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val firestore by lazy {
        FirebaseFirestore.getInstance()
    }

    private lateinit var eventSnapshot: ListenerRegistration
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentContatosBinding.inflate(inflater, container, false)

        contatosAdapter = ContatosAdapter{usuario ->
            val intent = Intent(context, MensagensActivity::class.java)
            intent.putExtra("contato", usuario)
            intent.putExtra("origem", Constantes.ORIGEM_CONTATO)
            startActivity(intent)
        }
        binding.rvContatos.adapter = contatosAdapter
        binding.rvContatos.layoutManager = LinearLayoutManager(context)
        binding.rvContatos.addItemDecoration(
            DividerItemDecoration(context, RecyclerView.VERTICAL)
        )

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        adicionarListenerContatos()
    }

    private fun adicionarListenerContatos() {
        eventSnapshot = firestore
            .collection("usuarios")
            .addSnapshotListener{querySnapshot, erro ->
                val listaContatos = mutableListOf<Usuario>()
                val documentos = querySnapshot?.documents
                documentos?.forEach{documentSnapshot ->
                    val idUsuarioLogado =  firebaseAuth.currentUser?.uid
                    val usuario = documentSnapshot.toObject(Usuario::class.java)
                    if(usuario != null && idUsuarioLogado != null){
                        if(usuario.id != idUsuarioLogado){
                            listaContatos.add(usuario)
                        }
                    }
                }

                if (listaContatos.isNotEmpty()){
                    contatosAdapter.adicionarLista(listaContatos)
                }

            }
    }

    override fun onDestroy() {
        super.onDestroy()
        eventSnapshot.remove()
    }

}

