package com.jetbrains.Server;

import com.jetbrains.Server.Dados.Repositorio;
import com.jetbrains.Server.Pedidos.*;


import java.io.*;
import java.net.Socket;


public class Worker implements Runnable {

    private Socket clSock;
    private ServerHelper serverhelper;
    private ThreadPool threadPool;
    private String nome;

    public Worker(Socket clientesock, Repositorio repositorio, ThreadPool threadPool) throws IOException{
        this.clSock = clientesock;
        this.serverhelper = new ServerHelper(clientesock, repositorio);
        this.threadPool = threadPool;
    }

    public void responde(String s) throws InterruptedException {
        String[] comandos = s.split(" ");

        switch (comandos[0]) {

            case "login":
                this.nome = comandos[1];
                PedidoLogin pedidoLogin = new PedidoLogin(serverhelper, comandos[1], comandos[2]);
                threadPool.adicionaTarefa(pedidoLogin);
                pedidoLogin.espera();
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
                threadPool.adicionaTarefa(pedidoDownload);
                pedidoDownload.espera();
                //System.out.println("id da musica reecebida: " + nrm);
                //System.out.println("ServerHelper funcionou!");
                break;

            case "procura":
                PedidoProcura pedidoProcura = new PedidoProcura(serverhelper, comandos[1]);
                threadPool.adicionaTarefa(pedidoProcura);
                pedidoProcura.espera();
                break;

            default:
                System.out.println("Opcao invalida"); //mandar para o cliente
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

            //passa o que li do canal para uma string
            String inComing = in.readLine();
            //System.out.println(inComing);

            //System.out.println(inComing);
            while(inComing != null) {
                responde(inComing);
                //System.out.println(inComing);
                inComing = in.readLine();
            }
            clSock.shutdownOutput();
            clSock.shutdownInput();
            clSock.close();
        }
        catch (IOException | InterruptedException e){}
    }

}


