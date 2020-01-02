package com.jetbrains.Server.Pedidos;

import com.jetbrains.Server.ServerHelper;

public class PedidoRegistar extends PedidoCliente {
    private String nome;
    private String password;

    public PedidoRegistar(ServerHelper s, String nome, String password) {
        super(s);
        this.nome = nome;
        this.password = password;
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

        s.registarUtilizador(getNome(), getPassword());

        notificaEspera();
    }
}
