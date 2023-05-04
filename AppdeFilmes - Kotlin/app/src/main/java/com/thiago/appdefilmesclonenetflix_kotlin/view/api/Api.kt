package com.thiago.appdefilmesclonenetflix_kotlin.view.api

import com.thiago.appdefilmesclonenetflix_kotlin.view.Model.Categorias
import retrofit2.Call
import retrofit2.http.GET

interface Api {
    @GET("/filmes")
    fun listaCategorias(): Call<Categorias>
}