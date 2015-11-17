/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

/**
 *
 * @author charles
 */
public class Instancia {
    String texto;
    String classe;
    int[] palavras;

    public Instancia(int tamanho) {
        this.palavras = new int[tamanho];
    }

    public Instancia(String texto, String classe, int tamanho) {
        this.texto = texto;
        this.classe = classe;
    }

    
}
