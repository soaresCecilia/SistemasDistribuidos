package com.jetbrains.Client;

import com.jetbrains.Exceptions.*;
import com.jetbrains.Server.Dados.Musica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;


public class Cliente {
    private ClienteSTUB cStub;
    private BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));
    private final Logger logger = Logger.getLogger("Cliente");



    private void caminhoServidor(String ip, Integer port) {
        cStub = new ClienteSTUB("127.0.0.1", 12346);
    }

    private void autenticacao(String nome, boolean querRegistar) throws IOException {
        System.out.println("Agora introduza a sua password. Atenção!!! A password não pode conter espaços.");

        String password = terminal.readLine();

        try {
            cStub.conectar();

            if (querRegistar) { //querRegistar = true se se quer registar, querRegistar = false se só quer o login
                cStub.registarUtilizador(nome, password);
                logger.info("Utilizador registado com sucesso");
            }

            cStub.login(nome, password);

            System.out.println("Utilizador autenticado, bem vindo");
            System.out.println("Se quiser procurar alguma música escreva: procurarMusica ");
            System.out.println("Se quiser fazer upload de uma música escreva: upload ");
            System.out.println("Se quiser fazer o download de uma música escreva: download  idMusica");
        }
        catch (CredenciaisInvalidasException e) {
            logger.warning(e.getMessage());
            cStub.desconectar();
        }
        catch (UtilizadorJaExisteException e) {
            System.out.println(e.getMessage());
            cStub.desconectar(); //quando dá erro desliga o socket
        }
        catch (ClienteServerException e){
            System.out.println(e.getMessage());
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
            System.out.println(e.getMessage());
        }
    }

    public void download(int id) {
        try{
            cStub.download(id);
            System.out.println("Download concluido com sucesso");
        }
        catch (MusicaInexistenteException e){
            System.out.println(e.getMessage());
        }
        catch (UtilizadorNaoAutenticadoException e){
            System.out.println(e.getMessage());
        }
        catch (ClienteServerException e){
            System.out.println(e.getMessage());
        }
    }

    public void upload() throws  IOException{

        System.out.println("Insira o nome da musica. Atenção!!! O nome não pode conter espaços, utilize underscore");
        String nome = terminal.readLine();

        System.out.println("Insira o interprete da musica. Atenção!!! O interprete não pode conter espaços, utilize underscore");
        String interprete = terminal.readLine();

        System.out.println("Insira o ano da musica. Atenção!!! O ano não pode conter espaços, utilize underscore");
        String ano = terminal.readLine();

        System.out.println("Insira o género musical. Atenção!!! O género não pode conter espaços, utilize underscore");
        String genero = terminal.readLine();

        System.out.println("Insira o caminho para o ficheiro a fazer upload. Atenção!!! O caminho não pode conter espaços, utilize underscore");
        String caminho = terminal.readLine();

        try{
            cStub.upload(caminho, nome, interprete, ano, genero);
            System.out.println("Upload concluido com sucesso");
        }
        catch (UtilizadorNaoAutenticadoException e){
            logger.warning(e.getMessage());
        }
        catch (ClienteServerException e){
            logger.warning(e.getMessage());
        }
    }

    public List<Musica> procuraMusica() throws IOException {
        List<Musica> m = new ArrayList<>();

        try{
            System.out.println("Insira a etiqueta a procurar.");
            String etiqueta = terminal.readLine();

            m = cStub.procuraMusica(etiqueta);

           System.out.println("Lista das músicas: " + m);
        }
        catch (MusicaInexistenteException e){
            logger.warning(e.getMessage());
        }
        catch (ClienteServerException e){
            logger.warning(e.getMessage());
        }
        catch (UtilizadorNaoAutenticadoException e) {
            logger.warning(e.getMessage());
        }

        return m;
    }

    public static void start(String ip, Integer porto) {
        Cliente cliente = new Cliente();
        cliente.caminhoServidor(ip, porto);
        cliente.opcoes();

        try {
            cliente.executaComandos();
        } catch (IOException e) {
            System.out.println(e.getMessage()); //TODO: por logger
        }
    }

    private void opcoes() {
        System.out.println("Para fazer login escreva login e o seu nome: ");

        System.out.println("Para criar uma conta escreva registar e o nome do utilizador. Atenção!!! O nome não pode conter espaços.");
    }


    private void executaComandos() throws IOException {
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
                        System.out.println("Não foi possível efectuar a operação. Volte a tentar");
                }

            } catch (Exception e) {
                logger.warning(e.getMessage());
                logger.warning("Excepção desconhecida.");
            }
        }
    }

    public static void main(String[] args) throws Exception{
       try {
           Cliente.start("127.0.0.1", 12345);
       }
       catch (Exception e){
           System.out.println("Erro na conexão" + e.getMessage());
       }
    }
}
