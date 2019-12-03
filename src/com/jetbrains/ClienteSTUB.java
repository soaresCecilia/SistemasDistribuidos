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

    public void login(String email, int password) throws CredenciaisInvalidasException, ClientesSTUBException{

        out.println("login "+email+" "+password);
        out.flush();

        try {
            String le = inBuffer.readLine();

            String[] rsp = le.split(" ");
            switch (rsp[0]) {
                case "1": //correu tudo bem
                    break;
                default:
                    throw new CredenciaisInvalidasException("Credenciais inválidas");

            }
        }
        catch (IOException e){
            throw new ClientesSTUBException("Ocorreu um erro na ligação");
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
    public void registarUtilizador(String email, int password) throws UtilizadorJaExisteException, ClientesSTUBException{
        out.println("registarUtilizador "+email+" "+password);
        out.flush();

        try {
            String le = inBuffer.readLine();

                String[] rsp = le.split(" ");
                switch (rsp[0]) {
                    case "1":
                        break;
                    default:
                        throw new UtilizadorJaExisteException();

                }

        }
        catch (IOException e){
            throw new ClientesSTUBException("Erro na ligação com o servidor");
        }
    }
    public void download(int id) throws MusicaInexistenteException, UtilizadorNaoAutenticadoException, ClientesSTUBException{
        String idM ="download"+id;
        out.println(idM);
        out.flush();

        try {
            String le = inBuffer.readLine();

            String[] rsp = le.split(" ");
            switch (rsp[0]) {
                case "0":
                    throw new UtilizadorNaoAutenticadoException();

                case "1":
                    break;

                default:
                    throw new MusicaInexistenteException();
            }
        }
        catch (IOException e){
            throw  new ClientesSTUBException("Ocorreu um erro na ligaçao");
        }

    }
    public void upload(String nome, String interprete, int ano, String caminho) throws UtilizadorNaoAutenticadoException, ClientesSTUBException{

        out.println("upload "+nome+" "+interprete+" "+ano+" "+caminho+" ");
        out.flush();

        try {
        String le =inBuffer.readLine();

            String[] rsp = le.split(" ");
            switch (rsp[0]){
                case "1":
                    break;
                default:
                    throw new UtilizadorNaoAutenticadoException();

            }
        }
        catch (IOException e){
            throw new ClientesSTUBException( "Ocorreu um erro com o servidor");
        }

    }



    public void procuraMusica (String etiqueta, String queProcurar) throws ClientesSTUBException, UtilizadorNaoAutenticadoException, MusicaInexistenteException{

        String prM = "procura "+etiqueta+" "+queProcurar;

        out.println(prM);
        out.flush();

        try{
        String le =inBuffer.readLine();

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
        catch (IOException e){
            throw new ClientesSTUBException("Ocorreu um erro com o servidor");
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
