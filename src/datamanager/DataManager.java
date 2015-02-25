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
    public List<String> palavrasEssenciais = new ArrayList<>();
    public double min, max;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        DataManager dm = new DataManager();
        //dm.geraEsssenciaBaseDados();
        dm.getMinMaxFrequency("1Gram.csv");
        System.out.println(dm.min + " : " + dm.max + " : ");
        System.out.println("done");
    }

    public void geraEsssenciaBaseDados() {
        System.out.println("teste");

        try {
            lerDadosWordRank("word_frequency.csv");
            printFile("test.csv", "");
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
                palavra = linha.split(",")[0] + " : " + linha.split(",")[1];
                rank = Double.parseDouble(linha.split(",")[5]);
                features.put(palavra, rank);
            }
        }
    }

    private void printFile(String fileName, String texto) throws IOException {
        try {
            StringBuilder sb = new StringBuilder();
            try (FileWriter fw = new FileWriter(fileName); BufferedWriter bw = new BufferedWriter(fw)) {
                for (String key : features.keySet()) {
                    sb.append("\n");
                    sb.append(key);
                    sb.append(":");
                    sb.append(features.get(key));
                }
                bw.write(sb.toString());
            }
        } catch (IOException ex) {
            System.out.println("Erro manipulacao de dados...");
        }
    }

    private void createEssentialDataBase(String filePath) {
        String palavra;
        String linha;
        double rank;
        try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linha = br.readLine();
                palavra = linha.split(",")[2];
                rank = Double.parseDouble(linha.split(",")[0]);
                if (features.containsKey(palavra)) {
                    palavrasEssenciais.add(palavra + ", " + features.get(palavra));
                }
                features.put(palavra, rank);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
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
        List<Feature> essencias = new ArrayList<>();

        try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linha = br.readLine();
                if (linha.length() < 2) {
                    continue;
                }
                palavra = linha.split(",")[2];
                rank = Double.parseDouble(linha.split(",")[0]);
                rank = (rank - min) / (max - min);
                if (features.containsKey(palavra)) {
                    essencia = rank - features.get(palavra);
                } else {
                    essencia = 0;
                }
                essencias.add(new Feature(palavra, rank));
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(DataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        return essencias;
    }

}
