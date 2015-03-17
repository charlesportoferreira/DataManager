/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author charles
 */
public class Classe {
    public String nome;
    public Map<String, Integer> palavras;

    public Classe(String nome) {
        this.nome = nome;
        palavras = new HashMap<>();
    }
    
}
