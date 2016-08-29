/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;
import util.FileManager;

/**
 *
 * @author charles
 */
public class Start {

    public static void main(String args[]) {
        int min, max, passo = 0;
        if (args.length > 0) {
            min = Integer.parseInt(args[0]);
            max = Integer.parseInt(args[1]);
            passo = Integer.parseInt(args[2]);
        } else {
            min = 100;
            max = 600;
            passo = 100;
        }

        FileManager fm = new FileManager();
        fm.limpaDados();
        //int n = 500;
        Ranker ranker = new Ranker();
        Map<String, Termo> mapaWFL = new HashMap<>();
        Map<String, Termo> mapaBase = new HashMap<>();
        try {
            mapaWFL = ranker.getFrequenciaWFL("WFL.csv");
            mapaBase = ranker.getFrequenciaAbsoluta("1Gram.all");
        } catch (IOException ex) {
            Logger.getLogger(Ranker.class.getName()).log(Level.SEVERE, null, ex);
        }
        //System.out.println(Arrays.toString(ranker.getMinMax(mapaWFL)));
        //System.out.println(Arrays.toString(ranker.getMinMax(mapaBase)));
        ranker.imprimeMap("map1Gram.txt", mapaBase);
        int[] minMaxWFL = ranker.getMinMax(mapaWFL);
        int[] minMaxBase = ranker.getMinMax(mapaBase);
        ranker.normalizaDados(mapaWFL, minMaxWFL[0], minMaxWFL[1]);
        ranker.normalizaDados(mapaBase, minMaxBase[0], minMaxBase[1]);
        ranker.imprimeMap("map1GramN.txt", mapaBase);
        ranker.rank(mapaWFL, mapaBase);
        ranker.imprimeMap("map1GramNR.txt", mapaBase);
        List<Termo> listaTermos = ranker.ordenaMap(mapaBase);
        ranker.imprimeList("List1GramOrder.txt", listaTermos);

        List<PoolClassificacao> poolClassificadores = new ArrayList<>(20);
        List<PoolBagOfWord> poolBagOfWords = new ArrayList<>();

        for (int n = min; n < max; n = n + passo) {
//        ranker.imprime_n_melhores(n, listaTermos);
//            BagOfWord bow = new BagOfWord();
//            List<Instancia> textos = criaInstanciaTextos(bow, ranker, n, listaTermos);
//            List<Instancia> classes = criaInstanciaClasses(bow, n, textos);
//            List<Instancia> distancias = criaInstanciasDistancias(textos, classes, bow);
//            StringBuilder sbClasses = getNomeClasses(classes);
            String fileName = "Dados" + n + ".arff";
//            bow.generateBagOfWord(distancias, classes.size(), sbClasses.toString(), fileName);

            poolBagOfWords.add(new PoolBagOfWord(n, ranker, listaTermos, max / 100));
            poolClassificadores.add(new PoolClassificacao(fileName, n, max / 100));

        }
        System.out.println("\nIniciando a criaçao de bag of words...");
        criaBagofWordsParalelo(poolBagOfWords);
        System.out.println("\niniciando a classificacao...");
        classificaParalelo(poolClassificadores);
        salvaDados(poolClassificadores, fm);
        fm.limpaDados();
    }

    public static void criaBagofWordsParalelo(List<PoolBagOfWord> poolBagOfWords) {
        List<Future> futures = new ArrayList<>();
//        ExecutorService pool = Executors.newFixedThreadPool(4);
        ExecutorService pool = Executors.newWorkStealingPool();
        for (PoolBagOfWord pbgow : poolBagOfWords) {
            Future f = pool.submit(pbgow);
            futures.add(f);
        }
        for (Future future : futures) {
            try {
//                System.out.println(future.get());
                future.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                throw new RuntimeException("erro na paralelizacao do fitness");
            }
        }
        pool.shutdown();
    }

    private static void classificaParalelo(List<PoolClassificacao> poolClassificadores) throws RuntimeException {
        List<Future> futures = new ArrayList<>();
//        ExecutorService pool = Executors.newFixedThreadPool(4);
        ExecutorService pool = Executors.newWorkStealingPool();
        for (PoolClassificacao pc : poolClassificadores) {
            Future f = pool.submit(pc);
            futures.add(f);
        }
        for (Future future : futures) {
            try {
//                System.out.println(future.get());
                future.get();
            } catch (InterruptedException | ExecutionException ex) {
                ex.printStackTrace();
                throw new RuntimeException("erro na paralelizacao do fitness");
            }
        }
        pool.shutdown();

//            System.out.println("");
//        classifica(fileName, new SMO(), n);
////            classifica(fileName, new HyperPipes(), n);
//        classifica(fileName, new IBk(3), n);
//        classifica(fileName, new IBk(5), n);
//        classifica(fileName, new IBk(7), n);
//        classifica(fileName, new IBk(9), n);
//        classifica(fileName, new IBk(11), n);
//        classifica(fileName, new IBk(13), n);
//        classifica(fileName, new IBk(15), n);
//        classifica(fileName, new IBk(17), n);
//        classifica(fileName, new IBk(19), n);
//        classifica(fileName, new NaiveBayes(), n);
//        classifica(fileName, new NaiveBayesSimple(), n);
//        classifica(fileName, new J48(), n);
//            classifica(fileName, new MultilayerPerceptron(), n);
//            classifica(fileName, new GridSearch(), n);
        //classifica(fileName, new IBk(1), n);
    }

//    public static void classifica(String fileName, Classifier classificador, int n) {
//        WekaSimulation wekaSimulation = new WekaSimulation();
//        // SMO smo = new SMO();
//
//        wekaSimulation.classifica(classificador, fileName);
//        System.out.println(wekaSimulation + "\t" + n + " selecionados" + "\t" + classificador.getClass().getSimpleName());
//    }
    public static StringBuilder getNomeClasses(List<Instancia> classes) {
        StringBuilder sbClasses = new StringBuilder("@ATTRIBUTE classe {");
        for (Instancia cl : classes) {
            sbClasses.append(cl.classe).append(",");
        }
        int pos = sbClasses.lastIndexOf(",");
        sbClasses.replace(pos, pos + 1, "}");
        return sbClasses;
    }

    public static List<Instancia> criaInstanciasDistancias(List<Instancia> textos, List<Instancia> classes, BagOfWord bow) {
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
            // System.out.println(ins.toArff());
        }
        return distancias;
    }

    public static List<Instancia> criaInstanciaClasses(BagOfWord bow, int n, List<Instancia> textos) {
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

    public static List<Instancia> criaInstanciaTextos(BagOfWord bow, Ranker ranker, int n, List<Termo> listaTermos) {
        //cria as instancias dos textos
        List<Instancia> textos = new ArrayList<>();
        try {
            textos = bow.geraInstancias("1Gram.txt", ranker.get_n_melhores(n, listaTermos));
        } catch (IOException ex) {
            Logger.getLogger(Ranker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return textos;
    }

    private static void salvaDados(List<PoolClassificacao> poolClassificadores, FileManager fm) {
        poolClassificadores.sort((PoolClassificacao pc1, PoolClassificacao pc2) -> pc1.numeroAtributos - pc2.numeroAtributos);
        StringBuilder sb = new StringBuilder();
        for (PoolClassificacao pc : poolClassificadores) {
            sb.append(pc.resultadoClassificacao).append("\n");
        }
        try {
            fm.saveFile("resultados.txt", sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(PoolClassificacao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
