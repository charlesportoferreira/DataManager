/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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

    private int[] getVetorAtributos(BufferedReader br, List<Termo> termos) throws FileNotFoundException, IOException {
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

    public int[] geraVetorAtributos(List<Termo> termos, Map<String, Integer> mapaPalavras) {
        int[] vetorAtributos = new int[termos.size()];
        for (int i = 0; i < termos.size(); i++) {
            if (mapaPalavras.containsKey(termos.get(i).nome)) {
                vetorAtributos[i] = mapaPalavras.get(termos.get(i).nome);
            } else {
                vetorAtributos[i] = 0;
            }
        }
        return vetorAtributos;
    }

    public void somaVetores(int[] vec1, int[] vec2) {
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

    private double getDistancia(Instancia texto, Instancia classe) {
        return 0.0;
    }

    private double getDistanciaEuclidiana(Instancia texto, Instancia classe) {
        double somatoria = 0.0;
        for (int i = 0; i < texto.palavras.length; i++) {
            somatoria += Math.pow(texto.palavras[i] - classe.palavras[i], 2);
        }
        return Math.sqrt(somatoria);
    }

}
