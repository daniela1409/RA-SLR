package com.ra_slr.Controllers;

import com.ra_slr.Services.ReconocedorService;
import com.ra_slr.Services.SintAscSLR;
import com.ra_slr.model.LexJson;
import com.ra_slr.model.Lexico;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("reconocedor")
public class reconocedorController {

    @Autowired
    ReconocedorService reconocedorService;

    @GetMapping("")
    public String analizaLexico(@RequestBody LexJson lex){

        return reconocedorService.analiza(lex);
    }
}
