package com.jetbrains;

import java.io.*;
import java.net.Socket;

public class ServidorStub {
    private Repositorio repositorio;
    public static String FILE_TO_SEND = "/home/luisabreu/Desktop/trabalhoSD";

    public ServidorStub(Repositorio r){
        this.repositorio = r;
    }

    public void login(String email, String Password) throws IOException, CredenciaisInvalidasException {

    }


    public void logout() throws IOException {

    }


    public void registarUtilizador(String dados) throws IOException, UtilizadorJaExisteException {


    }


    public byte[] download(Integer id) throws IOException, MusicaInexistenteException, UtilizadorNaoAutenticadoException {

        String n = FILE_TO_SEND+id+".mp3";

        File myFile = new File(FILE_TO_SEND);
        byte [] mybytearray  = new byte [(int)myFile.length()];

        return mybytearray;



    }


    public void upload(String s) throws IOException, UtilizadorNaoAutenticadoException {

    }


    public void connect() throws IOException {

    }


    public void procuraMusica(String m) throws IOException, UtilizadorNaoAutenticadoException, MusicaInexistenteException {

    }
}
