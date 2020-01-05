package com.jetbrains.Server.Dados;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Repositorio{

    private Map<String, Utilizador> utilizadores; //nome, utilizador

    private Map<Integer, Musica> musicas;

    private static int idMusica = 0;
    /*
    *Construtor da Classe Repositório
    */
    public Repositorio() {
        this.utilizadores = new ConcurrentHashMap<String, Utilizador>();
        this.musicas = new ConcurrentHashMap<Integer, Musica>();
    }

    /*
    * Metodo que devolve um Map com todas as musicas existentes
    */
    //não precisa de synchronizde porque o ConcurrentHashMap já é thread safe
    public Map<Integer, Musica> getMusicas() {
        return musicas;
    }

    /*
    * metodo que devolve uma musica, dando o id da musica pretendida
    */
    public Musica getMusicaId(int id){
        return (this.musicas.get(id));
    }

    /*
     * metodo que verifica a existencia uma musica, dando o seu id
     */
    public Boolean existeMusica(int idMusica){
        return (this.musicas.containsKey(idMusica));
    }

    //não precisa de synchronizde porque o ConcurrentHashMap já é thread safe
    public synchronized void addMusica(Musica m) {
        m.setId(++idMusica);
        musicas.put(m.getId(), m);
    }

    /*
     * Metodo que dado o id de uma musica, aumenta o numero de downloads dessa musica
     */
    public synchronized void growNDownloads(int idMusica){
        this.musicas.get(idMusica).growNDowloads();
    }

    /*
     * Metodo que devolde um Map com todos os utilizadores registados no sistema
     */
    //não precisa de synchronizde porque o ConcurrentHashMap já é thread safe
    public Map<String, Utilizador> getUtilizadores() {
        return utilizadores;
    }

    /*
     * Metodo que dado um utilizador, o adiciona ao Map dos utilizadores do sistema
     */
    //não precisa de synchronizde porque o ConcurrentHashMap já é thread safe
    public void addUtilizador(Utilizador u){
        utilizadores.put(u.getNome(), u);
    }
}
