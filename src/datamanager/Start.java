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
 * @author charles
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
        int n = 300;
        ranker.imprime_n_melhores(n, listaTermos);

        BagOfWord bow = new BagOfWord();

        //cria as instancias dos textos
        List<Instancia> textos = new ArrayList<>();
        try {
            textos = bow.geraInstancias("1Gram.txt", ranker.get_n_melhores(n, listaTermos));
        } catch (IOException ex) {
            Logger.getLogger(Ranker.class.getName()).log(Level.SEVERE, null, ex);
        }

        //cria as instancias das classes
        List<Instancia> classes = new ArrayList<>();
        for (String nomeClasse : bow.nomeClasses) {
            Instancia insClasse = new Instancia(n);
            insClasse.classe = nomeClasse;
            for (Instancia insTextos : textos) {
                if (insTextos.classe.equals(nomeClasse)) {
                    bow.somaVetores(insClasse.palavras, insTextos.palavras);

                }
            }
            classes.add(insClasse);
        }
        System.out.println("\n");

        //cria as instancias com a distancia entre os textos e as classes
        List<Instancia> distancias = new ArrayList<>();
        for (Instancia texto : textos) {
            Instancia ins = new Instancia(texto.texto, texto.classe, classes.size());
            double[] vet = new double[classes.size()];
            for (int i = 0; i < classes.size(); i++) {
                vet[i] = bow.getDistanciaEuclidiana(texto, classes.get(i));
            }
            ins.palavras = vet;
            distancias.add(ins);
            System.out.println(ins.toArff());
        }

        StringBuilder sbClasses = new StringBuilder("@ATTRIBUTE classe {");
        for (Instancia cl : classes) {
            sbClasses.append(cl.classe).append(",");
        }
        int pos = sbClasses.lastIndexOf(",");
        sbClasses.replace(pos, pos + 1, "}");

        bow.generateBagOfWord(distancias, classes.size(), sbClasses.toString(), "teste.arff");

    }

}
