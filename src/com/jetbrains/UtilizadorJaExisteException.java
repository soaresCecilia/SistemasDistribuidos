package com.jetbrains;

public class UtilizadorJaExisteException extends Exception{
    public UtilizadorJaExisteException(){
        super();
    }
    public UtilizadorJaExisteException(String m){
        super(m);
    }
}
