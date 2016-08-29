/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

//import static datamanager.Start.classifica;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
//import weka.attributeSelection.ASEvaluation;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.bayes.NaiveBayesSimple;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.classifiers.lazy.IBk;
import weka.classifiers.trees.J48;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class PoolClassificacao implements Callable<String> {

    public String nomeArquivo;
    public List<Classifier> classificadores;
    public int numeroAtributos;
    public StringBuilder resultadoClassificacao;
    int total;
    public static int count;

    public PoolClassificacao(String nomeArquivo, int numeroAtributos, int total) {
        this.total = total;
        this.nomeArquivo = nomeArquivo;
        this.numeroAtributos = numeroAtributos;
        classificadores = new ArrayList<>();
        criaListaClassificadores();
        resultadoClassificacao = new StringBuilder();
    }

    public void classifica(String fileName, Classifier classificador, int n) {
        WekaSimulation wekaSimulation = new WekaSimulation();
        wekaSimulation.classifica(classificador, fileName);
        resultadoClassificacao
                .append(wekaSimulation.toString(", "))
                .append(", ")
                .append(classificador.getClass().getSimpleName())
                .append(", ");
//        System.out.println(wekaSimulation + "\t" + n + " selecionados" + "\t" + classificador.getClass().getSimpleName());
    }

    private void criaListaClassificadores() {
        classificadores.add(new SMO());
        classificadores.add(new IBk(3));
        classificadores.add(new IBk(5));
        classificadores.add(new IBk(7));
        classificadores.add(new IBk(9));
        classificadores.add(new IBk(11));
        classificadores.add(new IBk(13));
        classificadores.add(new IBk(15));
        classificadores.add(new IBk(17));
        classificadores.add(new IBk(19));
        classificadores.add(new NaiveBayes());
        classificadores.add(new NaiveBayesSimple());
        classificadores.add(new MultilayerPerceptron());
        classificadores.add(new J48());
    }

    @Override
    public String call() throws Exception {
        for (Classifier classificador : classificadores) {
            classifica(nomeArquivo, classificador, numeroAtributos);
        }
        System.out.print("\r" + ++count + " de " + total);
        return "";
    }
}
