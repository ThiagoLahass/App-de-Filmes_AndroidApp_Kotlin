package com.thiago.appdefilmesclonenetflix_kotlin.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.thiago.appdefilmesclonenetflix_kotlin.databinding.ActivityTelaPrincipalBinding
import com.thiago.appdefilmesclonenetflix_kotlin.view.Adapter.AdapterCategoria
import com.thiago.appdefilmesclonenetflix_kotlin.view.Model.Categoria
import com.thiago.appdefilmesclonenetflix_kotlin.view.Model.Categorias
import com.thiago.appdefilmesclonenetflix_kotlin.view.api.Api
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class TelaPrincipal : AppCompatActivity() {

    private lateinit var binding: ActivityTelaPrincipalBinding
    private lateinit var adapterCategoria: AdapterCategoria
    private val listaCategorias: MutableList<Categoria> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTelaPrincipalBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar!!.hide()
        window.statusBarColor = Color.parseColor("#000000")

        //Configuração da RecyclerView
        val recyclerViewFilmes = binding.recyclerViewFilmes
        recyclerViewFilmes.layoutManager = LinearLayoutManager(this)
        adapterCategoria = AdapterCategoria(this, listaCategorias )
        recyclerViewFilmes.setHasFixedSize(true)
        recyclerViewFilmes.adapter = adapterCategoria


        binding.txtSair.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            navegarTelaLogin()
            Toast.makeText(this, "Usuário deslogado com sucesso!", Toast.LENGTH_SHORT).show()
        }

        //Configurar Retrofit
        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://stackmobile.com.br/")
            .build()
            .create(Api::class.java)

        retrofit.listaCategorias().enqueue(object : Callback<Categorias>{
            @SuppressLint("NotifyDataSetChanged")
            override fun onResponse(call: Call<Categorias>, response: Response<Categorias>) {
                if( response.code() == 200 ){
                    response.body()?.let {
                        adapterCategoria.listaCategorias.addAll(it.categorias)
                        adapterCategoria.notifyDataSetChanged()

                        binding.containerProgressBar.visibility = View.GONE
                        binding.progressBar.visibility = View.GONE
                        binding.txtCarregando.visibility = View.GONE
                    }
                }
            }
            override fun onFailure(call: Call<Categorias>, t: Throwable) {
                Toast.makeText(applicationContext, "Erro ao buscar os filmes!", Toast.LENGTH_SHORT).show()
            }
        })


    }

    private fun navegarTelaLogin(){
        val intent = Intent(this, FormLogin::class.java)
        startActivity(intent)
        finish()
    }

}