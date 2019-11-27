package com.jetbrains;

public class Utilizador {
    private int id;
    private String nome;
    private String email;
    private String password;

    public Utilizador(int id, String nome, String email, String pass) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.password = pass;
    }

    public int getId() {
        return id;
    }

    public String getNome(){
        return this.nome;
    }

    public String getEmail() {
        return email;
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
        return super.equals(aux) && this.id == aux.getId()
                && this.nome.equals(aux.getNome())
                && this.email.equals(aux.getEmail());
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("ID: ");
        sb.append(this.id+" ");
        sb.append("Nome: ");
        sb.append(this.nome+" ");
        sb.append("Email: ");
        sb.append(this.email);


        return sb.toString();
    }
}
