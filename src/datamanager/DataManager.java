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

/**
 *
 * @author charles
 */
public class DataManager {

    public static Map<String, Integer> features = new HashMap<>();

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        geraEsssenciaBaseDados();
    }

    public static void geraEsssenciaBaseDados() {
        System.out.println("teste");
    }

    public static void lerDadosWordRank(String filePath) throws FileNotFoundException, IOException {

        String palavra = "";
        String linha = "";
        int rank = 0;
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            while (br.ready()) {
                linha = br.readLine();
                if (linha.contains("Rank")) {
                    continue;
                }
                palavra = linha.split(",")[2];
                rank = Integer.parseInt(linha.split(",")[5]);
                features.put(palavra, rank);
            }
            br.close();
            fr.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public static void printFile(String fileName, String texto) throws IOException {
        try {
            FileWriter fw = new FileWriter(fileName);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(texto);
            bw.newLine();
            bw.close();
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

}
