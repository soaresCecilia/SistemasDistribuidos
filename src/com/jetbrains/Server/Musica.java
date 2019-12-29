package com.jetbrains.Server;

import javax.xml.crypto.Data;

public class Musica {
    private int id;
    private String titulo;
    private String interprete;
    private String ano;
    private String genero;
    private String caminhoFicheiro;
    private int nDownloads;

    public Musica(int id, String titulo, String interprete, String ano, String genero, String ficheiro, int nDownloads) {
        this.id = id;
        this.titulo = titulo;
        this.interprete = interprete;
        this.ano = ano;
        this.genero = genero;
        this.caminhoFicheiro = ficheiro;
        this.nDownloads = nDownloads;
    }

    public Musica(String titulo, String interprete, String ano, String genero, String ficheiro) {
        this.id = 0;
        this.titulo = titulo;
        this.interprete = interprete;
        this.ano = ano;
        this.genero = genero;
        this.caminhoFicheiro = ficheiro;
        this.nDownloads = 0;
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id){
        this.id = id;
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

    public String getGenero() { return this.genero; }

    public String getCaminhoFicheiro() {
        return this.caminhoFicheiro;
    }

    public int getNDownloads(){
        return this.nDownloads;
    }

    public void growNDowloads(){
        this.nDownloads++;
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
                && this.ano.equals(aux.getAno())
                && this.genero.equals(aux.getGenero())
                && this.caminhoFicheiro.equals((aux.getCaminhoFicheiro()));
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ID: ");
        sb.append(this.id + " ");
        sb.append("Título: ");
        sb.append(this.titulo + " ");
        sb.append("Interprete: ");
        sb.append(this.interprete+" ");
        sb.append("Ano: ");
        sb.append(this.ano + " ");
        sb.append("Genero: ");
        sb.append(this.genero + " ");
        sb.append("Caminho: ");
        sb.append(this.caminhoFicheiro + " ");
        sb.append("Numero_downloads: ");
        sb.append(this.nDownloads);

        return sb.toString();
    }

    public void addNDownload(){

        this.nDownloads++;
    }


    public boolean procuraEtiqueta(String etiqueta) {
        return (this.titulo.contains(etiqueta) || this.interprete.contains(etiqueta)
            || this.ano.equals(etiqueta) || this.genero.equals(etiqueta));
    }
}