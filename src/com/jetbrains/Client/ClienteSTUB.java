package com.jetbrains.Client;

import com.jetbrains.*;
import com.jetbrains.Exceptions.*;

import java.io.*;
import java.net.Socket;

public class ClienteSTUB implements SoundCloud {
    private Socket socket;
    private final String ip;
    private final Integer porto;
    private PrintWriter out;
    private BufferedReader inBuffer;
    public final static int FILE_SIZE = 6022386;
    public static String PATH_TO_RECEIVE = "/home/luisabreu/Desktop/musicaC/";

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

    public void login(String nome, String password) throws CredenciaisInvalidasException, ClientesSTUBException {

        out.println("login "+nome+" "+password);
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
    public void registarUtilizador(String email, String password) throws UtilizadorJaExisteException, ClientesSTUBException{
        out.println("registarUtilizador "+email+" "+password);
        out.flush();

        try {
            String le = inBuffer.readLine();

                String[] rsp = le.split(" ");
                switch (rsp[0]) {
                    case "1": //correu tudo bem
                        System.out.println("Ola");
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


        System.out.println("do que eu fiz flush: "+idM);

        try {



            String le = inBuffer.readLine();

            String[] rsp = le.split(" ");
            switch (rsp[0]) {
                case "0": // PRECISAMOS DE VER SE O UTILIZADOR ESTA AUTENTICADO??????????
                    throw new UtilizadorNaoAutenticadoException("Utilizador não autenticado.");
                case "1": //correu tudo bem

                    String pathSaveMusica = PATH_TO_RECEIVE+rsp[2]+".mp3";

                    int tamanhoFile = Integer.parseInt(rsp[1]);

                    byte[] mybytearray = new byte[1024];

                    InputStream is = socket.getInputStream();

                    FileOutputStream fos = new FileOutputStream(pathSaveMusica);

                    BufferedOutputStream bos = new BufferedOutputStream(fos);

                    int bytesIni=0;

                    while (bytesIni<tamanhoFile)       {

                        int bytesRead = is.read(mybytearray, 0, mybytearray.length);

                        bos.write(mybytearray, 0, bytesRead);

                        bytesIni+= bytesRead;
                    }

                    bos.close();

                    break;
                default:
                    throw new MusicaInexistenteException("Não existe esse id nas musicas");
            }


         }
        catch (IOException e){
            throw  new ClientesSTUBException("Ocorreu um erro na ligaçao");
        }

    }
    public void upload(String caminho, String titulo, String interprete, String ano, String genero) throws UtilizadorNaoAutenticadoException, ClientesSTUBException{
        try {


            File myFile = new File(caminho);

            byte[] mybytearray = new byte[(int) myFile.length()];

            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(myFile));

            bis.read(mybytearray, 0, mybytearray.length);

            OutputStream os = socket.getOutputStream();

            out.println("upload " + mybytearray.length + " " + titulo + " " + interprete + " " + ano + " " + genero);
            out.flush();


            os.write(mybytearray, 0, mybytearray.length);
            os.flush();
        }
        catch (IOException e){};



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
