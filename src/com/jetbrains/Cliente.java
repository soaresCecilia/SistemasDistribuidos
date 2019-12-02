package com.jetbrains;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class Cliente {
    ClienteSTUB cStub;



    private void caminhoServidor(String ip, Integer port){
        cStub = new ClienteSTUB("127.0.0.1", 12346);
    }


    private synchronized void connect() throws IOException, InterruptedException {
        boolean connected = false;
        while (!connected) {
            try {
                cStub.connect(); //connect do stub
                connected = true;
            } catch (IOException e) {
             e.printStackTrace();
            }

        }
    }


    private void autenticacao(String email, boolean querRegistar) throws IOException {
        BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("password");
        String password = terminal.readLine();

        try {
            if (querRegistar) { //querRegistar = true se se quer registar, querRegistar = false se só quer o login
                cStub.registarUtilizador(email,password);
                System.out.println("Utilizador registado com sucesso");
            }
            cStub.login(email,password);

            System.out.println("Utilizador autenticado, bem vindo");
        }
        catch (CredenciaisInvalidasException e) {
            System.out.println("Credenciais Inválidas");
        }
        catch (UtilizadorJaExisteException e) {
            System.out.println("Utilizador já existe");
        }
    }
    private void logout(String s){
        System.out.println("Adeus");
        try{cStub.logout(s);
        System.exit(0);}
        catch (IOException e){
            System.out.println("Logout não executado, tente de novo");
        }
    }

    public void download(String s) throws  IOException{

        try{

            cStub.download(s);
        }
        catch (MusicaInexistenteException e){
            System.out.println("Musica pretendida inexistente");
        }
        catch (UtilizadorNaoAutenticadoException e){
            System.out.println("Por favor aguarde...");
        }

    }


    public void upload(String metadados) throws  IOException{

        BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Insira o caminho para o ficheiro");

        String caminho = terminal.readLine();

        String metaCam = (new StringBuilder()).append(metadados).append(caminho).toString();

        try{

            cStub.upload( metaCam );
        }
        catch (UtilizadorNaoAutenticadoException e){
            System.out.println("Por favor aguarde...");
        }

    }

    public void procuraMusica(String s){
        try{

            cStub.procuraMusica(s);
        }
        catch (UtilizadorNaoAutenticadoException e){
            System.out.println("Por favor aguarde...");
        }
        catch (MusicaInexistenteException e){
            System.out.println("Não existe...");

        }
        catch (IOException e){}
    }

    public static void start(String ip, Integer porto) throws IOException, UtilizadorNaoAutenticadoException, InterruptedException{

        Cliente cliente = new Cliente();
        cliente.caminhoServidor(ip,porto);
        cliente.connect();

        try {
            cliente.comandos();
        } catch (IOException e) {
            cliente.connect();
        }
    }


    private void comandos() throws IOException, UtilizadorNaoAutenticadoException {
        String comando;
        String[] arrayComandos;

        BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));

        comando = terminal.readLine();

        while (true) {

            arrayComandos = comando.split(" ",2);

            int tam = arrayComandos.length;

            try {
                switch (arrayComandos[0]) {
                    case "login":
                        try{autenticacao(arrayComandos[1], false);}
                        catch (IOException e){
                            System.out.println("Algo correu mal, tente de novo");
                        }
                        break;
                    case "registar":
                        autenticacao(arrayComandos[1], true);
                        break;
                    case "logout":
                        logout(arrayComandos[0]);
                        break;
                    case "download":
                        download(comando);
                        break;
                    case "upload":
                        upload(comando);
                        break;
                    case "procuraID":
                        procuraMusica(comando);
                        break;
                    case "procuraPar":
                        procuraMusica(comando);
                        break;
                    default:
                        System.out.println("Comando introduzido não existe. Volte a tentar");
                        comandos();
                }

            } catch (UtilizadorNaoAutenticadoException e) {
                System.out.println("Utilizador não está autenticado");
                //comandos();
            }
            catch (Exception e) {

                comandos();
            }

            comando = terminal.readLine();

        }


    }

    public static void main(String[] args) throws IOException, InterruptedException, UtilizadorNaoAutenticadoException {
        Cliente.start("127.0.0.1", 12345);

    }
}
