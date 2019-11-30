package com.jetbrains;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Cliente {
//aqui vamos fazer as verificações se o input do cliente é bem feito, e depois aqui trata do
    // display dos resultados dados pelo servidor
    public static void main(String[] args) throws IOException {

        ClienteSTUB clienteSTUB = new ClienteSTUB("127.0.0.1",12345);

        BufferedReader inputCliente = new BufferedReader(new InputStreamReader(System.in));




        
    }
}
