package com.jetbrains.Server;

import com.jetbrains.Exceptions.ClienteServerException;
import com.jetbrains.Exceptions.CredenciaisInvalidasException;
import com.jetbrains.Exceptions.MusicaInexistenteException;
import com.jetbrains.Exceptions.UtilizadorJaExisteException;

import java.io.IOException;
import java.net.Socket;

public class PedidoUpload extends PedidoCliente {
    private String tamanho;
    private String titulo;
    private String interprete;
    private String ano;
    private String genero;

    public PedidoUpload(ServerHelper s, String tamanho, String titulo, String interprete, String ano, String genero) {
        super(s);
        this.tamanho = tamanho;
        this.titulo = titulo;
        this.interprete = interprete;
        this.ano = ano;
        this.genero = genero;
    }


    public String getTamanho() {
        return tamanho;
    }

    public void setNome(String nome) {
        this.tamanho = nome;
    }

    public String getInterprete() {
        return interprete;
    }

    public void setInterprete(String interprete) {
        this.interprete = interprete;
    }

    public String getAno() {
        return ano;
    }

    public void setAno(String ano) {
        this.ano = ano;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String caminho) {
        this.titulo = titulo;
    }

    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        PedidoUpload aux = (PedidoUpload) o;
        return  (this.tamanho.equals(aux.getTamanho()) &&
                this.interprete.equals(aux.getInterprete()) &&
                this.ano.equals(aux.getAno()) &&
                this.genero.equals(aux.getGenero()) &&
                this.titulo.equals(aux.titulo));
    }


    @Override
    public void executar() {
        ServerHelper s = getServerHelper();

        s.upload(getTamanho(), getTitulo(), getInterprete(), getAno(), getGenero());

        notificaEspera();
    }
}
