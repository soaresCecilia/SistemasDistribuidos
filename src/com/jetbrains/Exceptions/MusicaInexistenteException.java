package com.jetbrains.Exceptions;

public class MusicaInexistenteException extends Exception{
    public MusicaInexistenteException(){
        super();
    }
    public MusicaInexistenteException(String m){
        super(m);
    }
}
