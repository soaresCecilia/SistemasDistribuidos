package com.jetbrains.Server;


import java.util.PriorityQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class ThreadPool {
        private final int MAX_THREADS = 10;
        private final PoolWorker[] threads;
        private final LinkedBlockingQueue filaTarefas;



    public ThreadPool() {
            this.filaTarefas = new LinkedBlockingQueue(20);
            threads = new PoolWorker[MAX_THREADS];

            for (int i = 0; i < MAX_THREADS; i++) {
                threads[i] = new PoolWorker();
                threads[i].start();
            }
        }

        public void adicionaTarefa(PedidoCliente tarefa) throws InterruptedException {
            synchronized (filaTarefas) {
                filaTarefas.add(tarefa);
                filaTarefas.notify();
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
                            } catch (InterruptedException e) {
                                System.out.println("Ocorreu um erro enquanto a fila das tarefas estava vazia: " + e.getMessage());
                            }
                        }
                        tarefa = (PedidoCliente) filaTarefas.poll();
                    }
                    tarefa.executar();
                }
            }
        }
}
