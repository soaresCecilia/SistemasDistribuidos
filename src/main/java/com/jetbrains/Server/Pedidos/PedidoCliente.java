package com.jetbrains.Server.Pedidos;

import com.jetbrains.Exceptions.ClienteServerException;
import com.jetbrains.Exceptions.CredenciaisInvalidasException;
import com.jetbrains.Exceptions.MusicaInexistenteException;
import com.jetbrains.Exceptions.UtilizadorJaExisteException;
import com.jetbrains.Server.ServerHelper;

import java.io.IOException;
import java.net.Socket;

public abstract class PedidoCliente {
    private ServerHelper serverHelper;
    private Boolean executado;

    /*
    Construtor da classe.
     */
    public PedidoCliente(ServerHelper serverHelper) {
        this.serverHelper = serverHelper;
        this.executado = false;
    }

    /*
    Método abstracto que será implementado pelas subclasses.
     */
    public abstract void executar();

    /*
    Método que avisa as threads de que o seu pedido foi executado.
     */
    public synchronized void notificaEspera() {
        this.executado = true;
        notify();
    }

    /*
    Método que obriga a que cada pedido tenha de ser satisfeito até que possa ser enviado
    um novo do mesmo cliente.
     */
    public synchronized void espera() {
        while (!executado) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
    }

    /*
    Método que retorna um objecto da classe ServerHelper.
     */
    public ServerHelper getServerHelper(){
        return this.serverHelper;
    }
}
