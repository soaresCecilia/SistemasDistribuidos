package com.jetbrains;

public class CredenciaisInvalidasException extends Exception {
    public CredenciaisInvalidasException(){
        super();
    }
    public CredenciaisInvalidasException(String m){
        super(m);
    }
}
