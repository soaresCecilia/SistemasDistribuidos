package com.jetbrains;

public class Utilizador {
    private int identificador;
    private String nome;
    private String email;
    private String password;

    public Utilizador(int id, String nome, String email, String pass) {
        this.identificador = id;
        this.nome = nome;
        this.email = email;
        this.password = pass;
    }
}
