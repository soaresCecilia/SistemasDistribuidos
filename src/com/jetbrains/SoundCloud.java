package com.jetbrains;

import com.jetbrains.Exceptions.*;
import com.jetbrains.Server.Musica;

import java.io.IOException;
import java.util.List;

public interface SoundCloud {

   void login(String email, String password) throws IOException, CredenciaisInvalidasException, ClienteServerException;

   void logout(String s) throws  IOException, ClienteServerException;

   void registarUtilizador(String email, String password) throws UtilizadorJaExisteException, ClienteServerException,CredenciaisInvalidasException;


   void download(int idMusica) throws IOException, MusicaInexistenteException, UtilizadorNaoAutenticadoException,ClienteServerException;


   void upload(String tamanho, String titulo, String interprete, String ano, String genero) throws IOException, UtilizadorNaoAutenticadoException,ClienteServerException;

   List<Musica> procuraMusica (String etiqueta) throws IOException, UtilizadorNaoAutenticadoException, MusicaInexistenteException, ClienteServerException;

}
