/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datamanager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.Map;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class Util {

    public static List<String> filePaths = new ArrayList<>();

    public static List<String> lerNomeArquivos(File initialPath, int initialDepth) {
 
        
        filePaths = new ArrayList<>();
        List <String> nomeArquivos = fileTreePrinter(initialPath,initialDepth);
        return nomeArquivos;
        
    }

    public static List<String> fileTreePrinter(File initialPath, int initialDepth) {

        int depth = initialDepth++;
        if (initialPath.exists()) {
            File[] contents = initialPath.listFiles();
            for (File content : contents) {
                if (content.isDirectory()) {
                    fileTreePrinter(content, initialDepth + 1);
                } else {
                    char[] dpt = new char[initialDepth];
                    for (int j = 0; j < initialDepth; j++) {
                        dpt[j] = '+';
                    }
                    // System.out.println(new String(dpt) + content.getName() + " " + content.getPath() );
                    //System.out.println(content.toString());
                    if (content.getName().contains(".txt")) {
                        filePaths.add(content.toString());
                    }

                }
            }
        }
        return filePaths;
    }

    // public static String lerArquivo(String filePath) throws FileNotFoundException, IOException {
    //     StringBuilder linha = new StringBuilder();
    //     try (FileReader fr = new FileReader(filePath); BufferedReader br = new BufferedReader(fr)) {
    //         while (br.ready()) {
    //             linha.append(br.readLine());
    //             linha.append("\n");
    //         }
    //         br.close();
    //         fr.close();
    //     }
    //     return linha.toString();
    // }

   

    // public static void printFile(String fileName, String texto) throws IOException {
    //     try (FileWriter fw = new FileWriter(fileName); BufferedWriter bw = new BufferedWriter(fw)) {
    //         bw.write(texto);
    //         bw.newLine();
    //         bw.close();
    //         fw.close();
    //     }
    // }

}
