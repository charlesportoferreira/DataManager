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
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * S
 *
 * @author charles
 */
public class DataManager {

    public Map<String, Double> featuresWordRank = new HashMap<>();
    public double min, max;
    List<Classe> classes = new ArrayList<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        DataManager dm = new DataManager();
        try {
            dm.createEssencialClass("1Gram.txt", "test3.csv");
        } catch (IOException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.exit(0);
        dm.geraEsssenciaBaseDados("word_frequency.csv", "1Gram.all");
        System.out.println(dm.min + " : " + dm.max + " : ");
        System.out.println("done");
    }

    public void geraEsssenciaBaseDados(String wordFrequencFile, String databaseFile) {
        System.out.println("teste");
        try {
            lerDadosWordRank(wordFrequencFile);
            getMinMaxFrequency(databaseFile);
            List<Feature> features = geraEssenciaBaseDados(databaseFile);
            for (Feature feature : features) {
                System.out.println(feature.getNome() + " : " + feature.getRank());
            }
            System.out.println("*******************************************************");
            features.sort(new FeatureComparator());
            System.out.println("*******************************************************");
            StringBuilder sb = new StringBuilder();
            for (Feature feature : features) {
                System.out.println(feature.getNome() + " : " + feature.getRank());
                sb.append(feature.getNome()).append(" : ").append(feature.getRank());
                sb.append("\n");
            }
            printFile("test3.csv", sb.toString());
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
                palavra = linha.split(",")[1].toLowerCase();
                rank = Double.parseDouble(linha.split(",")[5]);
                featuresWordRank.put(palavra, rank);
            }
            br.close();
            fr.close();
        }
        System.out.println(featuresWordRank.size());
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

    private List<Feature> geraEssenciaBaseDados(String filePath) {
        String palavra;
        String linha;
        double rank;
        double valorEssencia;
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
                if (featuresWordRank.containsKey(palavra)) {
                    valorEssencia = rank - featuresWordRank.get(palavra);
                } else {
                    System.out.println("Nao achei " + palavra);
                    valorEssencia = rank - 100;
                }
                palavrasEssenciais.add(new Feature(palavra, valorEssencia));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return palavrasEssenciais;
    }

    private void createEssencialClass(String pretextNGram, String arquivoEssenciaBase) throws IOException {
        List<String> palavras = getPalavrasEssenciais(arquivoEssenciaBase, 0.10);
        String linha;
        Classe c = new Classe("");
        String nomeClasse;
        String palavraLida;
        int frequencia;
        for (String palavra : palavras) {
            try (FileReader fr = new FileReader(pretextNGram); BufferedReader br = new BufferedReader(fr)) {
                while (br.ready()) {
                    linha = br.readLine();
                    if (linha.contains("Maid")) {
                        nomeClasse = linha.split("/")[1];
                        c = getClasse(nomeClasse);
                        continue;
                    }
                    palavraLida = linha.split(":")[0].trim();
                    frequencia = Integer.parseInt(linha.split(":")[1]);
                    if (palavra.equals(palavraLida)) {
                        if (c.palavras.containsKey(palavra)) {
                            frequencia += c.palavras.get(palavra);
                            c.palavras.put(palavra, frequencia);
                        } else {
                            c.palavras.put(palavra, frequencia);
                        }
                    }
                }
                br.close();
                fr.close();
            }
        }
        for (Classe classe : classes) {
            StringBuilder sb = new StringBuilder();
            for (Entry<String, Integer> entry : classe.palavras.entrySet()) {
                sb.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
            }
            printFile("E: " + classe.nome + ".txt", sb.toString());
        }
        int a = 0;
    }

    private List<String> getPalavrasEssenciais(String filePath, double thereshould) throws IOException {
        String linha;
        List<String> palavras = new ArrayList<>();
        double rank;
        try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linha = br.readLine();
                rank = Double.parseDouble(linha.split(":")[1]);
                if (rank >= thereshould) {
                    palavras.add(linha.split(":")[0].trim());
                }
            }
            br.close();
            fr.close();
        }
        return palavras;
    }

    private Classe getClasse(String nome) {
        for (Classe classe : classes) {
            if (classe.nome.equals(nome)) {
                return classe;
            }
        }
        Classe c = new Classe(nome);
        classes.add(c);
        return c;
    }
}
