/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author charles
 */
public class Classe {

    public String nome;
    public Map<String, Integer> palavras;
    public List<Double> dados;

    public Classe(String nome) {
        this.nome = nome;
        palavras = new HashMap<>();
        dados = new ArrayList<>();
    }

    
    

}
