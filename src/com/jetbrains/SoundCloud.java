package com.jetbrains;

import com.jetbrains.Exceptions.*;
import com.jetbrains.Server.Musica;

import java.io.IOException;
import java.util.List;

public interface SoundCloud {

   void login(String email, String password) throws IOException, CredenciaisInvalidasException, ClientesSTUBException;

   void logout(String s) throws  IOException, ClientesSTUBException;

   void registarUtilizador(String email, String password) throws UtilizadorJaExisteException,ClientesSTUBException;

   void download(int idMusica) throws IOException, MusicaInexistenteException, UtilizadorNaoAutenticadoException,ClientesSTUBException;

   void upload(String tamanho, String titulo, String interprete, String ano, String genero) throws IOException, UtilizadorNaoAutenticadoException,ClientesSTUBException;

   List<Musica> procuraMusica (String etiqueta) throws IOException, UtilizadorNaoAutenticadoException, MusicaInexistenteException, ClientesSTUBException;

}
