package com.jetbrains;

import com.jetbrains.Exceptions.*;

import java.io.IOException;

public interface SoundCloud {

   void login(String email, String password) throws IOException, CredenciaisInvalidasException, ClientesSTUBException;

   void logout(String s) throws  IOException, ClientesSTUBException;

   void registarUtilizador(String email, String password) throws UtilizadorJaExisteException,ClientesSTUBException;

   void download(int idMusica) throws IOException, MusicaInexistenteException, UtilizadorNaoAutenticadoException,ClientesSTUBException;

   void upload( String nome, String interprete, int ano, String caminho) throws IOException, UtilizadorNaoAutenticadoException,ClientesSTUBException;

   void procuraMusica (String etiqueta) throws IOException, UtilizadorNaoAutenticadoException, MusicaInexistenteException, ClientesSTUBException;

}
