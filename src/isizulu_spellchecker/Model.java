/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isizulu_spellchecker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Norman_P
 */
public class Model {

    public HashMap<String, Integer> trigramMap = new HashMap<>();
    private final HashSet<String> wordlist = new HashSet<>();
    public HashSet<String> dictionary = new HashSet<>(); //Stores words added by the user
    private String dictDatabas = ""; //to make writing back to file easier

    public Model() {
        initialize();
    }

    /*
     * Checks word in wordlist before doing error detection
     * returns true if correct
     */
    public boolean check(String str) {
        if (search(str)) {
            //finds word in wordlist or dictionary
            return true;
        } else {
            //uses trigrams for a new word
            return errorDetection(str);
        }
    }

    /*
     * Searches for a word from wordlist
     * returns true if word is in the wordlist
     */
    private boolean search(String word) {
        return wordlist.contains(word) || dictionary.contains(word);
    }

    /*
     * Detects the errors of a word
     * returns: true if error was detected
     */
    private boolean errorDetection(String word) {
        //Get trigrams of the word
        ArrayList<String> trigrams = wordTrigram(word);
        boolean error = false;
        int fre = 0;
        double probability, threshold = 0.003;
        int totalWords = trigramMap.size(); //Needs to be fixed

        //calculate the probability of each trigram and check for correctness
        for (String trigram : trigrams) {
            probability = (double) getFrequency(trigram) / (double) totalWords;
            if (probability < threshold) {
                error = false; //the trigam is incorrect
                break; //No neeed to continue iterations
            } else {
                error = true;
            }
        }
        return error;
    }

    /*
     * Reads a word word
     * returns: a list of trigrams
     */
    private ArrayList<String> wordTrigram(String word) {

        ArrayList<String> strTri = new ArrayList<>();
        int pos = 0;
        while (pos < word.length()) {
            if ((pos + 3) >= word.length()) {
                strTri.add(word.substring(pos, word.length()));
                pos = word.length();
            } else {
                strTri.add(word.substring(pos, pos + 3));
                pos = pos + 1;
            }
        }
        return strTri;
    }

    /*
     * Initializes wordlist, loads from text file into a hashset for quick access
     */
    private void initialize() {
        try {
            InputStream wdlist = Isizulu_Spellchecker.class.getResourceAsStream("resources/wordlist.txt");
            InputStream dict = Isizulu_Spellchecker.class.getResourceAsStream("resources/dictionary.txt");
            InputStream tri = Isizulu_Spellchecker.class.getResourceAsStream("resources/trigrams.txt");

            BufferedReader wdReader = new BufferedReader(new InputStreamReader(wdlist));
            BufferedReader dictReader = new BufferedReader(new InputStreamReader(dict));
            BufferedReader trigramReader = new BufferedReader(new InputStreamReader(tri));

            // Load user dictionary
            String line = dictReader.readLine();
            while (line != null) {
                dictionary.add(line.trim());
                dictDatabas += line.trim();
                line = dictReader.readLine();
            }
            dict.close();

            //Load the wordlist
            line = wdReader.readLine();
            while (line != null) {
                wordlist.add(line.trim());
                line = wdReader.readLine();
            }
            wdlist.close();

            //Load the trigram
            line = trigramReader.readLine();
            String[] entry;
            while (line != null) {
                entry = line.split(" ");//trigram and frequency
                trigramMap.put(entry[0], Integer.parseInt(entry[1]));
                line = trigramReader.readLine();
            }
            tri.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /*
     * Gets the frequency of a trigram from a storage
     * return: frequency of trigram
     */
    private int getFrequency(String trigram) {
        if (trigramMap.containsKey(trigram)) {
            return trigramMap.get(trigram);
        } else {
            return 0;
        }
    }

    /*
     * Adds a new User word to user's personal dictionary
     */
    public void addWord(String word) {
        FileWriter fw = null;
        try {
            String wdlist = Isizulu_Spellchecker.class.getResource("resources/dictionary.txt").getFile();
            File file = new File(wdlist);
            //String content = new FileReader(file.getAbsoluteFile()).r;
            fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            //updates the content of the user dictionary
            dictionary.add(word);
            dictDatabas += "\n"+word;
            bw.write(dictDatabas);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                fw.close();
            } catch (IOException ex) {
                Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}