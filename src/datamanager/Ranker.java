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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author charles
 */
public class Ranker {

    
    public Map<String, Termo> getFrequenciaAbsoluta(String filePath) throws FileNotFoundException, IOException {
        Map<String, Termo> termos = new HashMap<>();
        String linhaLida;
        String[] dados;
        Termo t;
        int i = 0;
        try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linhaLida = br.readLine();
                dados = linhaLida.split(":");
                t = new Termo(dados[2], Integer.parseInt(dados[0]));
                termos.put(t.nome, t);
            }
            br.close();
            fr.close();
        }
        return termos;
    }

    public int[] getMinMax(Map<String, Termo> termos) {
        int min = 10000, max = 0;
        Termo t;
        for (Map.Entry pair : termos.entrySet()) {
            t = (Termo) pair.getValue();
            if (t.frequenciaAbs > max) {
                max = t.frequenciaAbs;
            }
            if (t.frequenciaAbs < min) {
                min = t.frequenciaAbs;
            }
        }
        return new int[]{min, max};
    }

    public void normalizaDados(Map<String, Termo> termos, double min, double max) {
        Iterator it = termos.entrySet().iterator();
        Termo termo;
        int freqAbsoluta;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            termo = (Termo) pair.getValue();
            freqAbsoluta = termo.frequenciaAbs;
            termo.freqNorm = (Double) ((freqAbsoluta - min) / (max - min)) * ((100 - 0) + 0);
        }
    }

    public Map<String, Termo> getFrequenciaWFL(String filePath) throws FileNotFoundException, IOException {
        Map<String, Termo> termos = new HashMap<>();
        String linhaLida;
        String[] dados;
        int frequencia;
        Termo t;
        try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
            while (br.ready()) {
                linhaLida = br.readLine();
                if (!linhaLida.matches("^[0-9].*[0-9]$")) {
                    continue;
                }
                dados = linhaLida.split(";");
                try {
                    frequencia = Integer.parseInt(dados[3]);
                } catch (NumberFormatException ex) {
                    continue;
                }
                t = new Termo(dados[1].replaceAll(" |\t", ""), frequencia);
                termos.put(t.nome, t);
            }
            br.close();
            fr.close();
        }
        return termos;
    }

    public void imprimeMap(String fileName, Map<String, Termo> termos) {
        StringBuilder sb = mapTermoToString(termos);
        try {
            printFile(fileName, sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(Ranker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void imprimeList(String fileName, List< Termo> termos) {
        StringBuilder sb = listTermoToString(termos);
        try {
            printFile(fileName, sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(Ranker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public StringBuilder mapTermoToString(Map<String, Termo> termos) {
        Iterator it = termos.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        Termo t;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            t = (Termo) pair.getValue();
            sb.append(t.toString()).append("\n");
        }
        return sb;
    }

    public StringBuilder listTermoToString(List<Termo> listaTermos) {
        StringBuilder sb = new StringBuilder();
        for (Termo t : listaTermos) {
            sb.append(t.toString()).append("\n");
        }
        return sb;
    }

    public void printFile(String fileName, String texto) throws IOException {
        try (FileWriter fw = new FileWriter(fileName); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(texto);
            bw.close();
            fw.close();
        }
    }

    public void rank(Map<String, Termo> termosWFL, Map<String, Termo> termos) {
        Iterator it = termos.entrySet().iterator();
        Termo t, termoWFL;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            t = (Termo) pair.getValue();
            if (termosWFL.containsKey(t.nome)) {
                termoWFL = termosWFL.get(t.nome);
                t.rank = t.freqNorm - termoWFL.freqNorm;
                System.out.println(t.freqNorm - termoWFL.freqNorm);
            } else {
                t.rank = t.freqNorm - 0;
            }
        }
    }

    public List<Termo> ordenaMap(Map<String, Termo> termos) {
        List<Termo> listaTermos = convertMapToList(termos);
        listaTermos.sort(new TermoComparator());
        return listaTermos;
    }

    public List<Termo> convertMapToList(Map<String, Termo> termos) {
        List<Termo> listaTermos = new ArrayList<>();
        Iterator it = termos.entrySet().iterator();
        Termo t;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            t = (Termo) pair.getValue();
            listaTermos.add(t);
            it.remove();
        }
        return listaTermos;
    }

    public void imprime_n_melhores(int n, List<Termo> termos) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            sb.append(termos.get(i).toString()).append("\n");
        }
        try {
            printFile("n_melhores.txt", sb.toString());
        } catch (IOException ex) {
            Logger.getLogger(Ranker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public List<Termo> get_n_melhores(int n, List<Termo> termos) {
        List<Termo> melhoresTermos = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            melhoresTermos.add(termos.get(i));
            System.out.print(termos.get(i).nome + "\t");
        }

        return melhoresTermos;
    }

}
