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

        String le =inBuffer.readLine();
        if(le!=null) {
            String[] rsp = le.split(" ");
            switch (rsp[0]){
                case "1":
                    break;
                default:
                    throw new CredenciaisInvalidasException();

            }
        }
        else{
            throw new IOException();
        }

    }
    public void logout() throws IOException{
        try{
            out.println("logout");
            out.flush();
        }
        finally {
            socket.close();
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
    public void download(Integer id) throws IOException, MusicaInexistenteException, UtilizadorNaoAutenticadoException{
        out.println("download "+id);
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

    public void connect() throws IOException{

        this.socket = new Socket(this.ip,this.porto);

        out= new PrintWriter(socket.getOutputStream());
        inBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));

    }

    public String procurarUtilizador(String mensagem) throws IOException {

        String address = "127.0.0.1";
        short port = 12345;


        //criar socket (A maquina em que está o serviço, em que porto me devo conectar)
        Socket socket = new Socket(address, port);



        //escreve noo canal de ligacao cliente cervidor
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        //le do canal de ligação cliente servidor
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));



        out.println(mensagem);
        out.flush();


        String rsp = in.readLine();






        socket.shutdownOutput();
        socket.shutdownInput();
        socket.close();

        return rsp;

    }

}
