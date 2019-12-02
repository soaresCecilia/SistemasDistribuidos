package com.jetbrains;

import java.io.IOException;
import java.util.List;

public interface SoundCloud {

    void login(String email, String Password) throws IOException, CredenciaisInvalidasException;
    void logout(String s) throws  IOException;
    void registarUtilizador(String email, String password) throws IOException, UtilizadorJaExisteException;
    void download(String s) throws IOException,MusicaInexistenteException, UtilizadorNaoAutenticadoException;
    void upload(String s) throws IOException, UtilizadorNaoAutenticadoException;
    void connect() throws IOException;
    void disconnect() throws IOException;
    void procuraMusica (String m) throws IOException, UtilizadorNaoAutenticadoException, MusicaInexistenteException;

}
