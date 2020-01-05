package com.jetbrains.Server;

import com.jetbrains.Server.Pedidos.PedidoCliente;
import com.jetbrains.Server.Pedidos.PedidoDownload;

import java.util.Queue;
import java.util.concurrent.LinkedTransferQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool {
    private final int MAX_THREADS = 10;
    private final Thread[] threadsPool;
    private final Queue filaTarefas;
    private static int nDownloads = 0;
    private static final int MAX_TAM_FILA = 2;
    private static final int MAX_DOWNLOADS = 1;
    private ReentrantLock lockFila = new ReentrantLock();
    private ReentrantLock lockDownload = new ReentrantLock();
    private Condition esperaExecutarTarefa = lockFila.newCondition();
    private Condition esperaAdicionarTarefaFila = lockFila.newCondition();
    private Condition esperaExecutarDownload = lockDownload.newCondition();


    /*
    Construtor da classe
     */
    public ThreadPool() {
        this.filaTarefas = new LinkedTransferQueue();
        this.threadsPool = new PoolWorker[MAX_THREADS];

        for (int i = 0; i < MAX_THREADS; i++) {
            this.threadsPool[i] = new PoolWorker("Worker Pool " + i);
            this.threadsPool[i].start();
        }
    }

    /*
    Método que adiciona as tarefas à fila de tarefas enquanto esta não estiver cheia.
     */
    public void adicionaTarefa(PedidoCliente tarefa) {
        lockFila.lock();

        boolean inseriu = false;

        while(!inseriu) {
            if (filaTarefas.size() < MAX_TAM_FILA) {
                filaTarefas.add(tarefa);
                esperaExecutarTarefa.signal();
                inseriu = true;
            }
            else {
                try {
                    esperaAdicionarTarefaFila.await();
                } catch (InterruptedException e) { }
            }
        }

        lockFila.unlock();
    }


    /*
    Método que retira uma tarefa da fila de tarefas, desde que haja tarefas na fila.
     */
    public PedidoCliente seleccionaTarefaExecutar() {

        this.lockFila.lock();

        while (filaTarefas.isEmpty()) {
            try {
                esperaExecutarTarefa.await();
            } catch (InterruptedException e) {
            }
        }

        PedidoCliente tarefa = (PedidoCliente) filaTarefas.poll();

        esperaAdicionarTarefaFila.signal();

        this.lockFila.unlock();

        return tarefa;
    }


    /*
    Método que executa a tarefa. Para tal, verifica se a tarefa é um pedido de download e, em caso afirmativo,
    verifica se o mesmo está dentro do limite de downloads que podem ocorrer em simultâneo. Se sim executa-o,
    senão espera até obter um sinal de que pode executar o seu download.
     */
    public void executaTarefa(PedidoCliente tarefa) {

        boolean executaPedido = false;

        while (!executaPedido) {
            if (tarefa instanceof PedidoDownload) {
                lockDownload.lock();

                if (nDownloads < MAX_DOWNLOADS) {
                  nDownloads++;
                  executaPedido = true;
                }
                else {
                     try{
                         esperaExecutarDownload.await();
                     } catch (InterruptedException e)  {}
                }

                lockDownload.unlock();

            } else {
                executaPedido = true;
            }
        }

        tarefa.executar();

        if (tarefa instanceof PedidoDownload) {
            lockDownload.lock();
            nDownloads--;
            esperaExecutarDownload.signal();
            lockDownload.unlock();
        }
    }

    /*
    Classe privada que extends a classe Thread para poder executar estes métodos com a nossa threadsPool.
     */
    private class PoolWorker extends Thread {
        public PoolWorker(String name) {
            super(name);
        }

        public void run() {
            while (true) {
                PedidoCliente tarefa = seleccionaTarefaExecutar();
                executaTarefa(tarefa);
            }
        }
    }
}
