package com.jetbrains.Exceptions;

public class UtilizadorNaoAutenticadoException extends Exception {
    public UtilizadorNaoAutenticadoException(){
        super();
    }
    public UtilizadorNaoAutenticadoException(String m){
        super(m);
    }
}
