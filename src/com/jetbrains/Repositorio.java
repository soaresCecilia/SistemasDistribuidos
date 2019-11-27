package com.jetbrains;

import java.util.HashMap;
import java.util.Map;

public class Repositorio {



    private Map<Integer,Utilizador> utilizadores;
    private Map<Integer,Musica> musicas;


    public Repositorio(){

        this.utilizadores = new HashMap<Integer, Utilizador>();
        this.musicas = new HashMap<Integer, Musica>();

    }

    public Map<Integer, Utilizador> getUtilizadores() {
        return utilizadores;
    }

    public Map<Integer, Musica> getMusicas() {
        return musicas;
    }




}
