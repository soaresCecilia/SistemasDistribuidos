package com.jetbrains;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class Cliente {
    ClienteSTUB cStub;



    private void caminhoServidor(String ip, Integer port){
        cStub = new ClienteSTUB("127.0.0.1", 12345);
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

        cStub.connect();

        try {
            if (querRegistar) { //querRegistar = true se se quer registar, querRegistar = false se só quer o login
                cStub.registarUtilizador(email,password);
            }
            cStub.login(email,password);

            System.out.println("Utilizador autenticado");
        } catch (CredenciaisInvalidasException e) {
            System.out.println("Credenciais Inválidas");
        } catch (UtilizadorJaExisteException e) {
            System.out.println("Utilizador já existe");
        }
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


        while (true) {
            comando = terminal.readLine();


            arrayComandos = comando.split(" ");


            try {
                switch (arrayComandos[0]) {
                    case "login":
                        autenticacao(arrayComandos[1], false);
                        break;
                    case "registar":
                        autenticacao(arrayComandos[1], true);
                        break;
                    case "logout":
                        System.out.println("Adeus");
                        System.exit(0);
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

        }
    }



    public static void main(String[] args) throws IOException, InterruptedException, UtilizadorNaoAutenticadoException {
        Cliente.start("127.0.0.1", 12345);

    }
}
