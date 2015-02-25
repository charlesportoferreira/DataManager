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
public class Feature {
    private String nome;
    private double rank;

    public Feature(String nome, double rank) {
        this.nome = nome;
        this.rank = rank;
    }
    
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getRank() {
        return rank;
    }

    public void setRank(double rank) {
        this.rank = rank;
    }

   
    
    
    
}
