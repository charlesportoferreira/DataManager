/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.functions.SMO;
import weka.classifiers.misc.HyperPipes;
import weka.core.Instances;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class WekaSimulation {

    private double pctAcerto;
    private double microAverage;
    private double macroAverage;
    private int numAtributos;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SMO smo = new SMO();
        HyperPipes hyperpipes = new HyperPipes();
//        classifier.buildClassifier(trainset);
        // new WekaSimulation().classifica(smo);

    }

    public void classifica(Classifier classifier, String fileName) {
        BufferedReader datafile = readDataFile(fileName);

        Instances data;
        Evaluation eval;
        try {
            data = new Instances(datafile);
            data.setClassIndex(data.numAttributes() - 1);
            numAtributos = data.numAttributes();
            eval = new Evaluation(data);
            Random rand = new Random(1); // usando semente = 1
            int folds = 10;
            eval.crossValidateModel(classifier, data, folds, rand);
            //this.fitness = eval.pctCorrect();
            //fitness = new BigDecimal(fitness).setScale(2, RoundingMode.HALF_UP).doubleValue();//arredondamento para duas casas
            pctAcerto = eval.pctCorrect();
            pctAcerto = new BigDecimal(pctAcerto).setScale(2, RoundingMode.HALF_UP).doubleValue();
            microAverage = getMicroAverage(eval, data);
            microAverage = new BigDecimal(microAverage).setScale(2, RoundingMode.HALF_UP).doubleValue();
            macroAverage = getMacroAverage(eval, data);
            macroAverage = new BigDecimal(macroAverage).setScale(2, RoundingMode.HALF_UP).doubleValue();

        } catch (Exception ex) {
            Logger.getLogger(WekaSimulation.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static BufferedReader readDataFile(String filename) {
        BufferedReader inputReader = null;

        try {
            inputReader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            System.err.println("File not found: " + filename);
        }

        return inputReader;
    }

    private double getMacroAverage(Evaluation eval, Instances data) {
        double macroMeasure;
        double macroPrecision = 0;
        double macroRecall = 0;

        for (int i = 0; i < data.numClasses(); i++) {
            macroPrecision += eval.precision(i);
            macroRecall += eval.recall(i);
        }
        macroPrecision = macroPrecision / data.numClasses();
        macroRecall = macroRecall / data.numClasses();
        macroMeasure = (macroPrecision * macroRecall * 2) / (macroPrecision + macroRecall);
        //System.out.println("macroMeasure: " + macroMeasure);

        return macroMeasure;
    }

    private double getMicroAverage(Evaluation eval, Instances data) {
        double TP = 0;
        double TP_plus_FP = 0;
        double TP_plus_FN = 0;
        double microPrecision;
        double microRecall;
        double microMeasure;

        for (int i = 0; i < data.numClasses(); i++) {
            TP += eval.truePositiveRate(i);
            TP_plus_FP += eval.truePositiveRate(i) + eval.falsePositiveRate(i);
            TP_plus_FN += eval.truePositiveRate(i) + eval.falseNegativeRate(i);
        }
        microPrecision = TP / TP_plus_FP;
        microRecall = TP / TP_plus_FN;
        microMeasure = (microPrecision * microRecall * 2) / (microPrecision + microRecall);

        //System.out.println("microMeasure: " + microMeasure);
        return microMeasure;
    }

    @Override
    public String toString() {
        return "at:" + numAtributos + "\tacertos:" + pctAcerto + "\tmicro:" + microAverage + "\tmacro:" + macroAverage;
    }

}
