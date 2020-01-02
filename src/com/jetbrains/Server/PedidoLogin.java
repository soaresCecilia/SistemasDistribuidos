package com.jetbrains.Server;

import com.jetbrains.Exceptions.ClienteServerException;
import com.jetbrains.Exceptions.CredenciaisInvalidasException;
import com.jetbrains.Exceptions.MusicaInexistenteException;

import java.io.IOException;
import java.net.Socket;

public class PedidoLogin extends PedidoCliente{
    private String nome;
    private String password;
    private Boolean executado;

    public PedidoLogin(ServerHelper s, String nome, String password) {
        super(s);
        this.nome = nome;
        this.password = password;
        this.executado = false;
    }

    public String getNome() {
        return nome;
    }

    public String getPassword() {
        return password;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        PedidoLogin aux = (PedidoLogin) o;
        return  (this.nome.equals(aux.getNome()) && this.password.equals(aux.getPassword()));
    }

    @Override
    public void executar() {
        ServerHelper s = getServerHelper();

        s.login(getNome(), getPassword());

        notificaEspera();
    }
}
