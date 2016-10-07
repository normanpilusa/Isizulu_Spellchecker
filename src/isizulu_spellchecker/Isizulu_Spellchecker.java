/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package isizulu_spellchecker;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Isizulu_Spellchecker {

    public static void main(String[] args) throws IOException {
        
        Spellchecker.main(args);
//        try{
//        ClassLoader loader =Isizulu_Spellchecker.class.getClassLoader();
//        InputStream file = Isizulu_Spellchecker.class.getResourceAsStream("resources/wordlist.txt");
//        BufferedReader r = new BufferedReader(new InputStreamReader(file));
//
//         // reads each line
//         String l;
//         while((l = r.readLine()) != null) {
//            System.out.println(l);
//            break;
//         } 
//         file.close();
//        
//        }catch(FileNotFoundException e){
//            System.out.println("File not found");
//        }
//        System.out.print("Enter a sentence to spellcheck right here => ");
//        Scanner scn = new Scanner(System.in);
//        String searchstring, input = scn.nextLine();
//        long lStartTime = System.currentTimeMillis();
//
//        String misW = preprocessin(input);
//        if (misW.equals("")) {
//            System.out.println("All words were correctly spelled");
//        } else {
//            StringTokenizer ErrorDWords = new StringTokenizer(misW);
//            int numberoftokens = ErrorDWords.countTokens();
//            for (int i = 0; i < numberoftokens; i++) {
//                searchstring = ErrorDWords.nextToken();
//                boolean check = errordetection(searchstring);
//                if (check) {
//                    System.out.println(searchstring + " is correctly spelled");
//                } else {
//                    System.out.println(searchstring + " is wrongly spelled");
//                }
//
//            }
//        }
//        long lEndTime = System.currentTimeMillis();
//        long difference = lEndTime - lStartTime;
//        System.out.println("Elapsed milliseconds: " + difference);
//        scn.close();

    }

    // Preprocesssing algorithm
    public static String preprocessin(String str) throws IOException {
        String searchstring, store;
        StringBuilder builder = new StringBuilder();
        StringTokenizer sTok = new StringTokenizer(str.toUpperCase());

        //try {
        int numberoftokens = sTok.countTokens();
        for (int i = 0; i < numberoftokens; i++) {
            searchstring = sTok.nextToken();
            int count = search(searchstring);//,
            //"C:/Users/Norman_P/Desktop/balone/ukwebalanacorpus.txt");
            if (count == 0) {
                builder.append(searchstring);
                builder.append(" ");
            }
        }
        //} catch (FileNotFoundException e) {
        //    System.err.format("File does not exist\n");
        //}
        store = builder.toString();
        return store;
    }

    // Searches for a string in a text file
    public static int search(String search) {
        int count = 0;

        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(
                    "jdbc:postgresql://localhost:5432/postgres", "pilusa",
                    "iamtheonly1whoknowsit");
            Statement st = conn.createStatement();
            ResultSet val = st.executeQuery("SELECT * FROM corpus WHERE word= '" + search + "'");

            while (val.next()) {
                count += val.getInt("frequency");
            }

        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Isizulu_Spellchecker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return count;
    }

    // Error detection algorithm
    public static boolean errordetection(String str) throws IOException {
        String searchstring;
        boolean found = false;
        int fre = 0;
        double calFre, threshold = 0.003;
        int totalW = 196589;

        if (str.length() < 3) {
            found = true;
        } else {

            StringTokenizer sTok = new StringTokenizer(str.toUpperCase());
            int numberoftokens = sTok.countTokens();
            try {
                Class.forName("org.postgresql.Driver");
                try (Connection conn = DriverManager.getConnection(
                        "jdbc:postgresql://localhost:5432/postgres", "pilusa",
                        "iamtheonly1whoknowsit")) {
                    Statement st = conn.createStatement();
                    for (int i = 0; i < numberoftokens; i++) {
                        searchstring = sTok.nextToken();
                        int pos = 0;
                        while (pos < searchstring.length()) {
                            str = searchstring.substring(pos, pos + 3);
                            ResultSet val = st
                                    .executeQuery("SELECT * FROM corpus WHERE word= '"
                                            + str + "'");

                            while (val.next()) {
                                fre += val.getInt("frequency");
                            }
                            calFre = (double) fre / (double) totalW;
                            if (calFre < threshold) {
                                found = false;
                                pos = searchstring.length();
                                i = numberoftokens;
                            } else {
                                found = true;

                            }

                            if (pos + 3 >= searchstring.length()) {
                                pos = searchstring.length();
                            }
                            pos++;
                        }

                    }
                }
            } catch (ClassNotFoundException | SQLException e) {
            }
        }
        return found;
    }

}
