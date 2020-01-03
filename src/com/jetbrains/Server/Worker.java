package com.jetbrains.Server;

import com.jetbrains.Server.Dados.Repositorio;
import com.jetbrains.Server.Pedidos.*;


import java.io.*;
import java.net.Socket;
import java.util.logging.Logger;


public class Worker implements Runnable {

    private Socket clSock;
    private ServerHelper serverhelper;
    private ThreadPool threadPool;
    private String nome;

    private final Logger logger = Logger.getLogger("Worker");

    public Worker(Socket clientesock, Repositorio repositorio, ThreadPool threadPool) throws IOException{
        this.clSock = clientesock;
        this.serverhelper = new ServerHelper(clientesock, repositorio);
        this.threadPool = threadPool;
    }

    public void responde(String s) throws InterruptedException {
        String[] comandos = s.split(" ");

        String threadNome = Thread.currentThread().getName();
        String msg = null;

        switch (comandos[0]) {

            case "login":
                this.nome = comandos[1];
                PedidoLogin pedidoLogin = new PedidoLogin(serverhelper, comandos[1], comandos[2]);

                msg = String.format("%s : Adiciona pedido %s", threadNome, pedidoLogin);
                logger.info(msg);
                threadPool.adicionaTarefa(pedidoLogin);

                msg = String.format("%s : Espera processar pedido %s", threadNome, pedidoLogin);
                logger.info(msg);
                pedidoLogin.espera();

                msg = String.format("%s : Pedido processado %s", threadNome, pedidoLogin);
                logger.info(msg);
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
                msg = String.format("%s : Adiciona pedido %s", threadNome, pedidoDownload);
                logger.info(msg);

                threadPool.adicionaTarefa(pedidoDownload);

                msg = String.format("%s : Espera processar pedido %s", threadNome, pedidoDownload);
                logger.info(msg);

                pedidoDownload.espera();

                msg = String.format("%s : Pedido processado %s ", threadNome, pedidoDownload);
                logger.info(msg);
                break;

            case "procura":
                PedidoProcura pedidoProcura = new PedidoProcura(serverhelper, comandos[1]);
                threadPool.adicionaTarefa(pedidoProcura);
                pedidoProcura.espera();
                break;

            default:
                logger.warning("Opcao invalida");
        }
    }


    //O worker so fala com o stub quando dá erro
    public void run(){
        try {
            if (clSock == null) {
                System.out.println("clSock null");
                return;
            }

            BufferedReader in = new BufferedReader(new InputStreamReader(this.clSock.getInputStream()));

            String threadNome = Thread.currentThread().getName();
            String msg = null;

            //passa o que li do canal para uma string
            String inComing = in.readLine();

            msg = String.format("%s : Pedido recebido %s", threadNome, inComing);
            logger.info(msg);

            while(inComing != null) {
                responde(inComing);

                inComing = in.readLine();

                msg = String.format("%s : Pedido recebido %s", threadNome, inComing);
                logger.info(msg);
            }
            clSock.shutdownOutput();
            clSock.shutdownInput();
            clSock.close();
        }
        catch (IOException | InterruptedException e){
            logger.warning(e.getMessage());
        }
    }

}


