package com.jetbrains;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

    public static void main(String[] args) throws IOException {
        String address = "127.0.0.1";
        short port = 12345;



        //criar socket (A maquina em que está o serviço, em que porto me devo conectar)
        Socket socket = new Socket(address, port);

        //le no terminal o nosso input
        BufferedReader inserCliente = new BufferedReader(new InputStreamReader(System.in));


        //le do canal de ligação cliente servidor
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        //escreve noo canal de ligacao cliente cervidor
        PrintWriter out = new PrintWriter(socket.getOutputStream());

        String inComing = inserCliente.readLine();

        String[] comandos = inComing.split(" ");

        while(!inComing.equals("quit")){
            //envio o que escrevi para o canal.
            out.println(inComing);
            out.flush();


            switch (comandos[0]){

                case "pesquisar titulo":

                    //leio a resposta do canal ex: nr de movimentos ()  movimento1 movimento2 (que o servidor me enviou em strings diferentes)
                    String doCanal =in.readLine();
                    int nMatchs = Integer.parseInt(doCanal);

                    for(int i=0; i<nMatchs;i++){
                        //leio a resposta seguinte à resposta que me indica o numero de movimentos que preciso de printar do canal
                        doCanal =in.readLine();
                        //printo a resposta do canal
                        System.out.println(doCanal);
                    }
                    break;

                case "pesquisar interprete":
                        System.out.println("incompleto");

                default:
                    //para o caso de só ler uma String

                    //leio a resposta do canal
                    doCanal =in.readLine();
                    //printo a resposta do canal
                    System.out.println(doCanal);
            }


            inComing = inserCliente.readLine();

            comandos = inComing.split(" ");
        }

        socket.shutdownOutput();
        socket.shutdownInput();
        socket.close();


    }
}
