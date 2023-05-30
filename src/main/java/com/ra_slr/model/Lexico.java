package com.ra_slr.model;

import com.ra_slr.model.Automata;
import lombok.Data;

@Data
public class Lexico {
    final int TOKREC = 9;
    final int MAXTOKENS = 1000;
    String[] lexemas;
    String[] tokens;
    String lexema;
    int noTokens;
    int[] i = {0};
    int iniToken;
    Automata oAFD;

    public Lexico(){
        this.lexemas = new String[MAXTOKENS];
        this.tokens = new String[MAXTOKENS];
        this.oAFD = new Automata();
        i[0] = 0;
        iniToken = 0;
        noTokens = 0;
    }

    public void inicia(){
        i[0] = 0;
        iniToken = 0;
        noTokens = 0;
    }

    private boolean esId(){
        return true;
    }

    public void anade(String tok, String lex){
        tokens[noTokens] = tok;
        lexemas[noTokens++] = lex;
    }

    public boolean analiza(String texto){
        boolean recAuto;
        int noAuto;

        while (i[0] < texto.length()){
            recAuto = false;
            for (noAuto = 0; noAuto < TOKREC && !recAuto;)
                if(oAFD.reconoce(texto, iniToken, i, noAuto))
                    recAuto = true;

                else
                    noAuto ++;

            if((recAuto)){
                lexema = texto.substring(iniToken, i[0]);

                switch (noAuto){
                    case 0:
                        break;
                    case 1:
                        if(this.esId())
                            tokens[noTokens] = "id";
                        else
                            tokens[noTokens] = lexema;
                        break;
                    case 2:
                        tokens[noTokens] = lexema;
                        break;
                    case 3:
                        tokens[noTokens] = lexema;
                        break;
                    case 4:
                        tokens[noTokens] = "num";
                        break;
                    case 7:
                        tokens[noTokens] = "num";
                        break;
                    case 8:
                        tokens[noTokens] = "num";
                        break;
                    case 5:
                        tokens[noTokens] = lexema;
                        break;
                    case 6:
                        tokens[noTokens] = lexema;
                        break;
                }
                if(noAuto > 0){
                    lexemas[noTokens++] = lexema;
                }
            }
            else {
                return false;
            }
            iniToken = i[0];
        }
        return true;
    }



}
