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

    private void autenticacao(String nome, boolean querRegistar) throws IOException, UtilizadorNaoAutenticadoException, CredenciaisInvalidasException {
        System.out.println("Agora introduza a sua password. Atenção!!! A password não pode conter espaços.");
        String password = terminal.readLine();

        try {
            cStub.conectar();

            if (querRegistar) { //querRegistar = true se se quer registar, querRegistar = false se só quer o login
                cStub.registarUtilizador(nome, password);
                System.out.println("Utilizador registado com sucesso");
            }

            cStub.login(nome, password);

            System.out.println("Utilizador autenticado, bem vindo");
            System.out.println("Se quiser procurar alguma música escreva: procurarMusica ");
            System.out.println("Se quiser fazer upload de uma música escreva: upload ");
            System.out.println("Se quiser fazer o download de uma música escreva: download  idMusica");
        }
        catch (CredenciaisInvalidasException e) {
            System.out.println(e.getMessage());
            cStub.desconectar();
        }
        catch (UtilizadorJaExisteException e) {
           System.out.println(e.getMessage());
            cStub.desconectar(); //quando dá erro desliga o socket
        }
        catch (ClienteServerException e){
            e.printStackTrace();
            cStub.desconectar();  //quando dá erro desliga o socket
        }
    }

    private void logout() {
        System.out.println("Adeus");
        try{
            cStub.logout(null);
            System.exit(0);
        }
        catch (IOException e) {
            e.printStackTrace();
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

            System.out.println(e.getMessage());
        }
        catch (ClienteServerException e){
            System.out.println(e.getMessage());
        }
    }

    public void upload() throws  IOException{

        System.out.println("Insira o nome da musica sem espaços");
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
        catch (ClienteServerException e){
            e.printStackTrace();
        }
    }

    public List<Musica> procuraMusica() throws IOException {
        List<Musica> m = new ArrayList<>();

        try{
            System.out.println("Insira a etiqueta a procurar");
            String etiqueta = terminal.readLine();

            m = cStub.procuraMusica(etiqueta);

            System.out.println(m);
        }
        catch (MusicaInexistenteException e){
            System.out.println(e.getMessage());
            procuraMusica(); //Quando a música não existe volta a chamar a função para procurar algo novamente
        }
        catch (ClienteServerException e){
            e.printStackTrace();
        }

        return m;
    }

    public static void start(String ip, Integer porto) throws ClienteServerException{
        Cliente cliente = new Cliente();
        cliente.caminhoServidor(ip, porto);
        cliente.opcoes();

        try {
            cliente.executaComandos();
        } catch (IOException e) {
            System.out.println("Erro ao executar comando");
        }
    }

    private void opcoes() {
        System.out.println("Para fazer login escreva login e o seu nome: ");

        System.out.println("Para criar uma conta escreva registar e o nome do utilizador. Atenção!!! O nome não pode conter espaços. ");
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
                        autenticacao(arrayComandos[1], true); //o nome não permite espaços
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
                e.printStackTrace();
                System.out.println("Excepção desconhecida.");
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, UtilizadorNaoAutenticadoException {
       try {
           Cliente.start("127.0.0.1", 12345);
       }
       catch (ClienteServerException e){
           System.out.println("Erro na conexão");
       }
    }
}
