package com.jetbrains.Server.Dados;

public class Utilizador {

    private String nome;
    private String password;
    private boolean activo = false;
    private String caminhoFicheiro; //uploads


    public Utilizador(String nome, String pass) {
        this.nome = nome;
        this.password = pass;
        this.activo = false;
        this.caminhoFicheiro = null;
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
        sb.append("Estado: ");
        sb.append(this.activo + " ");


        return sb.toString();
    }

    public boolean autentica(String nome, String password) {
        return (this.nome.equals(nome) && this.password.equals(password));
    }


    public boolean getActivo() {
        return this.activo;
    }


    public void setActivo() {
        this.activo = true;
    }

    public void setInactivo() {
        this.activo = false;
    }
}
