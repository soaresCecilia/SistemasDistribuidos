package com.jetbrains.Server.Dados;

public class Utilizador {

    private String nome;
    private String password;
    private boolean activo = false;
    private String caminhoFicheiro; //uploads

    /*
     * Metodo construtor da Classe Utilizador
     */
    public Utilizador(String nome, String pass) {
        this.nome = nome;
        this.password = pass;
        this.activo = false;
        this.caminhoFicheiro = null;
    }

    /*
     * get nome de um Utilizador
     */
    public String getNome(){
        return this.nome;
    }

    /*
     * get password de um Utilizador
     */
    public String getPassword() {
        return password;
    }

    /*
     * metodo equals
     */
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

    /*
     * Metodo que transforma um Utilizador numa String
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Nome: ");
        sb.append(this.nome+" ");
        sb.append("Estado: ");
        sb.append(this.activo + " ");


        return sb.toString();
    }

    /*
     * Metodo que retorna True, caso o nome e a password introduzida estão de acordo com um Utilizador já registado no Sistema
     */
    public boolean autentica(String nome, String password) {
        return (this.nome.equals(nome) && this.password.equals(password));
    }

    /*
     * get do estado em que o Utilizador se encontra no Sistema
     */
    public boolean getActivo() {
        return this.activo;
    }

    /*
     * set's do estado do Utilizador no Sistema
     */
    public void setActivo() {
        this.activo = true;
    }

    public void setInactivo() {
        this.activo = false;
    }
}
