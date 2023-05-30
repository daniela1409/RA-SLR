package com.ra_slr.Services;

import com.ra_slr.model.LexJson;
import com.ra_slr.model.Lexico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReconocedorService {

    @Autowired
    SintAscSLR sintAscSLR;

    public String analiza(LexJson lexJson){

        Lexico lexico = new Lexico();
        lexico.inicia();
        String result = "";

        if(lexico.analiza(lexJson.getLex())){
            List<String> terminales = new ArrayList<>();

            sintAscSLR.inicia();

            result = sintAscSLR.analiza(lexico) == 0 ? "Análisis exitoso": "Error de sintaxis";

        }
        else {
            result = "Error lexico";
        }

        return result;
    }

}
