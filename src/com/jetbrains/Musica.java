package com.jetbrains;

import java.util.Arrays;

public class Musica {



    private int id;
    private String titulo;
    private String interprete;
    private String ano;
    private byte[] ficheiro;
    private int nDownloads;

    public Musica(int id, String titulo, String interprete, String ano, byte[] ficheiro) {
        this.id = id;
        this.titulo = titulo;
        this.interprete = interprete;
        this.ano = ano;
        this.ficheiro = ficheiro;
        this.nDownloads=0;
    }

    public int getId() {
        return this.id;
    }

    public String getTitulo() {
        return this.titulo;
    }

    public String getInterprete() {
        return this.interprete;
    }

    public String getAno() {
        return this.ano;
    }

    public byte[] getFicheiro() {
        return this.ficheiro;
    }

    public int getNDownloads(){
        return this.nDownloads;
    }


    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Musica aux = (Musica) o;
        return super.equals(aux) && this.id == aux.getId()
                && this.titulo.equals(aux.getTitulo())
                && this.interprete.equals(aux.getInterprete())
                && this.ano.equals(aux.getAno());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ID: ");
        sb.append(this.id+" ");
        sb.append("Título: ");
        sb.append(this.titulo+" ");
        sb.append("Interprete: ");
        sb.append(this.interprete+" ");
        sb.append("Ano: ");
        sb.append(this.ano+" ");
        sb.append("Numero de downloads: ");
        sb.append(this.nDownloads);

        return sb.toString();
    }
}
