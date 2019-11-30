package com.jetbrains;

import java.io.IOException;

public class ServidorStub implements SoundCloud {
    private Repositorio repositorio;


    @Override
    public void login(String email, String Password) throws IOException, CredenciaisInvalidasException {

    }

    @Override
    public void logout() throws IOException {

    }

    @Override
    public void registarUtilizador(String email, String password) throws IOException, UtilizadorJaExisteException {
        //tem de imprimir para o canal para falar com o ClienteStub;


       // this.repositorio.adicionaUtilizador(email, password);

    }

    @Override
    public void download(Integer id) throws IOException, MusicaInexistenteException, UtilizadorNaoAutenticadoException {

    }

    @Override
    public void upload(String s) throws IOException, UtilizadorNaoAutenticadoException {

    }

    @Override
    public void connect() throws IOException {

    }

    @Override
    public void procuraMusica(String m) throws IOException, UtilizadorNaoAutenticadoException, MusicaInexistenteException {

    }
}
