package com.jetbrains;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class Cliente {
    ClienteSTUB cStub;
    BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));

    private void caminhoServidor(String ip, Integer port){
        cStub = new ClienteSTUB("127.0.0.1", 12346);
    }

    private synchronized void connect() {
        boolean connected = false;
        while (!connected) {
            try {
                cStub.connect(); //connect do stub
                connected = true;
            } catch (ClientesSTUBException e) {
             e.printStackTrace();
            }

        }
    }

    private void autenticacao(String email, boolean querRegistar) throws IOException, UtilizadorNaoAutenticadoException{

        System.out.println("introduza a password");
        String password = terminal.readLine();
        int pass = Integer.parseInt(password);
        try {
            if (querRegistar) { //querRegistar = true se se quer registar, querRegistar = false se só quer o login
                cStub.registarUtilizador(email,pass);
                System.out.println("Utilizador registado com sucesso");
            }
            cStub.login(email,pass);

            System.out.println("Utilizador autenticado, bem vindo");
        }
        catch (CredenciaisInvalidasException e) {
            e.printStackTrace();
        }
        catch (UtilizadorJaExisteException e) {
           e.printStackTrace();
        }
        catch (ClientesSTUBException e){
            e.printStackTrace();
        }
    }
    private void logout(String s) {
        System.out.println("Adeus");
        try{cStub.logout(s);
        System.exit(0);}
        catch (IOException e){
            System.out.println("Logout não executado, tente de novo");
        }
    }

    public void download(int s) {

        try{
            cStub.download(s);
            System.out.println("Download concluido com sucesso");
        }
        catch (MusicaInexistenteException e){
            e.printStackTrace();
        }
        catch (UtilizadorNaoAutenticadoException e){
            e.printStackTrace();
        }
        catch (ClientesSTUBException e){
            e.printStackTrace();
        }
    }

    public void upload() throws  IOException{

        System.out.println("Insira o nome da musica");
        String nome = terminal.readLine();

        System.out.println("Insira o interprete da musica");
        String interprete = terminal.readLine();

        System.out.println("Insira o ano da musica");
        String a = terminal.readLine();
        int ano = Integer.parseInt(a);

        System.out.println("Insira o caminho para o ficheiro a fazer upload");
        String caminho = terminal.readLine();

        try{
            cStub.upload( nome, interprete, ano,caminho);
        }
        catch (UtilizadorNaoAutenticadoException e){
            System.out.println("Por favor aguarde...");
        }
        catch (ClientesSTUBException e){
            e.printStackTrace();
        }

    }

    public void procuraMusica(String s) throws IOException {

        try{
            System.out.println("Insira a etiqueta a procurar");
            String etiqueta = terminal.readLine();

            System.out.println("Insira o que procurar");
            String oQp = terminal.readLine();

            cStub.procuraMusica(etiqueta,oQp);
        }
        catch (UtilizadorNaoAutenticadoException e){
            System.out.println("Por favor aguarde...");
        }
        catch (MusicaInexistenteException e){
            System.out.println("Não existe...");

        }
        catch (ClientesSTUBException e){
            e.printStackTrace();
        }
    }

    public static void start(String ip, Integer porto) throws ClientesSTUBException{

        Cliente cliente = new Cliente();
        cliente.caminhoServidor(ip,porto);
        cliente.connect();

        try {
            cliente.comandos();
        } catch (IOException e) {
            cliente.connect();
        }
    }

    public void testeF(){

        String s = cStub.testeF();
        System.out.println(s);
    }

    private void comandos() throws IOException{
        String comando;
        String[] arrayComandos;

        comando = terminal.readLine();

        while (true) {

            arrayComandos = comando.split(" ");

            int tam = arrayComandos.length;

            try {
                switch (arrayComandos[0]) {
                    case "teste":
                        testeF();
                    case "login":
                        autenticacao(arrayComandos[1], false);
                        break;
                    case "registar":
                        autenticacao(arrayComandos[1], true);
                        break;
                    case "logout":
                        logout(arrayComandos[0]);
                        break;
                    case "download":
                        int id= Integer.parseInt(arrayComandos[1]);
                        download(id);
                        break;
                    case "upload":
                        upload();
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

    public static void main(String[] args) throws InterruptedException, UtilizadorNaoAutenticadoException {
       try {
           Cliente.start("127.0.0.1", 12345);
       }
       catch (ClientesSTUBException e){
           System.out.println("Erro conecção");

       }
    }
}
