package com.jetbrains.Client;

import com.jetbrains.Exceptions.*;
import com.jetbrains.Server.Dados.Musica;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Cliente {
    ClienteSTUB cStub;
    BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));

    private static final int TAM_CAMPO_LOGIN = 3;
    private static final int TAM_CAMPO_REGISTO = 3;
    private static final int TAM_CAMPO_DOWNLOAD = 2;
    private static final int TAM_CAMPO_UPLOAD = 6;
    private static final int TAM_CAMPO_PROCURA_MUSICA = 2;


    private void caminhoServidor(String ip, Integer port) {
        cStub = new ClienteSTUB("127.0.0.1", 12346);
    }

    private void autenticacao(String nome,String password, boolean querRegistar) throws IOException, UtilizadorNaoAutenticadoException, CredenciaisInvalidasException {

        try {
            cStub.conectar();

            if (querRegistar) { //querRegistar = true se se quer registar, querRegistar = false se só quer o login
                cStub.registarUtilizador(nome, password);
                System.out.println("Utilizador registado com sucesso");
            }

            cStub.login(nome, password);
            bemVindo();
            opcoesLoggedIn();

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

    public void upload( String nome, String interprete, String ano, String genero, String caminho) throws  IOException{

        try{
            cStub.upload(caminho, nome, interprete, ano, genero);
            System.out.println("Upload concluido com sucesso");
        }
        catch (UtilizadorNaoAutenticadoException e){
            System.out.println(e.getMessage());
        }
        catch (ClienteServerException e){
            System.out.println(e.getMessage());
        }
    }

    public List<Musica> procuraMusica(String etiqueta) throws IOException {
        List<Musica> m = new ArrayList<>();

        try{

            m = cStub.procuraMusica(etiqueta);

            System.out.println(m);
        }
        catch (MusicaInexistenteException e){
            System.out.println(e.getMessage());
        }
        catch (ClienteServerException e){
            System.out.println(e.getMessage());
        }
        catch (UtilizadorNaoAutenticadoException e){
            System.out.println(e.getMessage());
        }

        return m;
    }

    public static void start(String ip, Integer porto) throws ClienteServerException{
        Cliente cliente = new Cliente();
        cliente.caminhoServidor(ip, porto);
        cliente.opcoesNotLoggedIn();

        try {
            cliente.executaComandos();
        } catch (IOException e) {
            System.out.println("Erro ao executar comando");
        }
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
                        if (arrayComandos.length > TAM_CAMPO_LOGIN)
                            throw new Exception();
                        else{
                        autenticacao(arrayComandos[1], arrayComandos[2], false);
                        }
                        break;
                    case "registar":
                        if (arrayComandos.length > TAM_CAMPO_REGISTO)
                            throw new Exception();
                        else{
                        autenticacao(arrayComandos[1], arrayComandos[2],true);
                        }
                        break;
                    case "logout":
                        logout();
                        break;

                    case "download":

                        if (arrayComandos.length > TAM_CAMPO_DOWNLOAD)
                            throw new Exception();
                        else{

                            int id = Integer.parseInt(arrayComandos[1]);
                            download(id);
                        }
                        break;

                    case "upload":
                        if (arrayComandos.length > TAM_CAMPO_UPLOAD)
                            throw new Exception();
                        else{
                            upload(arrayComandos[1],arrayComandos[2],arrayComandos[3],arrayComandos[4],arrayComandos[5]);
                        }
                        break;

                    case "procurarMusica":
                        if (arrayComandos.length > TAM_CAMPO_PROCURA_MUSICA)
                            throw new Exception();
                        else{
                            procuraMusica(arrayComandos[1]);}
                        break;

                    case "sair":
                        System.exit(0);

                    default:
                        System.out.println("Não foi possível efectuar a operação. Volte a tentar");
                }

            } catch (UtilizadorNaoAutenticadoException e) {
                System.out.println(e.getMessage());
            }
            catch (Exception e) {
                System.out.println("Campos da accao indevidamente preenchidos.");
                opcoesLoggedIn();
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


    private void opcoesNotLoggedIn() {
        System.out.println("........................................................................................................");
        System.out.println("........................................................................................................");
        System.out.println("........ATENCAO!!! O nome não pode conter espaços.......................................................");
        System.out.println("........................................................................................................");
        System.out.println("........................................................................................................");
        System.out.println("Fazer login escreva: login seu_nome palavra_pass........................................................");
        System.out.println("........................................................................................................");
        System.out.println("Criar uma conta escreva: registar nome_utilizador pallavra_pass.........................................");
        System.out.println("........................................................................................................");
        System.out.println("........................................................................................................");

    }
    private void bemVindo(){
        System.out.println("........................................................................................................");
        System.out.println("........................................................................................................");
        System.out.println("Utilizador autenticado, bem vindo.......................................................................");
        System.out.println("........................................................................................................");
    }
    private void opcoesLoggedIn() {
        System.out.println("........................................................................................................");
        System.out.println("........................................................................................................");
        System.out.println("........ATENCAO!!! Os nomes não podem conter espaços, utilize por exemplo:  _ &.........................");
        System.out.println("........................................................................................................");
        System.out.println("........................................................................................................");
        System.out.println("Procurar música escreva: procurarMusica 'almofada_maluca'...............................................");
        System.out.println("........................................................................................................");
        System.out.println("Fazer upload de uma música escreva: upload titulo_da_musica interprete ano genero caminho_para_a_musica.");
        System.out.println("........................................................................................................");
        System.out.println("Fazer o download de uma música escreva: download  idMusica..............................................");
        System.out.println("........................................................................................................");
        System.out.println("........................................................................................................");
    }
}
