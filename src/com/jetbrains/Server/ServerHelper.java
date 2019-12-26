package com.jetbrains.Server;

import com.jetbrains.*;
import com.jetbrains.Exceptions.*;

import java.io.*;
import java.net.Socket;

/*out = new PrintWriter(clSock.getOutputStream());*/

public class ServerHelper implements SoundCloud, Serializable {
    private Socket clSock;
    private Repositorio repositorio;
    PrintWriter out ;

    public static String FILE_TO_SEND = "/home/luisabreu/Desktop/musicaS/2.mp3";


    public ServerHelper(Socket cliSock, Repositorio r){
        this.repositorio = r;
        this.clSock = cliSock;
    }




    public void connect() throws IOException {

    }


    @Override
    public void login(String email, int password) throws IOException, CredenciaisInvalidasException, ClientesSTUBException {

    }

    @Override
    public void logout(String s) throws IOException, ClientesSTUBException {

    }

    @Override
    public void registarUtilizador(String email, int password) throws UtilizadorJaExisteException, ClientesSTUBException {

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
