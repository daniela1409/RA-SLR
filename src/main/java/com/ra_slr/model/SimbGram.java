package com.ra_slr.model;

import lombok.Data;

@Data
public class SimbGram {
    String elem;

    public SimbGram(String sValor) {
        elem = sValor;
    }
}
