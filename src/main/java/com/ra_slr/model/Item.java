package com.ra_slr.model;

import com.ra_slr.Services.SintAscSLR;
import lombok.Data;

@Data
public class Item {
    int [][] item;
    int noItems;

    public Item(int [][] arre, int len){
        noItems = len;
        item = new int[SintAscSLR.NOPROD+1][2];
        for (int i = 0; i < len; i++)
            for(int j = 0; j < 2; j++)
                item[i][j] = arre[i][j];
    }

    public Item(){
        item = new int[SintAscSLR.NOPROD+1][2];
        noItems = 0;
    }

    public void agregar(int noProd, int posPto){
        item[noItems][0] = noProd;
        item[noItems++][1] = posPto;
    }

    public boolean existeItem(int noProd, int posPto){
        for (int i = 0; i < noItems; i++){
            if(item[i][0] == noProd && item[i][1] == posPto){
                return true;
            }
        }
        return false;
    }

    public int posPto(int i){
        return item[i][1];
    }
    public int noProd(int i){
        return item[i][0];
    }
}
