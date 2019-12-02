package com.jetbrains;

import java.io.*;
import java.net.Socket;

public class ClienteSTUB implements SoundCloud {
    private Socket socket;
    private final String ip;
    private final Integer porto;
    private PrintWriter out;
    private BufferedReader inBuffer;

    public ClienteSTUB(String ip, Integer porto){
        this.ip = ip;
        this.porto = porto;
    }

    public void login(String email, String password) throws IOException, CredenciaisInvalidasException{
        out.println("login "+email+" "+password);
        out.flush();

        String le = inBuffer.readLine();
        if(le!=null) {
            String[] rsp = le.split(" ");
            switch (rsp[0]){
                case "1": //correu tudo bem
                    break;
                default:
                    throw new CredenciaisInvalidasException();

            }
        }
        else{
            throw new IOException();
        }

    }


    public void logout(String s) throws IOException{
        try{
            out.println(s);
            out.flush();
        }
        finally {
            this.disconnect();
        }
    }
    public void registarUtilizador(String email, String password) throws IOException,UtilizadorJaExisteException{
        out.println("registarUtilizador "+email+" "+password);
        out.flush();

        String le =inBuffer.readLine();
        if(le!=null) {
            String[] rsp = le.split(" ");
            switch (rsp[0]){
                case "1":
                    break;
                default:
                    throw new UtilizadorJaExisteException();

            }
        }
        else{
            throw new IOException();
        }
    }
    public void download(String d) throws IOException, MusicaInexistenteException, UtilizadorNaoAutenticadoException{
        out.println(d);
        out.flush();

        String le =inBuffer.readLine();
        if(le!=null) {
            String[] rsp = le.split(" ");
            switch (rsp[0]){
                case "0":
                    throw new UtilizadorNaoAutenticadoException();

                case "1":
                    break;

                default:
                    throw new MusicaInexistenteException();

            }
        }
        else{
            throw new IOException();
        }
    }
    public void upload(String musica) throws IOException, UtilizadorNaoAutenticadoException{
        out.println("upload "+musica);
        out.flush();

        String le =inBuffer.readLine();
        if(le!=null) {
            String[] rsp = le.split(" ");
            switch (rsp[0]){
                case "1":
                    break;
                default:
                    throw new UtilizadorNaoAutenticadoException();

            }
        }
        else{
            throw new IOException();
        }
    }



    public void procuraMusica (String m) throws IOException, UtilizadorNaoAutenticadoException, MusicaInexistenteException{

        out.println(m);
        out.flush();

        String le =inBuffer.readLine();
        if(le!=null) {
            String[] rsp = le.split(" ");
            switch (rsp[0]){
                case "1":
                    break;
                case "2":
                    throw new MusicaInexistenteException();
                default:
                    throw new UtilizadorNaoAutenticadoException();

            }
        }
        else{
            throw new IOException();
        }
    }


    public void connect() throws IOException{

        this.socket = new Socket(this.ip,this.porto);

        out= new PrintWriter(socket.getOutputStream());
        inBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }

    public void disconnect()throws  IOException{

        this.socket.shutdownOutput();
        this.socket.shutdownInput();
        this.socket.close();
    }


}
