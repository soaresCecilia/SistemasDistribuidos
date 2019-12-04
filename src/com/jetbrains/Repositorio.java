package com.jetbrains;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repositorio implements SoundCloud{

    private Map<String, Utilizador> utilizadores; //nome, utilizador

    //private List<Utilizador> utilizadorAutenticado;

    private Map<Integer,Musica> musicas;

    private static int idMusica = 0;


    public Repositorio(){
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






    public void login(String email, int password) throws IOException, CredenciaisInvalidasException, ClientesSTUBException{}
    public void logout(String s) throws  IOException, ClientesSTUBException{}
    public void registarUtilizador(String email, int password) throws UtilizadorJaExisteException,ClientesSTUBException{}
    public void download(int idMusica) throws IOException,MusicaInexistenteException, UtilizadorNaoAutenticadoException,ClientesSTUBException{}
    public void upload( String nome, String interprete, int ano, String caminho) throws IOException, UtilizadorNaoAutenticadoException,ClientesSTUBException{
        Musica mu = new Musica(1,"a","b",1,"r","p");
        int id = funcaoHash(mu);
        this.musicas.put(id,mu);
    }
    public void procuraMusica (String etiqueta, String oQueProcurar) throws IOException, UtilizadorNaoAutenticadoException, MusicaInexistenteException, ClientesSTUBException{}

    public int funcaoHash(Musica m){
        //fazer lock

        idMusica++;

        m.setId(idMusica);
        return idMusica;


    }





}
