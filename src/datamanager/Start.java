/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author debora
 */
public class Start {
    public static void main(String args[]) {
        Ranker ranker = new Ranker();
        Map<String, Termo> t1 = new HashMap<>();
        Map<String, Termo> t2 = new HashMap<>();
        try {
            t1 = ranker.getFrequenciaWFL("WFL.csv");
            t2 = ranker.getFrequenciaAbsoluta("1Gram.all");
        } catch (IOException ex) {
            Logger.getLogger(Ranker.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println(Arrays.toString(ranker.getMinMax(t1)));
        System.out.println(Arrays.toString(ranker.getMinMax(t2)));
        ranker.imprimeMap("map1Gram.txt", t2);
        int[] minMax1 = ranker.getMinMax(t1);
        int[] minMax2 = ranker.getMinMax(t2);
        ranker.normalizaDados(t1, minMax1[0], minMax1[1]);
        ranker.normalizaDados(t2, minMax2[0], minMax2[1]);
        ranker.imprimeMap("map1GramN.txt", t2);
        ranker.rank(t1, t2);
        ranker.imprimeMap("map1GramNR.txt", t2);
        List<Termo> listaTermos = ranker.ordenaMap(t2);
        ranker.imprimeList("List1GramOrder.txt", listaTermos);
        int n = 10;
        ranker.imprime_n_melhores(n, listaTermos);

        BagOfWord bow = new BagOfWord();
        List<Instancia> instancias = new ArrayList<>();
        try {
            instancias = bow.geraInstancias("1Gram.txt", ranker.get_n_melhores(n, listaTermos));
        } catch (IOException ex) {
            Logger.getLogger(Ranker.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<Instancia> classes = new ArrayList<>();
        for (String nomeClasse : bow.nomeClasses) {
            for (Instancia in : instancias) {
                if (in.classe.equals(nomeClasse)) {
                    Instancia insClasse = new Instancia(n);
                    insClasse.classe = nomeClasse;
                    bow.somaVetores(insClasse.palavras, in.palavras);
                    classes.add(insClasse);
                }
            }
        }
    }

}
