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


    public ThreadPool() {
        this.filaTarefas = new LinkedTransferQueue();
        this.threadsPool = new PoolWorker[MAX_THREADS];

        for (int i = 0; i < MAX_THREADS; i++) {
            this.threadsPool[i] = new PoolWorker("Worker Pool" + i);
            this.threadsPool[i].start();
        }
    }

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
