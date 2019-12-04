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


    public Worker(Socket clisock, Repositorio repositorio) throws IOException{
        this.clSock =  clisock;
        this.repositorio= repositorio;
    }

    public void logout(String s) throws IOException{}
    public void autenticacao(String email, boolean querRegistar) throws IOException {}
    public byte[] download(String s) throws  IOException{ return  null;}
    public void upload(String metadados) throws  IOException{}
    public Musica procuraMusicaID(int i){return null;}
    public List<Musica> procuraMusicaPAR(String s){ return null;}

    public void responde(String s){
        String result;
        String[] comandos = s.split(" ");

        switch (comandos[0]){
            case "login":
                int id = Integer.parseInt(comandos[2]);
                try{
                repositorio.login(comandos[1],id);}
                catch (IOException e ){}
                catch(CredenciaisInvalidasException e){}
                catch(ClientesSTUBException e){}
                break;

            case "logout":
                //vale a pena manter no repositorio quem está login?
                try{
                repositorio.logout(comandos[0]);}
                catch (IOException e){}
                catch (ClientesSTUBException e){}

                break;

            case "registar":
                try{
                int pass=Integer.parseInt(comandos[2]);
                repositorio.registarUtilizador(comandos[1],pass);}
                catch (ClientesSTUBException e){}
                catch (UtilizadorJaExisteException e){}

                break;

            case "upload":
                try{
                int ano=Integer.parseInt(comandos[3]);
                repositorio.upload(comandos[1],comandos[2],ano,comandos[4]);}
                catch (IOException e){}
                catch (ClientesSTUBException e){}
                catch (UtilizadorNaoAutenticadoException e){}

                break;
            case "download":
                try{
                    int nrm= Integer.parseInt(comandos[1]);
                    repositorio.download(nrm);
                }
                catch (MusicaInexistenteException e){}
                catch (IOException e){}
                catch (ClientesSTUBException e){}
                catch (UtilizadorNaoAutenticadoException e){}



                break;
            case "procurarMusica":
                try{
                    repositorio.procuraMusica(comandos[1],comandos[2]);
                }
                catch (MusicaInexistenteException e){}
                catch (IOException e){}
                catch (ClientesSTUBException e){}
                catch (UtilizadorNaoAutenticadoException e){}


                break;



            default:
                System.out.println ("Opcao invalida");

        }

    }


    public void run(){
        try {
            if (clSock == null) System.out.println("clSock null");

            //escreve no canal
            PrintWriter out = new PrintWriter(clSock.getOutputStream());
            //lê do canal
            BufferedReader in = new BufferedReader(new InputStreamReader(this.clSock.getInputStream()));


            //passa o que li do canal para uma string
            String inComing=in.readLine();

            System.out.println(inComing);
            while(inComing!=null){

                //if(inComing.equals("teste"))
                //responde(inComing);

                out.println();
                out.flush();

                inComing = in.readLine();

            }
            clSock.shutdownOutput();
            clSock.shutdownInput();
            clSock.close();
        }
        catch (IOException e){}
    }

}


