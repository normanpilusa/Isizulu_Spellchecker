/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isizulu_spellchecker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Norman_P
 */
public class CorpusProcessor {

    public static void main(String[] arg) {
        try {
            ArrayList<File> file = listFilesForFolder(new File("C:\\Users\\Norman_P\\Desktop\\IsiZulu\\Novels Compressed (1)\\Novels Compressed"));
            HashMap<String, Integer> corp = new HashMap();// stores word, frequency
            
            Scanner kb = new Scanner(file.get(0));
            String input = kb.next();
                    
            while(kb.hasNext()){
                //System.out.println(input.trim());
                if(corp.containsKey(input)){
                    corp.replace(input, corp.get(input), corp.get(input)+1);
                }
                else{
                    corp.put(input, 1);
                }
                input = kb.next();
            }
            //System.out.println(kb.hasNext());
            
            //print items in the corpus to see
            Set keys = corp.keySet();

   for (Iterator i = keys.iterator(); i.hasNext(); ) {
       String key = (String) i.next();
       System.out.printf("%s = %s\n",key,corp.get(key));
   }
            
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CorpusProcessor.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    /*
    Reads files from a directory and returns a list with aall the file names
    */
    public static ArrayList listFilesForFolder(final File folder) {

        ArrayList<File> files = new ArrayList<>();
        
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                files.add(new File(fileEntry.getAbsolutePath()));
            }
        }
        return files;
    }
}
