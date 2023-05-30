package com.ra_slr.model;

import lombok.Data;

@Data
public class Pila {
    final int MAX = 5000;
    SimbGram[] elems;
    int tope;

    public Pila(){
        elems = new SimbGram[MAX];
        for (int i = 0; i < elems.length; i++){
            elems[i] = new SimbGram("");
            tope = 0;
        }
    }

    public boolean empty(){
        return tope == 0;
    }

    public boolean full(){
        return tope == elems.length;
    }

    public void push(SimbGram simbGram){
        elems[tope++] = simbGram;
    }

    public int lenght(){
        return tope;
    }

    public SimbGram pop(){
        return elems[--tope];
    }

    public void inicia(){
        tope = 0;
    }

    public SimbGram tope(){
        return elems[tope - 1];
    }
}
