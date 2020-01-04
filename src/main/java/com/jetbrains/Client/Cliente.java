package com.jetbrains.Client;

import com.jetbrains.Exceptions.*;
import com.jetbrains.Server.Dados.Musica;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;
import org.apache.log4j.RollingFileAppender;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private ClienteSTUB cStub;
    private BufferedReader terminal = new BufferedReader(new InputStreamReader(System.in));
    private Logger logger = Logger.getLogger("Cliente");
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
                logger.info("Utilizador registado com sucesso");
            }

            cStub.login(nome, password);
            bemVindo();
            opcoesLoggedIn();

        }
        catch (CredenciaisInvalidasException e) {
            logger.error(e.getMessage());
            cStub.desconectar();
        }
        catch (UtilizadorJaExisteException e) {
            logger.error(e.getMessage());
            cStub.desconectar(); //quando dá erro desliga o socket
        }
        catch (ClienteServerException e){
            logger.error(e.getMessage());
            cStub.desconectar();  //quando dá erro desliga o socket
        }
    }

    private void logout() {
        adeus();
        try{
            cStub.logout(null);
            System.exit(0);
        }
        catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    public void download(int id) {
        try{
            cStub.download(id);
            System.out.println("Download concluido com sucesso");
        }
        catch (MusicaInexistenteException e){
            logger.error(e.getMessage());
        }
        catch (UtilizadorNaoAutenticadoException e){
            logger.error(e.getMessage());
        }
        catch (ClienteServerException e){
            logger.error(e.getMessage());
        }
    }

    public void upload( String nome, String interprete, String ano, String genero, String caminho) throws  IOException{

        try{
            cStub.upload(caminho, nome, interprete, ano, genero);
            System.out.println("Upload concluido com sucesso");
        }
        catch (UtilizadorNaoAutenticadoException e){
            logger.error(e.getMessage());
        }
        catch (ClienteServerException e){
            logger.error(e.getMessage());
        }
    }

    public List<Musica> procuraMusica(String etiqueta) throws IOException {
        List<Musica> m = new ArrayList<>();

        try{

            m = cStub.procuraMusica(etiqueta);

            for(Musica musica : m){

                System.out.println(musica.toString());}
            }
        catch (MusicaInexistenteException e){
            logger.error(e.getMessage());
        }
        catch (ClienteServerException e){
            logger.error(e.getMessage());
        }
        catch (UtilizadorNaoAutenticadoException e) {
            logger.error(e.getMessage());
        }

        return m;
    }

    public static void start(String ip, Integer porto) {
        Cliente cliente = new Cliente();
        cliente.caminhoServidor(ip, porto);
        cliente.opcoesNotLoggedIn();

        try {
            cliente.executaComandos();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
                        if (arrayComandos.length != TAM_CAMPO_LOGIN){
                            opcoesNotLoggedIn();
                            throw new CamposInvalidosException();
                        }
                        else {
                            autenticacao(arrayComandos[1], arrayComandos[2], false);
                        }
                        break;
                    case "registar":
                        if (arrayComandos.length != TAM_CAMPO_REGISTO){
                            opcoesNotLoggedIn();
                            throw new CamposInvalidosException();
                        }
                        else {
                            autenticacao(arrayComandos[1], arrayComandos[2], true);
                        }
                        break;
                    case "logout":
                        logout();
                        break;

                    case "download":

                        if (arrayComandos.length != TAM_CAMPO_DOWNLOAD){
                            opcoesLoggedIn();
                            throw new CamposInvalidosException();
                        }
                        else {

                            int id = Integer.parseInt(arrayComandos[1]);
                            download(id);
                        }
                        break;

                    case "upload":
                        if (arrayComandos.length != TAM_CAMPO_UPLOAD ){
                            opcoesLoggedIn();
                            throw new CamposInvalidosException();
                        }
                        else {
                            upload(arrayComandos[1], arrayComandos[2], arrayComandos[3], arrayComandos[4], arrayComandos[5]);
                        }
                        break;

                    case "procurarMusica":
                        if (arrayComandos.length != TAM_CAMPO_PROCURA_MUSICA){
                            opcoesLoggedIn();
                            throw new CamposInvalidosException();
                        }
                        else {
                            procuraMusica(arrayComandos[1]);
                        }
                        break;

                    case "sair":
                        System.exit(0);

                    default:
                        System.out.println("Não foi possível efectuar a operação. Volte a tentar");
                }
            }
            catch (UtilizadorNaoAutenticadoException e){
                logger.error("Utilizador não esta autenticado.");
            }
            catch (CredenciaisInvalidasException e){
                logger.error("Credenciais inválidas");
            }
            catch (CamposInvalidosException e) {
                logger.error("Campos da accao indevidamente preenchidos.");
            }
        }
    }

    private static void initLogger() {
        Logger rootLogger = Logger.getRootLogger();
        PatternLayout layout = new PatternLayout("[%-5p] %t:%c - %m%n");

        try {
            RollingFileAppender fileAppender = new RollingFileAppender(layout, System.getProperty("log.file", "./cliente-ssd.log"));
            fileAppender.setImmediateFlush(true);
            fileAppender.setThreshold(Level.DEBUG);
            fileAppender.setAppend(true);
            rootLogger.addAppender(fileAppender);
        } catch (IOException e) {
            rootLogger.error("Falha ao configurar o logging.", e);
        }
    }

    public static void main(String[] args) throws Exception {
        initLogger();

        try {
            Cliente.start("127.0.0.1", 12345);
        }
        catch (Exception e){
            System.out.println("Erro na conexão" + e.getMessage());
        }
    }


    private void opcoesNotLoggedIn() {
        System.out.println(".........................................................................................................");
        System.out.println(".........................................................................................................");
        System.out.println("........ATENCAO!!! O nome não pode conter espaços........................................................");
        System.out.println(".........................................................................................................");
        System.out.println(".........................................................................................................");
        System.out.println("Fazer login, escreva: login seu_nome palavra_pass........................................................");
        System.out.println(".........................................................................................................");
        System.out.println("Criar uma conta, escreva: registar nome_utilizador pallavra_pass.........................................");
        System.out.println(".........................................................................................................");
        System.out.println(".........................................................................................................");
    }
    private void bemVindo(){
        System.out.println(".........................................................................................................");
        System.out.println(".........................................................................................................");
        System.out.println("Utilizador autenticado, bem vindo........................................................................");
        System.out.println(".........................................................................................................");
    }

    private void adeus(){
        System.out.println(".........................................................................................................");
        System.out.println(".........................................................................................................");
        System.out.println("..................................................Adeus..................................................");
        System.out.println(".........................................................................................................");
        System.out.println(".........................................................................................................");

    }

    private void opcoesLoggedIn() {
        System.out.println(".........................................................................................................");
        System.out.println(".........................................................................................................");
        System.out.println("........ATENCAO!!! Os nomes não podem conter espaços, utilize por exemplo:  _ &..........................");
        System.out.println(".........................................................................................................");
        System.out.println(".........................................................................................................");
        System.out.println("Procurar música, escreva: procurarMusica 'almofada_maluca'...............................................");
        System.out.println(".........................................................................................................");
        System.out.println("Fazer upload de uma música, escreva: upload titulo_da_musica interprete ano genero caminho_para_a_musica.");
        System.out.println(".........................................................................................................");
        System.out.println("Fazer o download de uma música, escreva: download  idMusica..............................................");
        System.out.println(".........................................................................................................");
        System.out.println("Fazer logout, escreva: logout............................................................................");
        System.out.println(".........................................................................................................");
        System.out.println(".........................................................................................................");

    }
}
