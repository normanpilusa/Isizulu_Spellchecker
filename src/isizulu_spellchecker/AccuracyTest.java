/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isizulu_spellchecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;

/**
 *
 * @author Norman_P
 */
public class AccuracyTest {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        HashSet<String> dictionary = new HashSet<>();
        try {
            Model m = new Model();
            double correct = 0;
            double incorrect = 0;
            InputStream wdlist = Isizulu_Spellchecker.class
                    .getResourceAsStream("resources/AccuracyTestWords.txt");
            BufferedReader wdReader = new BufferedReader(new InputStreamReader(
                    wdlist));
            // Load the wordlist
            String line = wdReader.readLine();
            while (line != null) {
                if (!dictionary.contains(line)) {
                    dictionary.add(line);
                }
                line = wdReader.readLine();
            }
            wdlist.close();
            
            try {
                File unique = new File("unique.txt");
                File corr = new File("correct.txt");
                File incorr = new File("incorrect.txt");

                // if file doesnt exists, then create it
                if (!unique.exists()) {
                    unique.createNewFile();
                }
                if (!corr.exists()) {
                    corr.createNewFile();
                }
                if (!incorr.exists()) {
                    incorr.createNewFile();
                }

                FileWriter ufw = new FileWriter(unique.getAbsoluteFile());
                BufferedWriter bw = new BufferedWriter(ufw);

                FileWriter cfw = new FileWriter(corr.getAbsoluteFile());
                BufferedWriter cbw = new BufferedWriter(cfw);

                FileWriter ifw = new FileWriter(incorr.getAbsoluteFile());
                BufferedWriter ibw = new BufferedWriter(ifw);
                
                bw.write("Total unique words:" + dictionary.size() + '\n');
                for (String word : dictionary) {
                    bw.write(word + '\n');
                    if (m.check(word)) {
                        correct += 1;
                        cbw.write(word+'\n');
                    } else {
                        incorrect += 1;
                        ibw.write(word+'\n');
                    }
                }
                bw.close();
                cbw.close();
                ibw.close();

                System.out.println("Total unique words: " + dictionary.size());
            System.out.println("Predicted to be correct: " + correct);
            System.out.println("Predicted to be incorrect: " + incorrect);
            System.out.println("Accuracy is: " + (correct / (correct + incorrect)) * 100);

            } catch (IOException e) {
                e.printStackTrace();
            }

        } catch (IOException e) {

        }

    }
}
