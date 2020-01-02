package com.jetbrains.Server.Dados;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Repositorio{

    private Map<String, Utilizador> utilizadores; //nome, utilizador

    private Map<Integer, Musica> musicas;

    private static int idMusica = 0;

    public Repositorio() {
        this.utilizadores = new ConcurrentHashMap<String, Utilizador>();
        this.musicas = new ConcurrentHashMap<Integer, Musica>();
    }

    //não precisa de synchronizde porque o ConcurrentHashMap já é thread safe
    public Map<Integer, Musica> getMusicas() {
        return musicas;
    }

    public Musica getMusicaId(int id){
        return (this.musicas.get(id));
    }

    //não precisa de synchronizde porque o ConcurrentHashMap já é thread safe
    public synchronized void addMusica(Musica m) {
        m.setId(++idMusica);
        musicas.put(m.getId(), m);
    }

    public synchronized void growNDownloads(int idMusica){
        this.musicas.get(idMusica).growNDowloads();
    }

    //não precisa de synchronizde porque o ConcurrentHashMap já é thread safe
    public Map<String, Utilizador> getUtilizadores() {
        return utilizadores;
    }


    //não precisa de synchronizde porque o ConcurrentHashMap já é thread safe
    public void addUtilizador(Utilizador u){
        utilizadores.put(u.getNome(), u);
    }
}
