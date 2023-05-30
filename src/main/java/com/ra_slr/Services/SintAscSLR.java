package com.ra_slr.Services;

import com.ra_slr.model.Item;
import com.ra_slr.model.Lexico;
import com.ra_slr.model.Pila;
import com.ra_slr.model.SimbGram;
import org.springframework.stereotype.Service;

@Service
public class SintAscSLR {

    public static final int NOPROD = 10;
    public final int NODDS = 1000;
    public final int NOACTIONS = 1000;
    public final int NOGOTOS = 1000;

    String[] vts = {"", "id", "=", ";", "+", "-", "*", "/", "num", "(", ")", "$"};
    String[] vns = {"", "A", "E", "T", "F"};

    int[][] prod = {{1, 4, -1, -2, 2, -3}, // A->id = E ;
            {2, 3, 2, -4, 3, 0}, // E->E + T
            {2, 3, 2, -5, 3, 0}, // E->E - T
            {2, 1, 3, 0, 0, 0}, // E->T
            {3, 3, 3, -6, 4, 0}, // T->T * F
            {3, 3, 3, -7, 4, 0}, // T->T / F
            {3, 1, 4, 0, 0, 0}, // T->F
            {4, 1, -1, 0, 0, 0}, // F->id
            {4, 1, -8, 0, 0, 0}, // F->num
            {4, 3, -9, 2, -10, 0} // F->( E )
    };

    int[][] sig = {{0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // Renglon que no se usa
            {1, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0}, // SIG(A)={ $  }
            {4, 3, 4, 5, 10, 0, 0, 0, 0, 0, 0}, // SIG(E)={ ; + - )  }
            {6, 6, 7, 3, 4, 5, 10, 0, 0, 0, 0}, // SIG(T)={ * / ; + - )  }
            {6, 6, 7, 3, 4, 5, 10, 0, 0, 0, 0} // SIG(F)={ * / ; + - )  }
    };

    Pila pila;
    int[][] action;
    int noActions;
    int noGoTos;
    int[][] goTo;
    int[] dd;
    int noDds;
    Item[] c;
    int noItems;

    public SintAscSLR()
    {
        pila = new Pila();
        dd = new int[NODDS];
        noDds = 0;
        action = new int[1000 * (vts.length - 1)][4];
        goTo = new int[1000 * (vns.length - 1)][3];
        noActions = 0;
        noGoTos = 0;
        noItems = 0;
    }

    public void inicia()
    {
        pila.inicia();
        noDds = 0;
        noActions = 0;
        noGoTos = 0;
        c = new Item[1000];
        noItems = 0;
        for (int i = 0; i < c.length; i++) {
            c[i] = new Item();
        }


        int[][] arre = {{-1, 0}};
        c[noItems++] = cerradura(new Item(arre, 1));


        int[][] arreItem1 = {{-1, 1}};
        c[noItems++] = new Item(arreItem1, 1);


        for (int i = 0; i < noItems; i++) {
            if (i != 1) {
                agregarConjItems(i);
            }
        }


        goTo[noGoTos][0] = 0;
        goTo[noGoTos][1] = 1;
        goTo[noGoTos++][2] = 1;


        for (int i = 0; i < 1000; i++) {
            generaCambios(i);
            generaReducciones(i);
        }

    }

    public Item cerradura(Item oItem)
    {
        boolean cambios = true;
        while (cambios) {
            for (int i = 0; i < oItem.getNoItems(); i++) {
                int noItemsAgregado = agregarItems(i, oItem);
                if (noItemsAgregado > 0) {
                    cambios = true;
                    break;
                } else {
                    cambios = false;
                }
            }
        }
        return oItem;
    }

    public void agregarConjItems(int i)
    {
        boolean[] marcaItems = new boolean[NOPROD + 1];
        for (int j = 0; j < NOPROD + 1; j++) {
            marcaItems[j] = false;
        }
        marcaItems[0] = i == 0;
        for (int j = 0; j < c[i].getNoItems(); j++) {
            if (!marcaItems[j]) {
                int noProd = c[i].noProd(j);
                int posPto = c[i].posPto(j);
                if (posPto != prod[noProd][1]) {
                    Item oNuevoItem = new Item();
                    int indSimGoTo = prod[noProd][posPto + 2];
                    for (int k = 0; k < c[i].getNoItems(); k++) {
                        if (!marcaItems[k]) {
                            int nP = c[i].noProd(k);
                            int pP = c[i].posPto(k);
                            try {
                                if (indSimGoTo == prod[nP][pP + 2]) {
                                    oNuevoItem.agregar(nP, pP + 1);
                                    marcaItems[k] = true;
                                }
                            } catch (Exception e) {
                                continue;
                            }
                        }
                    }
                    int[] edoYaExiste = {-1};
                    goTo[noGoTos][0] = i;
                    goTo[noGoTos][1] = indSimGoTo;
                    oNuevoItem = cerradura(oNuevoItem);
                    if (!estaNuevoItem(oNuevoItem, edoYaExiste))//verifica si el item no existe
                    {
                        goTo[noGoTos++][2] = noItems;
                        c[noItems++] = oNuevoItem;
                    } else {
                        goTo[noGoTos++][2] = edoYaExiste[0];//calcular el goTo cuando el item no existe
                    }
                }
            }
        }
    }

    public int agregarItems(int i, Item oItem)
    {
        int noItemsAgregado = 0;
        int posPto = oItem.posPto(i);
        int noProd = oItem.noProd(i);
        int indVns = noProd == -1 ? 1 : (posPto == prod[noProd][1] ? 0 : (prod[noProd][posPto + 2] < 0 ? 0 : prod[noProd][posPto + 2]));
        if (indVns > 0) {
            for (int j = 0; j < NOPROD; j++) {
                if (indVns == prod[j][0] && !oItem.existeItem(j, 0))
                {
                    oItem.agregar(j, 0);
                    noItemsAgregado++;
                }
            }
        }
        return noItemsAgregado;
    }

    public boolean estaNuevoItem(Item oNuevoItem, int[] edoYaExiste)
    {
        edoYaExiste[0] = -1;
        for (int i = 0; i < noItems; i++) {
            if (c[i].getNoItems() == oNuevoItem.getNoItems()) {
                int aciertos = 0;
                for (int j = 0; j < c[i].getNoItems(); j++) {
                    for (int k = 0; k < oNuevoItem.getNoItems(); k++) {
                        if (c[i].noProd(j) == oNuevoItem.noProd(k) && c[i].posPto(j) == oNuevoItem.posPto(k)) {
                            aciertos++;
                            break;
                        }
                    }
                }
                if (aciertos == c[i].getNoItems()) //si numero de items son iguales a los aciertos, entonces ya existe
                {
                    edoYaExiste[0] = i;
                    return true;
                }

            }
        }
        return false;
    }

    public void generaReducciones(int i)
    {
        for (int j = 0; j < c[i].getNoItems(); j++) {
            int noProd = c[i].noProd(j);
            int posPto = c[i].posPto(j);
            if (i == 1)
            {
                action[noActions][0] = i;
                action[noActions][1] = vts.length - 1;
                action[noActions][2] = 2;
                action[noActions++][3] = -1;
            } else if (noProd != -1 && posPto == prod[noProd][1]) {
                int indVns = prod[noProd][0];
                for (int k = 1; k <= sig[indVns][0]; k++) {
                    action[noActions][0] = i;
                    action[noActions][1] = sig[indVns][k];
                    action[noActions][2] = 1;
                    action[noActions++][3] = noProd;
                }
            }
        }
    }

    public void generaCambios(int i)
    {
        for (int j = 0; j < c[i].getNoItems(); j++) {
            int noProd = c[i].noProd(j);
            int posPto = c[i].posPto(j);
            if (noProd != -1) {
                if (posPto != prod[noProd][1]) {
                    int indSim = prod[noProd] [posPto + 2];
                    if (indSim < 0) {
                        int edoTrans = -1;
                        for (int k = 0; k < noGoTos; k++) {
                            if (goTo[k][0] == i && goTo[k][1] == indSim) {
                                edoTrans = goTo[k][2];
                                break;
                            }
                        }
                        action[noActions][0] = i;
                        action[noActions][1] = -indSim;
                        action[noActions][2] = 0;
                        action[noActions++][3] = edoTrans;
                    }
                }
            }
        }
    }

    public int analiza(Lexico oAnalex) {
        int ae = 0;
        oAnalex.anade("$", "$");
        pila.push(new SimbGram("0"));
        while (true) {
            String s = pila.tope().getElem();
            String a = oAnalex.getTokens()[ae];
            String accion = accion(s, a);
            switch (accion.charAt(0)) {
                case 's':
                    pila.push(new SimbGram(a));
                    pila.push(new SimbGram(accion.substring(1)));
                    ae++;
                    break;
                case 'r':
                    sacarDosBeta(accion);
                    meterAGoTo(accion);
                    dd[noDds++] = Integer.parseInt(accion.substring(1));
                    break;
                case 'a':
                    return 0;  // aceptacion
                case 'e':
                    return 1;  // error
            }
        }
    }

    public String accion(String s, String a)
    {
        int tipo = -1, no = -1;
        int edo = Integer.parseInt(s);
        int inda = 0;
        boolean enc = false;
        for (int i = 1; i < vts.length; i++) {
            if (vts[i].equals(a)) {
                inda = i;
                break;
            }
        }
        for (int i = 0; i < noActions; i++) {
            if (action[i][0] == edo && action[i][1] == inda) {
                tipo = action[i][2];
                no = action[i][3];
                enc = true;
            }
        }
        if (!enc) {
            return "error";
        } else {
            switch (tipo) {
                case 0:
                    return "s" + Integer.toString(no);
                case 1:
                    return "r" + Integer.toString(no);
                case 2:
                    return "acc";
                default:
                    return "error";
            }
        }

    }

    public void sacarDosBeta(String accion)
    {
        int noProd = Integer.parseInt(accion.substring(1));
        int noVeces = prod[noProd][1] * 2;
        for (int i = 1; i <= noVeces; i++) {
            pila.pop();
        }
    }

    public void meterAGoTo(String accion)
    {
        int sPrima = Integer.parseInt(pila.tope().getElem());
        int noProd = Integer.parseInt(accion.substring(1));
        pila.push(new SimbGram(vns[prod[noProd][0]]));
        for (int i = 0; i < noGoTos; i++) {
            if (sPrima == goTo[i][0] && prod[noProd][0] == goTo[i][1]) {
                pila.push(new SimbGram(Integer.toString(goTo[i][2])));
                break;
            }
        }
    }


}
