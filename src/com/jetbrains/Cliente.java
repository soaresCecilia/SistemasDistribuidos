package com.jetbrains;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {

    public static void main(String[] args) throws IOException {

        ClienteSTUB clienteSTUB = new ClienteSTUB();

        BufferedReader inputCliente = new BufferedReader(new InputStreamReader(System.in));

        String mensagem = inputCliente.readLine();

        Boolean rsp= clienteSTUB.procurarUtilizador(mensagem);

        System.out.println("O servidor encontrou o utilizador? "+rsp);
    }
}
