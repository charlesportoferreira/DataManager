/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

import java.util.Arrays;

/**
 *
 * @author charles
 */
public class Instancia {

    String texto;
    String classe;
    double[] palavras;

    public Instancia(int tamanho) {
        this.palavras = new double[tamanho];
    }

    public Instancia(String texto, String classe, int tamanho) {
        this.texto = texto;
        this.classe = classe;
    }

    @Override
    public String toString() {
        return texto + " " + classe + " " + Arrays.toString(palavras);
    }

    public String toArff() {
        StringBuilder sb = new StringBuilder();
        sb.append(Arrays.toString(palavras));
        sb.append(",").append(classe);
        return sb.toString().replaceAll("\\s|\\[|\\]", "");
    }

}
