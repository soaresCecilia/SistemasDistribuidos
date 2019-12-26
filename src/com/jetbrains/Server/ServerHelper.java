package com.jetbrains.Server;

import com.jetbrains.*;
import com.jetbrains.Exceptions.*;

import java.io.*;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;

public class ServerHelper implements SoundCloud {
    private Repositorio repositorio;
    private Socket clSock;
    public static String FILE_TO_SEND = "/home/luisabreu/Desktop/trabalhoSD";
    private PrintWriter out;
    private Worker w;

    public ServerHelper(Socket clsock, Repositorio r) throws IOException {
        this.repositorio = r;
        this.clSock = clsock;

    }

    public ServerHelper() throws IOException {
        this.repositorio = new Repositorio();
    }


    public void connect() throws IOException {

    }


    @Override
    public void login(String nome, String password) throws IOException, CredenciaisInvalidasException, ClientesSTUBException {
            Utilizador user;

            user = repositorio.getUtilizadores().get(nome);

            this.out = new PrintWriter(clSock.getOutputStream());

            if (user == null || !user.autentica(nome, password)) {
                throw new CredenciaisInvalidasException("Utilizador não está registado");
            }
            else if (!user.getActivo() && user.autentica(nome, password)){
                user.setActivo();
            }


    }

    @Override
    public void logout(String nome) throws IOException, ClientesSTUBException {
        Utilizador u = repositorio.getUtilizadores().get(nome);

        if ( u != null && u.getActivo())
            u.setInactivo();

    }

    @Override
    public void registarUtilizador(String email, String password) throws UtilizadorJaExisteException, ClientesSTUBException {

    }

    @Override
    public void download(int idMusica) throws IOException, MusicaInexistenteException, UtilizadorNaoAutenticadoException, ClientesSTUBException {



    }

    @Override
    public void upload(String nome, String interprete, int ano, String caminho) throws IOException, UtilizadorNaoAutenticadoException, ClientesSTUBException {

    }

    @Override
    public void procuraMusica(String etiqueta) throws IOException, UtilizadorNaoAutenticadoException, MusicaInexistenteException, ClientesSTUBException {

    }


}
