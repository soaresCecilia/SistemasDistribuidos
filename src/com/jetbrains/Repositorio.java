package com.jetbrains;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repositorio {

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

    public void adicionaUtilizador(String nome, String pass) throws UtilizadorJaExisteException {
        if(!utilizadores.containsKey(nome)) {
            throw new UtilizadorJaExisteException("Impossivel adicionar utilizador com mesmo nome");
        }
        else {
            Utilizador u = new Utilizador(nome, pass);
            utilizadores.put(nome, u);
        }

    }

    public List<Musica> getMusicasPar(String str){

        List<Musica> lista = new ArrayList<Musica>();

        for(Musica m: musicas.values()){

            if(m.toString().contains(str))
                lista.add(m);
        }
        return lista;
    }

    public void adicionaM(Musica m){
        int id = funcaoHash(m);
        this.musicas.put(id,m);

    }

    public void registar(String s,String d){}
    public void login(String s, String d){}

    public int funcaoHash(Musica m){
        //fazer lock

        idMusica++;

        m.setId(idMusica);
        return idMusica;


    }





}
