package com.jetbrains.Client;

import com.jetbrains.Exceptions.*;
import com.jetbrains.Server.Musica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Cliente {
    ClienteSTUB cStub;
    BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));

    private void caminhoServidor(String ip, Integer port) {
        cStub = new ClienteSTUB("127.0.0.1", 12346);
    }

    private synchronized void conectar() {
        boolean connected = false;
        while (!connected) {
            try {
                cStub.conectar(); //connect do stub
                connected = true;
            } catch (ClientesSTUBException e) {
                e.printStackTrace();
            }
        }
    }

    private void autenticacao(String nome, boolean querRegistar) throws IOException, UtilizadorNaoAutenticadoException, CredenciaisInvalidasException {
        System.out.println("Agora introduza a sua password");
        String password = terminal.readLine();
        try {
            if (querRegistar) { //querRegistar = true se se quer registar, querRegistar = false se só quer o login
                cStub.registarUtilizador(nome, password);
                System.out.println("Utilizador registado com sucesso");
            }

            cStub.login(nome, password);

            System.out.println("Utilizador autenticado, bem vindo");
            System.out.println("Se quiser procurar alguma música escreva: procurarMusica ");
            System.out.println("Se quiser fazer upload de uma música escreva: upload ");
            System.out.println("Se quiser fazer o download de uma música escreva: download ");
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

    private void logout() {
        System.out.println("Adeus");
        try{
            cStub.logout(null);
            System.exit(0);
        }
        catch (IOException e){
            System.out.println("Logout não executado, tente de novo");
        }
    }

    public void download(int id) {
        try{
            cStub.download(id);
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
        String ano = terminal.readLine();

        System.out.println("Insira o género musical");
        String genero = terminal.readLine();

        System.out.println("Insira o caminho para o ficheiro a fazer upload");
        String caminho = terminal.readLine();

        try{
            cStub.upload(caminho, nome, interprete, ano, genero);
            System.out.println("Upload concluido com sucesso");
        }
        catch (UtilizadorNaoAutenticadoException e){
            System.out.println("Por favor aguarde...");
        }
        catch (ClientesSTUBException e){
            e.printStackTrace();
        }
    }

    public List<Musica> procuraMusica() throws IOException {
        List<Musica> m = new ArrayList<>();

        try{
            System.out.println("Insira a etiqueta a procurar");
            String etiqueta = terminal.readLine();

            //System.out.println("Insira o que procurar");
            //String oQp = terminal.readLine();

            m = cStub.procuraMusica(etiqueta);

            System.out.println(m);
        }
        catch (UtilizadorNaoAutenticadoException e){
            System.out.println("Por favor aguarde...");
        }
        catch (MusicaInexistenteException e){
            System.out.println(e.getMessage());
            System.out.println("Não existe nenhuma música com essa etiqueta");
            System.out.println("Insira a etiqueta a procurar");
            procuraMusica();
        }
        catch (ClientesSTUBException e){
            e.printStackTrace();
        }

        return m;
    }

    public static void start(String ip, Integer porto) throws ClientesSTUBException{
        Cliente cliente = new Cliente();
        cliente.caminhoServidor(ip, porto);
        cliente.conectar();

        cliente.opcoes();

        try {
            cliente.executaComandos();
        } catch (IOException e) {
            System.out.println("Erro ao executar comando");
        }
    }

    private void opcoes() {
        System.out.println("Para fazer login escreva login e o seu nome: ");

        System.out.println("Para criar uma conta escreva registar e o nome do utilizador: ");
    }


    private void executaComandos() throws IOException{
        String comando;
        String[] arrayComandos;

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
                        logout();
                        break;
                    case "download":
                        int id = Integer.parseInt(arrayComandos[1]);
                        System.out.println("Estou comando download e o id da musica pretendida: "+arrayComandos[1]+" parse integer :"+id);
                        download(id);
                        System.out.println("Passei o download");
                        break;
                    case "upload":
                        upload();
                        break;
                    case "procurarMusica":
                        procuraMusica();
                        break;
                    case "sair":
                        System.exit(0);
                    default:
                        System.out.println("Comando introduzido não existe. Volte a tentar");
                }

            } catch (UtilizadorNaoAutenticadoException e) {
                System.out.println("Utilizador não está autenticado");
            }
            catch (Exception e) {
                System.out.println("Excepção desconhecida.");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, UtilizadorNaoAutenticadoException {
       try {
           Cliente.start("127.0.0.1", 12345);
       }
       catch (ClientesSTUBException e){
           System.out.println("Erro na conexão");
       }
    }
}
