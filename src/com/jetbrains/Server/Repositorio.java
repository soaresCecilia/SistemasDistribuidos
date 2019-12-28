package com.jetbrains.Server;

import java.util.HashMap;
import java.util.Map;

public class Repositorio{

    private Map<String, Utilizador> utilizadores; //nome, utilizador

    //private List<Utilizador> utilizadorAutenticado;

    private Map<Integer, Musica> musicas;

    private static int idMusica = 0;

    public Repositorio() {
        this.utilizadores = new HashMap<String, Utilizador>();
        this.musicas = new HashMap<Integer, Musica>();
    }

    public void setUtilizadores(Map<String, Utilizador> utilizadores) {
        this.utilizadores = utilizadores;
    }

    public Map<String, Utilizador> getUtilizadores() {
        return utilizadores;
    }

    public Map<Integer, Musica> getMusicas() {
        return musicas;
    }

    public Musica getMusicaId(int id){
        return (this.musicas.get(id));
    }

    public void addMusica(Musica m){
        defineIdMusica(m);
        musicas.put(m.getId(), m);
    }

    public int defineIdMusica(Musica m){
        //fazer lock

        idMusica++;

        m.setId(idMusica);
        return idMusica;
    }





}
