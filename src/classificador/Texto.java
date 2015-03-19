/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package classificador;

/**
 *
 * @author debora
 */
public class Texto {

    private String nomeTexto;
    private String nomeClasseOrigem;
    private String nomeClasseSugerida;
    private double proximidade;
    public double[] dados;

    public Texto(int tamanho) {
        dados = new double[tamanho];
    }

    public double getProximidade() {
        return proximidade;
    }

    public void setProximidade(double proximidade) {
        this.proximidade = proximidade;
    }

    public String getNomeTexto() {
        return nomeTexto;
    }

    public void setNomeTexto(String nomeTexto) {
        this.nomeTexto = nomeTexto;
    }

    public String getNomeClasseOrigem() {
        return nomeClasseOrigem;
    }

    public void setNomeClasseOrigem(String nomeClasseOrigem) {
        this.nomeClasseOrigem = nomeClasseOrigem;
    }

    public String getNomeClasseSugerida() {
        return nomeClasseSugerida;
    }

    public void setNomeClasseSugerida(String nomeClasseSugerida) {
        this.nomeClasseSugerida = nomeClasseSugerida;
    }

}
