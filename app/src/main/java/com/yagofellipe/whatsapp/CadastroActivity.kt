package com.yagofellipe.whatsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.yagofellipe.whatsapp.databinding.ActivityCadastroBinding
import com.yagofellipe.whatsapp.model.Usuário
import com.yagofellipe.whatsapp.utils.exibirMensagem

class CadastroActivity : AppCompatActivity() {

    private val binding : ActivityCadastroBinding by lazy {
        ActivityCadastroBinding.inflate(layoutInflater)
    }

    private lateinit var nome: String
    private lateinit var email: String
    private lateinit var senha: String
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    private val firestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inicializarToolBar()
        inicializarCadastro()
    }

    private fun inicializarCadastro() {
        binding.btnCadastrar.setOnClickListener {

            if (validarCampos() == true) {
                cadastrarUsuario(nome, email, senha)
            }
        }
    }

    private fun cadastrarUsuario(nome: String, email: String, senha: String) {
        firebaseAuth.createUserWithEmailAndPassword(
            email, senha
        ).addOnCompleteListener {
            if (it.isSuccessful){
                val idUsuario = it.result.user?.uid
                if (idUsuario != null){
                    val usuario = Usuário (idUsuario, nome, email)
                    salvarUsuarioFirestore(usuario)
                }


            }
        }.addOnFailureListener{
            try {
                throw it
            }catch (erroSenhaFraca: FirebaseAuthWeakPasswordException) {
                exibirMensagem("Senha fraca")
            }catch (erroUsuarioExistente: FirebaseAuthUserCollisionException){
                exibirMensagem("E-mail já utilizado")
            }catch (erroCredencial: FirebaseAuthInvalidCredentialsException){
                exibirMensagem("Credencial inválida")
            }
        }
    }

    private fun salvarUsuarioFirestore(usuario: Usuário) {
        firestore
            .collection("usuarios")
            .document(usuario.id)
            .set(usuario)
            .addOnSuccessListener {
                exibirMensagem("Cadastro feito com sucesso!")
                startActivity(Intent(applicationContext, MainActivity::class.java))

            }.addOnFailureListener {
                exibirMensagem("Erro ao cadastrar")
            }

    }

    private fun validarCampos(): Boolean {
        nome = binding.inputNome.toString()
        email = binding.inputEmail.toString()
        senha = binding.inputSenha.toString()

        if (nome.isEmpty()) {
            binding.textInputNome.error = "Digite um nome"
            return false
        }
        binding.textInputNome.error = null

        if (email.isEmpty()) {
            binding.textInputEmail.error = "Digite um e-mail"
            return false
        }
        binding.textInputEmail.error = null

        if (senha.isEmpty()) {
            binding.textInputSenha.error = "Digite uma senha"
            return false
        }
        binding.textInputSenha.error = null

        return true
    }

    private fun inicializarToolBar() {
        val toolbar = binding.tbPrincipal.tbPrincipal
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = "Cadastro"
            setDisplayHomeAsUpEnabled(true)
        }
    }
}