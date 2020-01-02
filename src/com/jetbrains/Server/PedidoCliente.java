package com.jetbrains.Server;

import com.jetbrains.Exceptions.ClienteServerException;
import com.jetbrains.Exceptions.CredenciaisInvalidasException;
import com.jetbrains.Exceptions.MusicaInexistenteException;
import com.jetbrains.Exceptions.UtilizadorJaExisteException;

import java.io.IOException;
import java.net.Socket;

public abstract class PedidoCliente {
    private ServerHelper serverHelper;
    private Boolean executado;

    public PedidoCliente(ServerHelper serverHelper) {
        this.serverHelper = serverHelper;
        this.executado = false;
    }

    public abstract void executar();

    public synchronized void notificaEspera() {
        this.executado = true;
        notify();
    }

    public synchronized void espera() {
        while (!executado) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    public ServerHelper getServerHelper(){
        return this.serverHelper;
    }
}
