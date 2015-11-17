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
public class Termo {

    String nome;
    int frequenciaAbs;
    double rank;
    double freqNorm;

    public Termo(String nome, int frequenciaAbs) {
        this.nome = nome;
        this.frequenciaAbs = frequenciaAbs;
    }

    @Override
    public String toString() {
        return nome + " , " + frequenciaAbs + " , " + freqNorm + " , " + rank;
    }

}
