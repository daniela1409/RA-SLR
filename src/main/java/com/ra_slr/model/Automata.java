package com.ra_slr.model;

import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
public class Automata {
    Pattern[] afd = new Pattern[9];

    public Automata(){
        afd[0] = Pattern.compile("\\s+");
        afd[1] = Pattern.compile("([A-za-z]|_)([A-za-z]|_|[0-9])*");
        afd[2] = Pattern.compile("[\\+\\-\\*/]=|=");
        afd[3] = Pattern.compile("[\\+\\-\\*/]");
        afd[8] = Pattern.compile("[0-9]+");
        afd[4] = Pattern.compile("[0-9]+\\.[0-9]+");
        afd[7] = Pattern.compile("[0-9]+\\.");
        afd[5] = Pattern.compile("\\(|\\)");
        afd[6] = Pattern.compile("\\;");
    }

    public boolean reconoce(String texto, int initToken, int[] i, int noAuth){
        Matcher m = afd[noAuth].matcher(texto);
        if(m.find(initToken)){
            if(m.start() == initToken){
                i[0] = m.end();
                return true;
            }
            else
                return false;
        }
        else {
            return false;
        }
    }
}
