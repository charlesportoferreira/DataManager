/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classificador;

import java.util.List;

/**
 *
 * @author debora
 */
public class Classificador {

    public static void main(String[] args) {
        System.out.println("************* Metodo de teste ***************");
        double vet1[] = {0.3, 0.0, 0.5};
        double vet2[] = {0.5, 0.4, 0.3};

        System.out.println(new Classificador().similaridade(vet1, vet2));
    }

    public void classifica(Texto texto, List<Essencia> essencias) {
        double similaridade = 0;
        for (Essencia essencia : essencias) {
            similaridade = similaridade(texto.dados, essencia.getDados());
            if (similaridade > texto.getProximidade()) {
                texto.setProximidade(similaridade);
                texto.setNomeClasseSugerida(essencia.getNome());
            }
        }
    }

    public double similaridade(double[] doc1, double[] doc2) {

        int size = doc1.length;
        double somatorio = 0;
        double somaDoc1 = 0;
        double somaDoc2 = 0;
        double produtorio;
        for (int i = 0; i < size; i++) {
            somatorio += doc1[i] * doc2[i];
        }

        for (int i = 0; i < size; i++) {
            somaDoc1 += Math.pow(doc1[i], 2);
            somaDoc2 += Math.pow(doc2[i], 2);
        }
        somaDoc1 = Math.pow(somaDoc1, 0.5);
        somaDoc2 = Math.pow(somaDoc2, 0.5);
        produtorio = somaDoc1 * somaDoc2;

        return somatorio / produtorio;
    }

    public double getAcertosClassificacao(List<Texto> textos) {
        double corretos = 0;
        double incorretos = 0;
        for (Texto texto : textos) {
            if (texto.getNomeClasseOrigem().equals(texto.getNomeClasseSugerida())) {
                corretos++;
            } else {
                incorretos++;
            }
        }

        return (100 * corretos) / textos.size();
    }

}
