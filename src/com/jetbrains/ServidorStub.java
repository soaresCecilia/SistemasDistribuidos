package com.jetbrains;

import java.io.IOException;

public class ServidorStub {
    private Repositorio repositorio;



    public void login(String email, String Password) throws IOException, CredenciaisInvalidasException {

    }


    public void logout() throws IOException {

    }


    public void registarUtilizador(String email, String password) throws IOException, UtilizadorJaExisteException {
        //tem de imprimir para o canal para falar com o ClienteStub;


       // this.repositorio.adicionaUtilizador(email, password);

    }


    public void download(Integer id) throws IOException, MusicaInexistenteException, UtilizadorNaoAutenticadoException {

    }


    public void upload(String s) throws IOException, UtilizadorNaoAutenticadoException {

    }


    public void connect() throws IOException {

    }


    public void procuraMusica(String m) throws IOException, UtilizadorNaoAutenticadoException, MusicaInexistenteException {

    }
}
