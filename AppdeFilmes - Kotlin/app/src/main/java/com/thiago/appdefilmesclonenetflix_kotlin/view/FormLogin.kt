package com.thiago.appdefilmesclonenetflix_kotlin.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.thiago.appdefilmesclonenetflix_kotlin.R
import com.thiago.appdefilmesclonenetflix_kotlin.databinding.ActivityFormLoginBinding
import com.thiago.appdefilmesclonenetflix_kotlin.databinding.ActivityMainBinding

class FormLogin : AppCompatActivity() {

    private lateinit var binding: ActivityFormLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        window.statusBarColor = Color.parseColor("#000000")

        binding.editEmail.requestFocus()

        binding.btEntrar.setOnClickListener {

            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            when {
                email.isEmpty() -> {
                    binding.containerEmail.helperText = "Preencha o email!"
                    binding.containerEmail.boxStrokeColor = Color.parseColor("#FF9800")
                }
                senha.isEmpty() -> {
                    binding.containerSenha.helperText = "Preencha a senha"
                    binding.containerSenha.boxStrokeColor = Color.parseColor("#FF9800")
                }
                else->{
                    autenticacao(email, senha)
                }
            }
        }

        binding.txtTelaCadastro.setOnClickListener {
            val intent = Intent(this, FormCadastro::class.java)
            startActivity(intent)
        }
    }

    private fun autenticacao(email: String, senha: String) {
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, senha).addOnCompleteListener { autenticacao ->

                if( autenticacao.isSuccessful ){
                    Toast.makeText(this, "Login efetuado com sucesso!", Toast.LENGTH_SHORT).show()
                    navegarTelaPrincipal()
                }

            }.addOnFailureListener {
                Toast.makeText(this, "Erro ao realizar o login do usuário!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun navegarTelaPrincipal(){
        val intent = Intent(this, TelaPrincipal::class.java)
        startActivity(intent)
        finish()
    }

    override fun onStart() {
        super.onStart()

        val usuarioAtual = FirebaseAuth.getInstance().currentUser
        if( usuarioAtual != null ){
            navegarTelaPrincipal()
        }
    }
}