package com.pruebas.ades.renobar.Utils;

public class Apis {

    public static final String URI_001="https://bbdd.renobarapp.es/";

    public static RestauranteService getRestauranteService(){

        return Cliente.getCliente ( URI_001 ).create ( RestauranteService.class);
    }
}
