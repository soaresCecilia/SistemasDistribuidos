package com.jetbrains.Server.Pedidos;

import com.jetbrains.Server.ServerHelper;

public class PedidoDownload extends PedidoCliente{
    private int idMusica;


    public PedidoDownload(ServerHelper s, int idMusica) {
        super(s);
        this.idMusica = idMusica;
    }


    public int getIdMusica() {
        return idMusica;
    }

    public void setIdMusica(int idMusica) {
        this.idMusica = idMusica;
    }


    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        PedidoDownload aux = (PedidoDownload) o;
        return  (this.idMusica == aux.getIdMusica());
    }

    public void executar() {
        ServerHelper s = getServerHelper();

        s.download(this.getIdMusica());

        notificaEspera();
    }
}
