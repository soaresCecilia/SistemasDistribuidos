package com.jetbrains.Server;

import java.util.HashMap;
import java.util.Map;

public class Repositorio{

    private Map<String, Utilizador> utilizadores; //nome, utilizador

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

    public synchronized void addMusica(Musica m){
        musicas.put(defineIdMusica(m), m);
    }

    public synchronized void addUtilizador(Utilizador u){
        utilizadores.put(u.getNome(),u);
    }

    public synchronized void growNDownloads(int idMusica){

        this.musicas.get(idMusica).growNDowloads();
    }

    public synchronized int defineIdMusica(Musica m){

        idMusica++;

        m.setId(idMusica);
        return idMusica;
    }




}
