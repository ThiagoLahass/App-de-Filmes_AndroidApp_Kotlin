package com.thiago.appdefilmesclonenetflix_kotlin.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthEmailException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.thiago.appdefilmesclonenetflix_kotlin.R
import com.thiago.appdefilmesclonenetflix_kotlin.databinding.ActivityFormCadastroBinding

class FormCadastro : AppCompatActivity() {

    private lateinit var binding: ActivityFormCadastroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        window.statusBarColor = Color.parseColor("#FFFFFF")

        binding.editEmail.requestFocus()

        binding.btVamosLa.setOnClickListener {
            val email = binding.editEmail.text.toString()

            if(!email.isEmpty()){
                binding.containerHeader.visibility = View.VISIBLE
                binding.containerSenha.visibility = View.VISIBLE
                binding.btVamosLa.visibility = View.GONE
                binding.btContinuar.visibility = View.VISIBLE
                binding.txtTitulo.setText("Um mundo de séries e filmes\nilimitados espera por você.")
                binding.txtDescricao.setText("Crie uma conta mais sobre\no nosso App de Filmes")
                binding.containerEmail.boxStrokeColor = Color.parseColor("#FF018786")
                binding.containerEmail.helperText = ""
            }
            else{
                binding.containerEmail.helperText = "O email é obrigatório!"
                binding.containerEmail.boxStrokeColor = Color.parseColor("#FF0000")
            }
        }

        binding.btContinuar.setOnClickListener {
            val email = binding.editEmail.text.toString()
            val senha = binding.editSenha.text.toString()

            if( !email.isEmpty() && !senha.isEmpty()){

                cadastro(email, senha)

                binding.containerEmail.helperText = ""
                binding.containerEmail.boxStrokeColor = Color.parseColor("#FF018786")
                binding.containerSenha.helperText = ""
                binding.containerSenha.boxStrokeColor = Color.parseColor("#FF018786")
            }else if(senha.isEmpty() && !email.isEmpty()){
                binding.containerSenha.boxStrokeColor = Color.parseColor("#FF0000")
                binding.containerSenha.helperText = "A senha é obrigatória."
                binding.containerEmail.boxStrokeColor = Color.parseColor("#FF018786")
                binding.containerEmail.helperText = ""
            }else if(email.isEmpty()){
                binding.containerEmail.boxStrokeColor = Color.parseColor("#FF0000")
                binding.containerEmail.helperText = "O email é obrigatório."
                binding.containerSenha.boxStrokeColor = Color.parseColor("#FF018786")
            }
        }

        binding.txtEntrar.setOnClickListener {
            val intent = Intent(this, FormLogin::class.java)
            startActivity(intent)
        }
    }

    private fun cadastro(email: String, senha: String) {

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, senha).addOnCompleteListener { cadastro ->
            if( cadastro.isSuccessful ){
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()

                binding.containerEmail.helperText = ""
                binding.containerEmail.boxStrokeColor = Color.parseColor("#FF018786")
                binding.containerSenha.helperText = ""
                binding.containerSenha.boxStrokeColor = Color.parseColor("#FF018786")

            }
        }.addOnFailureListener {
            val erro = it

            when{
                erro is FirebaseAuthInvalidUserException ->{
                    binding.containerEmail.helperText = "Email inválido!"
                    binding.containerEmail.boxStrokeColor = Color.parseColor("#FF0000")
                }
                erro is FirebaseAuthWeakPasswordException ->{
                    binding.containerSenha.helperText = "A senha deve ter pelo menos seis caracteres!"
                    binding.containerSenha.boxStrokeColor = Color.parseColor("#FF0000")
                }
                erro is FirebaseAuthUserCollisionException ->{
                    binding.containerEmail.helperText = "Esta conta ja foi cadastrada!"
                    binding.containerEmail.boxStrokeColor = Color.parseColor("#FF0000")
                }
                erro is FirebaseNetworkException ->{
                    binding.containerEmail.helperText = "Sem conexão com a internet!"
                    binding.containerEmail.boxStrokeColor = Color.parseColor("#FF0000")
                }
                else->{
                    Toast.makeText(this, "Erro ao cadastrar usuário!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }
}