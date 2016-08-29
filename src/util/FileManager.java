/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author charleshenriqueportoferreira
 */
public class FileManager {

    public static List<String> filePaths = new ArrayList<>();

   

    public void saveFile(String fileName, String texto) throws IOException {
        try (FileWriter fw = new FileWriter(fileName); BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(texto);
            bw.close();
            fw.close();
        }
    }

    public void limpaDados() {
        String diretorio = System.getProperty("user.dir");
        fileTreePrinter(new File(diretorio), 0);
        for (String filePath : filePaths) {
            deletaArquivoExistente(filePath);
        }
    }

    public void deletaArquivoExistente(String nomeArquivo) {
        File f = new File(nomeArquivo);
        if (f.exists()) {
            f.delete();
        }
    }

    public List<String> fileTreePrinter(File initialPath, int initialDepth) {

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
                    if (content.getName().matches("Dados[0-9]+\\.arff")) {
                        filePaths.add(content.toString());
                    }

                }
            }
        }
        return filePaths;
    }

}
