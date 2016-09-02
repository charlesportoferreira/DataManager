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
import weka.classifiers.Classifier;

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
            min = 50029;
            max = 50030;
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
            String fileName = "Dados" + n + ".arff";
            poolBagOfWords.add(new PoolBagOfWord(n, ranker, listaTermos, max / passo));
            poolClassificadores.add(new PoolClassificacao(fileName, n, max / passo));
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
    }

    private static void salvaDados(List<PoolClassificacao> poolClassificadores, FileManager fm) {
        poolClassificadores.sort((PoolClassificacao pc1, PoolClassificacao pc2) -> pc1.numeroAtributos - pc2.numeroAtributos);
        StringBuilder sb = new StringBuilder();
        int k_parameter = 3;
        for (Classifier classificador : poolClassificadores.get(0).classificadores) {
            if (classificador.getClass().getSimpleName().contains("IBk")) {
                sb.append("atributos_").append(classificador.getClass().getSimpleName()).append("_").append(k_parameter).append(",");
                sb.append("acertos_").append(classificador.getClass().getSimpleName()).append("_").append(k_parameter).append(",");
                sb.append("micro_").append(classificador.getClass().getSimpleName()).append("_").append(k_parameter).append(",");
                sb.append("macro_").append(classificador.getClass().getSimpleName()).append("_").append(k_parameter).append(",");
                sb.append("nome_").append(classificador.getClass().getSimpleName()).append("_").append(k_parameter).append(",");
                k_parameter = k_parameter + 2;
            } else {
                sb.append("atributos_").append(classificador.getClass().getSimpleName()).append(",");
                sb.append("acertos_").append(classificador.getClass().getSimpleName()).append(",");
                sb.append("micro_").append(classificador.getClass().getSimpleName()).append(",");
                sb.append("macro_").append(classificador.getClass().getSimpleName()).append(",");
                sb.append("nome_").append(classificador.getClass().getSimpleName()).append(",");
            }
        }
        sb.append("\n");
        for (PoolClassificacao pc : poolClassificadores) {
            sb.append(pc.resultadoClassificacao).append("\n");
        }
        try {
            fm.saveFile("resultadosGenesDM.txt", sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(PoolClassificacao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
