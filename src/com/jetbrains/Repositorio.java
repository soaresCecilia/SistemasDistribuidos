package com.jetbrains;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Repositorio {




    private Map<Integer,Musica> musicas;

    private static int idMusica =0;


    public Repositorio(){

        this.musicas = new HashMap<Integer, Musica>();

    }


    public Map<Integer, Musica> getMusicas() {
        return musicas;
    }

    public Musica getMusicaId(int id){

        return (this.musicas.get(id));
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

    public int funcaoHash(Musica m){
        //fazer lock

        idMusica++;

        m.setId(idMusica);
        return idMusica;


    }





}
