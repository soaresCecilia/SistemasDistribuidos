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
    public static String PATH_TO_RECORD = "/home/luisabreu/Desktop/musicaS/";
    private PrintWriter out;
    private BufferedReader in;
    private Worker w;

    public ServerHelper(Socket clsock, Repositorio r) throws IOException {
        this.repositorio = r;
        this.clSock = clsock;
        this.out = new PrintWriter(clSock.getOutputStream());
        this.in = new BufferedReader(new InputStreamReader(clSock.getInputStream()));
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
        boolean n = repositorio.getMusicas().containsKey(idMusica);

        System.out.println("Existe ou não: "+n);

        if(repositorio.getMusicas().containsKey(idMusica)){

            Musica m = repositorio.getMusicaId(idMusica);

            File myFile = new File(m.getCaminhoFicheiro());

            byte[] mybytearray = new byte[(int) myFile.length()];

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));

            bis.read(mybytearray, 0, mybytearray.length);

            OutputStream os = clSock.getOutputStream();

            //envia a string "1 tamanhaFicheiroAler"
            System.out.println("tamanho do ficheiro lido: "+ mybytearray.length);

            String okTam = "1 "+mybytearray.length+" "+m.getTitulo();
            System.out.println(okTam);
            out.println(okTam);
            out.flush();
            System.out.println("fez bem o flucsh");
            //envia ficheiro pedido
            os.write(mybytearray, 0, mybytearray.length);

            os.flush();
        }
        else{
            throw new MusicaInexistenteException();

        }


    }

    @Override
    public void upload(String tamanho, String titulo, String interprete,  String ano, String genero) throws IOException, UtilizadorNaoAutenticadoException, ClientesSTUBException {

        String caminho = PATH_TO_RECORD+titulo+".mp3";

        int tamanhoFile =  Integer.parseInt(tamanho);

        byte[] mybytearray = new byte[1024];

        InputStream is = clSock.getInputStream();

        FileOutputStream fos = new FileOutputStream(caminho);

        BufferedOutputStream bos = new BufferedOutputStream(fos);

        int bytesIni=0;

        while (bytesIni<tamanhoFile)       {
            int bytesRead = is.read(mybytearray, 0, mybytearray.length);
            bos.write(mybytearray, 0, bytesRead);

            bytesIni+= bytesRead;
        }

        bos.close();
        bos.flush();

        Musica m= new Musica(titulo,interprete,ano,genero, caminho);
        repositorio.addMusica(m);

        out.println("1");
        out.flush();
    }

    @Override
    public void procuraMusica(String etiqueta) throws IOException, UtilizadorNaoAutenticadoException, MusicaInexistenteException, ClientesSTUBException {

    }


}
