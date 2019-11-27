package com.jetbrains;

import java.io.*;
import java.net.Socket;

public class ClienteSTUB {
    // aqui ficam os vários protocolos de comunicação com o servidor

    public String procurarUtilizador(String mensagem) throws IOException {

        String address = "127.0.0.1";
        short port = 12345;


        //criar socket (A maquina em que está o serviço, em que porto me devo conectar)
        Socket socket = new Socket(address, port);



        //escreve noo canal de ligacao cliente cervidor
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        //le do canal de ligação cliente servidor
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));



        out.println(mensagem);
        out.flush();


        String rsp = in.readLine();






        socket.shutdownOutput();
        socket.shutdownInput();
        socket.close();

        return rsp;

    }

}
