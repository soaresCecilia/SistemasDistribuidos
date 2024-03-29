package com.jetbrains.Server.Pedidos;

import com.jetbrains.Server.ServerHelper;

public class PedidoLogout extends PedidoCliente {
    private String nome;

    public PedidoLogout(ServerHelper s, String nome) {
        super(s);
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        PedidoLogout aux = (PedidoLogout) o;
        return  (this.nome.equals(aux.getNome()));
    }

    @Override
    public void executar() {
        ServerHelper s = getServerHelper();

        s.logout(getNome());

        notificaEspera();
    }
}
