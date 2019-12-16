package com.jetbrains.Server;

import com.jetbrains.*;
import com.jetbrains.Exceptions.*;

import java.io.*;

public class ServerHelper implements SoundCloud {
    private Repositorio repositorio;
    public static String FILE_TO_SEND = "/home/luisabreu/Desktop/trabalhoSD";

    public ServerHelper(Repositorio r){
        this.repositorio = r;
    }

    public ServerHelper() {
        this.repositorio = new Repositorio();
    }




    public byte[] download(Integer id) throws IOException, MusicaInexistenteException, UtilizadorNaoAutenticadoException {

        String n = FILE_TO_SEND+id+".mp3";

        File myFile = new File(FILE_TO_SEND);
        byte [] mybytearray  = new byte [(int)myFile.length()];

        return mybytearray;



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
