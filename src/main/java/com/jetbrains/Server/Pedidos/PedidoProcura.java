package com.jetbrains.Server.Pedidos;

import com.jetbrains.Server.ServerHelper;

public class PedidoProcura extends PedidoCliente {
    private String etiqueta;


    public PedidoProcura(ServerHelper s, String etiqueta) {
        super(s);
        this.etiqueta = etiqueta;
    }


    public String getEtiqueta() {
        return etiqueta;
    }

    public void setEtiqueta(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public boolean equals(Object o) {
        if(this == o){
            return true;
        }
        if (o == null || o.getClass() != this.getClass()) {
            return false;
        }
        PedidoProcura aux = (PedidoProcura) o;
        return  (this.etiqueta.equals(aux.getEtiqueta()));
    }

    @Override
    public void executar() {
        ServerHelper s = getServerHelper();

        s.procuraMusica(getEtiqueta());

        notificaEspera();
    }
}
