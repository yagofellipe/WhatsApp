package com.yagofellipe.whatsapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.yagofellipe.whatsapp.databinding.ActivityLoginBinding
import com.yagofellipe.whatsapp.utils.exibirMensagem

class LoginActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    private lateinit var email: String
    private lateinit var senha: String
    private val firebaseAuth : FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        firebaseAuth.signOut()
        inicializarEventosClique()
    }

    override fun onStart() {
        super.onStart()
        verificarUsuarioLogado()
    }

    private fun verificarUsuarioLogado() {
        val usuarioAtual = firebaseAuth.currentUser
        if (usuarioAtual != null)
            startActivity(Intent(this, MainActivity::class.java))
    }

    private fun inicializarEventosClique(){
        binding.txCadastro.setOnClickListener(){
            startActivity(
                Intent(this, CadastroActivity::class.java)
            )
        }
        binding.btnLogar.setOnClickListener {
            if(validarCampos() == true) {
                logarUsuario()
            }
        }


    }

    private fun logarUsuario() {
        firebaseAuth.signInWithEmailAndPassword(email, senha)
            .addOnSuccessListener {
                exibirMensagem("Logado com sucesso!")
                startActivity(Intent(this, MainActivity::class.java))
            }.addOnFailureListener {
                try {
                    throw it
                }catch (erroUsuarioInvalido: FirebaseAuthInvalidUserException){
                    erroUsuarioInvalido.printStackTrace()
                    exibirMensagem("E-mail não cadastrado")
                }catch (erroCredenciaisInvalidas: FirebaseAuthInvalidCredentialsException){
                    erroCredenciaisInvalidas.printStackTrace()
                    exibirMensagem("E-mail ou senha incorretos")
                }
            }
    }

    private fun validarCampos(): Boolean {
        email = binding.EditLoginEmail.toString()
        senha = binding.EditLoginSenha.toString()

        if (email.isEmpty()) {
            binding.textInputLayoutLoginEmail.error = "Digite um email"
            return false
        }
        binding.textInputLayoutLoginEmail.error = null
        if (senha.isEmpty()) {
            binding.textInputLayoutLoginSenha.error = "Digite uma senha"
            return false
        }
        binding.textInputLayoutLoginSenha.error = null


        return true
    }

}