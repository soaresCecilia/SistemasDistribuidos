package com.jetbrains.Server;


import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
    private final int MAX_THREADS = 10;
    private final PoolWorker[] threads;
    private final LinkedBlockingQueue filaTarefas;
    private static int nDownloads = 0;
    private static final int MAX_DOWNLOADS = 10;

    public ThreadPool() {
        this.filaTarefas = new LinkedBlockingQueue(20);
        this.threads = new PoolWorker[MAX_THREADS];

        for (int i = 0; i < MAX_THREADS; i++) {
            this.threads[i] = new PoolWorker();
            this.threads[i].start();
        }
    }

    public void adicionaTarefa(PedidoCliente tarefa) {
        synchronized (filaTarefas) {
            boolean inseriu = false;

            while(!inseriu) {
                if (filaTarefas.remainingCapacity() != 0) {
                    filaTarefas.add(tarefa);
                    filaTarefas.notifyAll();
                    inseriu = true;
                }
                else {
                    try {
                        filaTarefas.wait();
                    } catch (InterruptedException e) { }
                }
            }
        }
    }

    private class PoolWorker extends Thread {
        public void run() {
            PedidoCliente tarefa;

            while (true) {
                synchronized (filaTarefas) {
                    while (filaTarefas.isEmpty()) {
                        try {
                            filaTarefas.wait();
                        } catch (InterruptedException e) { }
                    }
                    tarefa = (PedidoCliente) filaTarefas.poll();
                    filaTarefas.notifyAll();
                }

                synchronized (filaTarefas) {
                    boolean executaPedido = false;
                    while (!executaPedido) {
                        if (tarefa instanceof PedidoDownload && nDownloads >= MAX_DOWNLOADS) {
                            try {
                                filaTarefas.wait();
                            } catch (InterruptedException e) { }
                        }
                        else {
                            executaPedido = true;
                            if(tarefa instanceof PedidoDownload) {
                                nDownloads++;
                            }
                        }
                    }
                }

                tarefa.executar();

                synchronized (filaTarefas) {
                    if (tarefa instanceof PedidoDownload) {
                        nDownloads--;
                        filaTarefas.notifyAll(); //ver se notifica só um
                    }
                }
            }
        }
    }
}
