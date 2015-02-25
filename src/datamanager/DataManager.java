/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * S
 *
 * @author charles
 */
public class DataManager {

    public Map<String, Double> features = new HashMap<>();
    public double min, max;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        DataManager dm = new DataManager();
        dm.geraEsssenciaBaseDados("word_frequency.csv", "texto.csv");
        System.out.println(dm.min + " : " + dm.max + " : ");
        System.out.println("done");
    }

    public void geraEsssenciaBaseDados(String wordFrequencFile, String databaseFile) {
        System.out.println("teste");
        try {
            lerDadosWordRank(wordFrequencFile);
            getMinMaxFrequency(databaseFile);
            List<Feature> features = generateRankedDataBase(databaseFile);
            for (Feature feature : features) {
                System.out.println(feature.getNome() + " : " + feature.getRank());
            }
            System.out.println("*******************************************************");
            features.sort(new FeatureComparator());
            System.out.println("*******************************************************");
            StringBuilder sb = new StringBuilder();
            for (Feature feature : features) {
                System.out.println(feature.getNome() + " : " + feature.getRank());
                sb.append(feature.getNome() + " : " + feature.getRank());
                sb.append("\n");
            }
          printFile("test2.csv", sb.toString());
        } catch (Exception ex) {
            System.out.println("Erro manipulacao de arquivo");
        }
    }

    public void lerDadosWordRank(String filePath) throws FileNotFoundException, IOException {

        String palavra;
        String linha;
        double rank;
        try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linha = br.readLine();
                if (linha.contains("Rank")) {
                    continue;
                }
                palavra =  linha.split(",")[1];
                rank = Double.parseDouble(linha.split(",")[5]);
                features.put(palavra, rank);
            }
        }
        System.out.println(features.size());
    }

    private void printFile(String fileName, String texto) throws IOException {

        StringBuilder sb = new StringBuilder();
        try (FileWriter fw = new FileWriter(fileName); BufferedWriter bw = new BufferedWriter(fw)) {
//            for (String key : features.keySet()) {
//                sb.append("\n");
//                sb.append(key);
//                sb.append(":");
//                sb.append(features.get(key));
//            }
           
            bw.write(texto);
        }
    }

    public void getMinMaxFrequency(String filePath) {
        String palavra;
        String linha;
        double frequencia = 0;
        try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
            String primeiraLinha = br.readLine();
            min = Double.parseDouble(primeiraLinha.split(":")[0]);
            while (br.ready()) {
                linha = br.readLine();
                if (linha.length() < 2) {
                    continue;
                }
                frequencia = Double.parseDouble(linha.split(":")[0]);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        max = frequencia;

    }

    private List<Feature> generateRankedDataBase(String filePath) {
        String palavra;
        String linha;
        double rank;
        double essencia;
        List<Feature> palavrasEssenciais = new ArrayList<>();
        try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linha = br.readLine();
                if (linha.length() < 2) {
                    continue;
                }
                palavra = linha.split(":")[2];
                rank = Double.parseDouble(linha.split(":")[0]);
                //normalizacao do dado de 0 ~ 1
                rank = (rank - min) / (max - min);
                if (features.containsKey(palavra)) {
                    essencia = rank - features.get(palavra);
                } else {
                    System.out.println("Nao achei " + palavra);
                    essencia = 0;
                }
                palavrasEssenciais.add(new Feature(palavra, essencia));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return palavrasEssenciais;
    }

}
