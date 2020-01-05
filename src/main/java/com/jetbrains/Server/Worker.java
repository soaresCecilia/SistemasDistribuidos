package com.jetbrains.Server;

import com.jetbrains.Server.Dados.Repositorio;
import com.jetbrains.Server.Pedidos.*;
import org.apache.log4j.Logger;

import java.io.*;
import java.net.Socket;

public class Worker implements Runnable {

    private Socket socket;
    private ServerHelper serverhelper;
    private ThreadPool threadPool;
    private String nome;

    private final Logger logger = Logger.getLogger("Worker");

    public Worker(Socket clienteSock, Repositorio repositorio, ThreadPool threadPool) throws IOException{
        this.socket = clienteSock;
        this.serverhelper = new ServerHelper(socket, repositorio);
        this.threadPool = threadPool;
    }

    /*
    * Metodo que faz o tratamento do pedido proveniente do Cliente e o reencaminha para a Thread Pool, para posteriormente ser
    * tratado
    */
    public void responde(String s) throws InterruptedException {
        String[] comandos = s.split(" ");

        switch (comandos[0]) {

            case "login":
                this.nome = comandos[1];
                PedidoLogin pedidoLogin = new PedidoLogin(serverhelper, comandos[1], comandos[2]);

                logger.info("Adiciona pedido " + pedidoLogin);
                threadPool.adicionaTarefa(pedidoLogin);

                logger.info("Espera processar pedido " + pedidoLogin);
                pedidoLogin.espera();

                logger.info("Pedido processado " + pedidoLogin);
                break;

            case "logout":
                PedidoLogout pedidoLogout = new PedidoLogout(serverhelper, this.nome);
                threadPool.adicionaTarefa(pedidoLogout);
                pedidoLogout.espera();
                break;

            case "registar":
                this.nome = comandos[1];
                PedidoRegistar pedidoRegistar = new PedidoRegistar(serverhelper, comandos[1], comandos[2]);
                threadPool.adicionaTarefa(pedidoRegistar);
                pedidoRegistar.espera();
                break;

            case "upload":
                PedidoUpload pedidoUpload = new PedidoUpload(serverhelper, comandos[1], comandos[2], comandos[3], comandos[4], comandos[5]);
                threadPool.adicionaTarefa(pedidoUpload);
                pedidoUpload.espera();
                break;

            case "download":
                int nrm = Integer.parseInt(comandos[1]);

                PedidoDownload pedidoDownload = new PedidoDownload(serverhelper, nrm);

                logger.info("Adiciona pedido " + pedidoDownload);
                threadPool.adicionaTarefa(pedidoDownload);

                logger.info("Espera processar pedido " + pedidoDownload);
                pedidoDownload.espera();

                logger.info("Pedido processado" + pedidoDownload);
                break;

            case "procura":
                PedidoProcura pedidoProcura = new PedidoProcura(serverhelper, comandos[1]);
                threadPool.adicionaTarefa(pedidoProcura);
                pedidoProcura.espera();
                break;

            default:
                logger.error("Opcao invalida");
        }
    }


    /*
    * Metodo que lê os pedidos provenientes do Cliente
    */
    public void run(){
        try {
            if (socket == null) {
                System.out.println("clSock null");
                return;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));

            //passa o que li do canal para uma string
            String inComing = in.readLine();

            logger.info("Pedido recebido " + inComing);

            while(inComing != null) {
                responde(inComing);

                inComing = in.readLine();

                logger.info("Pedido recebido " + inComing);
            }
            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
        }
        catch (IOException | InterruptedException e){
            logger.error("Erro ao processar pediodo: ", e);
        }
    }

}


