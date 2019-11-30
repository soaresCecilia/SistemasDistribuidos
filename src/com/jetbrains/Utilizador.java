package com.jetbrains;

import java.util.Map;

public class Utilizador {

    private String nome;
    private String password;
    private String caminhoFicheiro;


    public Utilizador(String nome, String pass) {
        this.nome = nome;
        this.password = pass;
    }


    public String getNome(){
        return this.nome;
    }



    public String getPassword() {
        return password;
    }

    public boolean equals(Object o){
        if(this == o){
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        Utilizador aux = (Utilizador) o;
        return super.equals(aux) && this.nome.equals(aux.getNome());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Nome: ");
        sb.append(this.nome+" ");


        return sb.toString();
    }

    public boolean autentica(String nome, String password) {
        return (this.nome.equals(nome) && this.password.equals(password));
    }
}
