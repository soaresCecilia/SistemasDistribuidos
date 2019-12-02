package com.jetbrains;

import java.io.*;
import java.net.Socket;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Worker implements Runnable {

    private Socket clSock;
    private int id;
    private Repositorio repositorio;
    ServidorStub sStub;

    public Worker(Socket clsock, Repositorio repositorio) throws IOException{
        this.clSock =  clsock;
        this.repositorio= repositorio;

        this.sStub = new ServidorStub(this.repositorio);
    }

    public void logout(String s) throws IOException{}
    public void autenticacao(String email, boolean querRegistar) throws IOException {}
    public byte[] download(String s) throws  IOException{ return  null;}
    public void upload(String metadados) throws  IOException{}
    public Musica procuraMusicaID(int i){return null;}
    public List<Musica> procuraMusicaPAR(String s){

        return null;}

    public void run(){
        try {
            if (clSock == null) System.out.println("clSock null");

            //lê do canal
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clSock.getInputStream()));
            //escreve no canal
            PrintWriter out = new PrintWriter(clSock.getOutputStream());

            //passa o que li do canal para uma string
            String inComing=in.readLine();

             String[] comandos = inComing.split(" ");



            while(inComing!=null){

                switch (comandos[0]){
                    case "login":
                        //como faço login no repositorio??
                        autenticacao(comandos[1],false);

                        out.println("1");
                        out.flush();
                        break;

                    case "logout":
                        //vale a pena manter no repositorio quem está login?
                        logout(comandos[0]);
                        out.println("1");
                        out.flush();
                        break;

                    case "registar":
                        autenticacao(comandos[1],true);
                        out.println("1");
                        out.flush();
                        break;

                    case "procurarID":

                        int idM= Integer.parseInt(comandos[1]);
                        Musica m = procuraMusicaID(idM);
                        out.println(m.toString());
                        out.flush();
                        break;
                    case "procurarPar":

                        List<Musica> lm=procuraMusicaPAR(comandos[1]);
                        int tam = lm.size();

                        out.println(""+tam);

                        for(Musica musica: lm){
                            out.println(musica.toString());
                        }

                        out.flush();

                        break;

                    case "upload":
                            upload(comandos[1]);
                            out.println("1");
                            out.flush();
                            break;
                    case "download":

                        FileInputStream fis = null;
                        BufferedInputStream bis = null;
                        OutputStream os = null;
/*
                        //File myFile = new File (FILE_TO_SEND);
                       // byte [] mybytearray  = new byte [(int)myFile.length()];
                        fis = new FileInputStream(myFile);
                        bis = new BufferedInputStream(fis);
                        bis.read(mybytearray,0,mybytearray.length);
                        os = clSock.getOutputStream();

                        os.write(mybytearray,0,mybytearray.length);
                        byte[] musica= download(comandos[1]);

                        //in.read(musica,0,musica.length);
                        //out.write(musica,0,musica.length);
*/
                        out.println("ServidorWorker encontrou ??? asdad");
                        out.flush();
                        break;

                    default:
                        out.println("Opcao invalida");
                        out.flush();
                }

                inComing = in.readLine();
                comandos = inComing.split(" ");
            }
            clSock.shutdownOutput();
            clSock.shutdownInput();
            clSock.close();
        }
        catch (IOException e){}
    }

}


