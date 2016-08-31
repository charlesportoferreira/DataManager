/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class PoolBagOfWord implements Callable<String> {

    public static int count;
    public int numeroAtributos;
    Ranker ranker;
    List<Termo> listaTermos;
    public int total;

    public PoolBagOfWord(int numeroAtributos, Ranker ranker, List<Termo> listaTermos, int total) {
        this.numeroAtributos = numeroAtributos;
        this.listaTermos = listaTermos;
        this.ranker = ranker;
        this.total = total;

    }

    @Override
    public String call() throws Exception {
        BagOfWord bow = new BagOfWord();
        List<Instancia> textos = criaInstanciaTextos(bow, ranker, numeroAtributos, listaTermos);
        List<Instancia> classes = criaInstanciaClasses(bow, numeroAtributos, textos);
        List<Instancia> distancias = criaInstanciasDistancias(textos, classes, bow);
        StringBuilder sbClasses = getNomeClasses(classes);
        String fileName = "Dados" + numeroAtributos + ".arff";
        bow.generateBagOfWord(distancias, classes.size(), sbClasses.toString(), fileName);
        System.out.print("\r" + ++count + " de " + total);
        return "";

    }

    public List<Instancia> criaInstanciaTextos(BagOfWord bow, Ranker ranker, int n, List<Termo> listaTermos) {
        //cria as instancias dos textos
        List<Instancia> textos = new ArrayList<>();
        try {
            textos = bow.geraInstancias("1Gram.txt", ranker.get_n_melhores(n, listaTermos));
        } catch (IOException ex) {
            Logger.getLogger(Ranker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return textos;
    }

    public List<Instancia> criaInstanciaClasses(BagOfWord bow, int n, List<Instancia> textos) {
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
//        System.out.println("\n");
        return classes;
    }

    public List<Instancia> criaInstanciasDistancias(List<Instancia> textos, List<Instancia> classes, BagOfWord bow) {
        //cria as instancias com a distancia entre os textos e as classes
        List<Instancia> distancias = new ArrayList<>();
        for (Instancia texto : textos) {
            Instancia ins = new Instancia(texto.texto, texto.classe, classes.size());
            double[] vet = new double[classes.size()];
            for (int i = 0; i < classes.size(); i++) {
//                vet[i] = bow.getDistanciaEuclidiana(texto, classes.get(i));
                 vet[i] = bow.getDistanciaCosseno(texto, classes.get(i));
            }
            ins.palavras = vet;
            distancias.add(ins);
            // System.out.println(ins.toArff());
        }
        return distancias;
    }

    public StringBuilder getNomeClasses(List<Instancia> classes) {
        StringBuilder sbClasses = new StringBuilder("@ATTRIBUTE classe {");
        for (Instancia cl : classes) {
            sbClasses.append(cl.classe).append(",");
        }
        int pos = sbClasses.lastIndexOf(",");
        sbClasses.replace(pos, pos + 1, "}");
        return sbClasses;
    }

}
