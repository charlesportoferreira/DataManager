/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.ArrayList;

/**
 * S
 *
 * @author charles
 */
public class DataManager {

    public static Map<String, Double> features = new HashMap<>();
    public static List<String> palavrasEssenciais = new ArrayList<>();
    public static double min,max;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        //geraEsssenciaBaseDados();
        getMinMaxFrequency("1Gram.csv");
        System.out.println(min + " : " + max + " : ");
        System.out.println("done");
    }

    public static void geraEsssenciaBaseDados() {
        System.out.println("teste");
        
        try {
            lerDadosWordRank("word_frequency.csv");
            printFile("test.csv", "");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void lerDadosWordRank(String filePath) throws FileNotFoundException, IOException {

        String palavra = "";
        String linha = "";
        double rank = 0;
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            while (br.ready()) {
                linha = br.readLine();
                if (linha.contains("Rank")) {
                    continue;
                }
                palavra = linha.split(",")[0] + " : " + linha.split(",")[1];
                rank = Double.parseDouble(linha.split(",")[5]);
                features.put(palavra, rank);
            }
            br.close();
            fr.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void printFile(String fileName, String texto) throws IOException {
        try {
            StringBuilder sb = new StringBuilder();
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
        
            for (String key : features.keySet()) {
                sb.append("\n");
                sb.append(key);
                sb.append(":");
                sb.append(features.get(key));
            }

            bw.write(sb.toString());
            bw.close();
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private static void createEssentialDataBase(String filePath){
        String palavra = "";
        String linha = "";
        double rank = 0;
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            while (br.ready()) {
                linha = br.readLine();
                palavra =  linha.split(",")[2];
                rank = Double.parseDouble(linha.split(",")[0]);
                if(features.containsKey(palavra)){
                   // double valorEssencia =  features.getKey 
                    palavrasEssenciais.add(palavra + ", " + features.get(palavra));
                }
                features.put(palavra, rank);
            }
            br.close();
            fr.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    private static void getMinMaxFrequency(String filePath){
        String palavra = "";
        String linha = "";
        double frequencia = 0;
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String primeiraLinha = br.readLine();
            min = Double.parseDouble(primeiraLinha.split(",")[0]);
            while (br.ready()) {
                linha = br.readLine();
                if(linha.length() < 2){
                    continue;
                }
                palavra =  linha.split(",")[2];
                frequencia = Double.parseDouble(linha.split(",")[0]);
            }
            br.close();
            fr.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        max = frequencia;
    }

     private static void generateRankedDataBase(String filePath){
        String palavra = "";
        String linha = "";
        double rank = 0;
        double rankWordFrequency;
        List<String> ngramRankeados = new ArrayList<>();
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            while (br.ready()) {
                linha = br.readLine();
                if(linha.length() < 2){
                    continue;
                }
                palavra =  linha.split(",")[2];
                rank = Double.parseDouble(linha.split(",")[0]);
                rank = (rank - min)/(max - min);
                if(features.containsKey(palavra)){
                    rankWordFrequency = rank - features.get(palavra);
                }else{
                    rankWordFrequency = 0;
                }
               
            }
            br.close();
            fr.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
