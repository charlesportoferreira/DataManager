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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author charles
 */
public class BagOfWord {

    private String temp;
    Set<String> nomeClasses;
    List<Double[]> data;

    public BagOfWord() {
        this.temp = "";
        nomeClasses = new HashSet<>();
        data = new ArrayList<>();
    }

    public List<Instancia> geraInstancias(String filePath, List<Termo> termos) throws FileNotFoundException, IOException {
        String linhaLida;
        List<Instancia> instancias = new ArrayList<>();
        try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                if (temp.equals("")) {
                    linhaLida = br.readLine();
                } else {
                    linhaLida = temp;
                    temp = "";
                }
                if (linhaLida.contains("/")) {
                    Instancia ins = getInstancia(linhaLida, termos.size());
                    ins.palavras = getVetorAtributos(br, termos);
                    instancias.add(ins);
                }
            }
            br.close();
            fr.close();
        }
        return instancias;
    }

    public Instancia getInstancia(String linhaLida, int size) {
        String[] dados = linhaLida.split("/");
        nomeClasses.add(dados[1]);
        Instancia i = new Instancia(dados[2], dados[1], size);
        return i;
    }

    private double[] getVetorAtributos(BufferedReader br, List<Termo> termos) throws FileNotFoundException, IOException {
        String linhaLida;
        Map<String, Integer> mapaPalavras = new HashMap<>();
        String[] dados;
        while (br.ready()) {
            linhaLida = br.readLine();
            linhaLida = linhaLida.replaceAll(" ", "");
            if (linhaLida.matches("[a-z]+:[0-9]+")) {
                dados = linhaLida.split(":");
                mapaPalavras.put(dados[0], Integer.parseInt(dados[1]));
            } else {
                temp = linhaLida;
                break;
            }
        }
        return geraVetorAtributos(termos, mapaPalavras);
    }

    public double[] geraVetorAtributos(List<Termo> termos, Map<String, Integer> mapaPalavras) {
        double[] vetorAtributos = new double[termos.size()];
        for (int i = 0; i < termos.size(); i++) {
            if (mapaPalavras.containsKey(termos.get(i).nome)) {
                vetorAtributos[i] = mapaPalavras.get(termos.get(i).nome);
            } else {
                vetorAtributos[i] = 0;
            }
        }
        return vetorAtributos;
    }

    public void somaVetores(double[] vec1, double[] vec2) {
        for (int i = 0; i < vec1.length; i++) {
            vec1[i] = vec1[i] + vec2[i];
        }
    }

    public void geraBagOfWord(List<Instancia> textos, List<Instancia> classes) {
        for (Instancia texto : textos) {
            Double[] dists = new Double[classes.size()];
            int i = 0;
            for (Instancia classe : classes) {
                dists[i] = getDistanciaEuclidiana(texto, classe);
                i++;
            }
            data.add(dists);
        }
    }

    public double getDistanciaJaccart(Instancia texto, Instancia classe) {
        double numInterseccaoPalavras = 0;
        double numUniaoPalavras = 0;
        for (int i = 0; i < texto.palavras.length; i++) {
            if ((texto.palavras[i] == 0 && classe.palavras[i] > 0) | (texto.palavras[i] > 0 && classe.palavras[i] == 0)) {
                numInterseccaoPalavras++;
            }
            if ((texto.palavras[i] == 0 && classe.palavras[i] == 0)) {
                continue;
            }
            numUniaoPalavras++;
        }
        return numInterseccaoPalavras / numUniaoPalavras;
    }

    public double getDistanciaManhattan(Instancia texto, Instancia classe) {
        double somatorio = 0;
        for (int i = 0; i < texto.palavras.length; i++) {
            somatorio += Math.abs(texto.palavras[i] - classe.palavras[i]);
        }
        return somatorio;
    }

    public double getDistanciaCosseno(Instancia texto, Instancia classe) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < texto.palavras.length; i++) {
            dotProduct += texto.palavras[i] * classe.palavras[i];
            normA += Math.pow(texto.palavras[i], 2);
            normB += Math.pow(texto.palavras[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
//        return 0.0;
    }

    public double getDistanciaEuclidiana(Instancia texto, Instancia classe) {
        double somatoria = 0.0;
        for (int i = 0; i < texto.palavras.length; i++) {
            somatoria += Math.pow(texto.palavras[i] - classe.palavras[i], 2);
        }
        return Math.sqrt(somatoria);
    }

    public void generateBagOfWord(List<Instancia> instancias, int numClasses, String classes, String nomeArquivo) {
        StringBuilder sb = new StringBuilder();
        sb.append("@RELATION teste");
        sb.append("\n\n");

        for (int i = 0; i < numClasses; i++) {
            sb.append("@ATTRIBUTE ").append("DC").append(i).append(" REAL").append("\n");
        }

        sb.append(classes).append("\n");

        sb.append("\n@data\n");
        int i = 0;
        for (Instancia instancia : instancias) {
            sb.append(instancia.toArff()).append("\n");
        }
        //@ATTRIBUTE C0	REAL
        //@ATTRIBUTE classe {185,388,041,344,211,612,111}
        try {
            printData(nomeArquivo, sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(BagOfWord.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void printData(String fileName, String texto) throws IOException {
        try (FileWriter fw = new FileWriter(fileName); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(texto);
            bw.close();
            fw.close();
        }
    }

}
