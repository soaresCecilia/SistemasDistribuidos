package com.jetbrains;

import java.io.*;
import java.net.Socket;

public class ClienteSTUB {

    public Boolean procurarUtilizador(String mensagem) throws IOException {

        String address = "127.0.0.1";
        short port = 12345;


        //criar socket (A maquina em que está o serviço, em que porto me devo conectar)
        Socket socket = new Socket(address, port);

        //le no terminal o nosso input
        BufferedReader inserCliente = new BufferedReader(new InputStreamReader(System.in));


        //escreve noo canal de ligacao cliente cervidor


        //le do canal de ligação cliente servidor


        RequestUtilizador rU = new RequestUtilizador(mensagem);







        socket.shutdownOutput();
        socket.shutdownInput();
        socket.close();

        return rsp;

    }

}
