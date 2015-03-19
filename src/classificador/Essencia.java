package classificador;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author debora
 */
public class Essencia {

    private String nome;
    private List<Double> dados;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Essencia(String nome) {
        this.nome = nome;
        dados = new ArrayList<>();
    }

    public List<Double> getDados() {
        return dados;
    }
    
    

    public void criaVetorEssencia(List<String> palavras, String filePath) throws IOException {
        String linha;
        String word;
        double valor;
        boolean possuiPalavra;
        for (String palavra : palavras) {
            possuiPalavra = false;
            try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
                while (br.ready()) {
                    linha = br.readLine();
                    word = linha.split(":")[0];
                    valor = Double.parseDouble(linha.split(":")[1]);
                    if (word.equals(palavra)) {
                        dados.add(valor);
                        possuiPalavra = true;
                        break;
                    }
                }
                if (!possuiPalavra) {
                    dados.add(0.0);
                }

                fr.close();
            }
        }
    }
}
