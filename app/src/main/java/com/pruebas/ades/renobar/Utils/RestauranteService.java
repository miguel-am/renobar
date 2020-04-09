package com.pruebas.ades.renobar.Utils;

import com.pruebas.ades.renobar.Model.Restaurante;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestauranteService {

    @GET("listar/")
    Call<List <Restaurante>> getRestaurante();
}
