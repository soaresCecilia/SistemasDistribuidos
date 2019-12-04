package com.jetbrains;

import java.io.IOException;
import java.util.List;

public interface SoundCloud {

    void login(String email, int password) throws IOException, CredenciaisInvalidasException, ClientesSTUBException;
    void logout(String s) throws  IOException, ClientesSTUBException;
    void registarUtilizador(String email, int password) throws UtilizadorJaExisteException,ClientesSTUBException;
    void download(int idMusica) throws IOException,MusicaInexistenteException, UtilizadorNaoAutenticadoException,ClientesSTUBException;
    void upload( String nome, String interprete, int ano, String caminho) throws IOException, UtilizadorNaoAutenticadoException,ClientesSTUBException;
    void procuraMusica (String etiqueta, String oQueProcurar) throws IOException, UtilizadorNaoAutenticadoException, MusicaInexistenteException, ClientesSTUBException;

}
