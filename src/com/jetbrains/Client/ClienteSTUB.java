package com.jetbrains.Client;

import com.jetbrains.*;
import com.jetbrains.Exceptions.*;

import java.io.*;
import java.net.Socket;

public class ClienteSTUB implements SoundCloud, Serializable {
    private Socket socket;
    private final String ip;
    private final Integer porto;
    private PrintWriter out;
    private BufferedReader inBuffer;
    public final static int FILE_SIZE = 6022386;
    public static String FILE_TO_RECEIVE = "/home/luisabreu/Desktop/musicaC/2.mp3";

    public ClienteSTUB(String ip, Integer porto){
        this.ip = ip;
        this.porto = porto;
    }

    public String testeF(){
        try {
            out.println("teste");
            out.flush();

        String r = inBuffer.readLine();
        return r;
        }
        catch (IOException e){return "correu mal";}
    }

    public void login(String email, int password) throws CredenciaisInvalidasException, ClientesSTUBException {

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
                    case "1": //correu tudo bem
                        break;
                    default:
                        throw new UtilizadorJaExisteException("Não pode registar esse utilizador porque ele já existe");

                }

        }
        catch (IOException e){
            throw new ClientesSTUBException("Erro na ligação com o servidor");
        }
    }
    public void download(int id) throws MusicaInexistenteException, UtilizadorNaoAutenticadoException, ClientesSTUBException{
        String idM ="download "+id;
        out.println(idM);
        out.flush();

        try {
            /*
            String le = inBuffer.readLine();

            String[] rsp = le.split(" ");
            switch (rsp[0]) {
                case "0":
                    throw new UtilizadorNaoAutenticadoException("Utilizador não autenticado.");
                case "1": //correu tudo bem
                    break;
                default:
                    throw new MusicaInexistenteException("Não existe esse id nas musicas");
            }*/

            int bytesRead;

            int current = 0;

            FileOutputStream fos = null;
            BufferedOutputStream bos = null;
            Socket sock = socket;

            try {

                System.out.println("File "+FILE_TO_RECEIVE+" downloaded (" + current + " bytes read)");
            }
            finally {
                if (fos != null) fos.close();
                if (bos != null) bos.close();
                if (sock != null) sock.close();
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
                case "1": //correu tudo bem
                    break;
                default:
                    throw new UtilizadorNaoAutenticadoException("Utilizador não autenticado.");

            }
        }
        catch (IOException e){
            throw new ClientesSTUBException( "Ocorreu um erro com o servidor");
        }

    }



    public void procuraMusica (String etiqueta) throws ClientesSTUBException, UtilizadorNaoAutenticadoException, MusicaInexistenteException{

        String procuraEtiqueta = "procura "+ etiqueta;

        out.println(procuraEtiqueta);
        out.flush();

        try{
        String le =inBuffer.readLine();

            String[] rsp = le.split(" ");
            switch (rsp[0]){
                case "1": //correu tudo bem
                    break;
                case "0":
                    throw new MusicaInexistenteException("Não existe musica com essa etiqueta."); //ver se é excecão
                default:
                    throw new UtilizadorNaoAutenticadoException("Utilizador não autenticado.");

            }
        }
        catch (IOException e){
            throw new ClientesSTUBException("Ocorreu um erro com o servidor");
        }
    }


    public void connect() throws ClientesSTUBException {
        try{
        this.socket = new Socket(this.ip,this.porto);

        out= new PrintWriter(socket.getOutputStream());
        inBuffer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        }
        catch (IOException e){
            throw new ClientesSTUBException("Ocorreu um erro na ligação com o servidor");
        }

    }

    public void disconnect()throws  IOException{

        this.socket.shutdownOutput();
        this.socket.shutdownInput();
        this.socket.close();
    }


}
