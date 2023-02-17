package aipr;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CExtractor {
    public static Map<String, ArrayList<String>> cimmitMap= new HashMap<>();

    /**
     Extracts commits and code changes from a text file and populates them in a HashMap.
     The method takes a file path string and reads the file as a string. It then extracts all the commit ids from the file,
     uses a helper function to parse the text file and extract the actual code changes, and stores them in a HashMap.
     The commit ids are used as keys in the HashMap, and the corresponding code changes are stored as values in ArrayLists.
     @param filepath a string representing the path of the file to be read
     @throws IOException if there is an error reading the file
     @return void
     **/
    public static void extractcimmit(String filepath) throws IOException {
        //bring in the file
        BufferedReader realReader = new BufferedReader(new FileReader(filepath + "changefile.txt"));

        //string example of commit id to extract commit + hash number length for map key
        String tag = "cimmit 5d78304b7dfc861bfd57299fc3ca1c9a04a32078";
        int cimmitCount = tag.length();

        //read the file as a string
        Path fileName = Path.of(filepath + "changefile.txt");
        String string = Files.readString(fileName);

        //calls helper function to get a list of Srings of all commits
        List<String> cimmitArr = getSubstrings(string, "commit");

        //parses text file to extract the actual code changes foregoing 1st @@ -->@@ usually the import statements
        //due to the token limitation and parsing challenge
        for(String s: cimmitArr){
            String keyS = s.substring(0, cimmitCount+1).strip();
            cimmitMap.put(keyS, new ArrayList<>());
            List<String> diffs = getSubstrings(s, "diff");
            for(String d: diffs) {
                if (d.contains("@@")) {
                    String atsString = d.substring(d.indexOf("@@"), d.length()-1);
                    int index1 = atsString.indexOf("@@");
                    int index2 = atsString.indexOf("@@", index1+2);
                    int index3 = atsString.indexOf("@@", index2+2);
                    if(index3 == -1) {
                        ArrayList<String> atsList = cimmitMap.get(keyS);
                        atsList.add(atsString.substring(index2+2, atsString.length()-1));
                    } else {
                        int index4 = atsString.indexOf("@@", index3 + 1);
                        ArrayList<String> atsList = cimmitMap.get(keyS);
                        atsList.add(atsString.substring(index4 + 2, atsString.length() - 1));
                    }
                }
            }
        }
    }
    /**
     Returns an ArrayList of all substrings of a given input string that occur between instances of a specified word.
     The method takes an input string and a word, and then iterates over the input string to find all instances of the
     word. For each instance of the word, it extracts the substring that occurs between the current and next instance of
     the word, and adds this substring to an ArrayList. The final substring in the input string is added to the ArrayList
     after the loop ends. The ArrayList of substrings is then returned.
     @param input a string representing the input text to be searched
     @param word a string representing the word to be used as a separator
     @return an ArrayList of strings, containing all substrings of the input string that occur between instances of the word
     */
    public static ArrayList<String> getSubstrings(String input, String word) {
        ArrayList<String> substrings = new ArrayList<>();
        int index = 0;
        while (index < input.length()) {
            int nextIndex = input.indexOf(word, index + word.length());
            if (nextIndex == -1) {
                break;
            }
            substrings.add(input.substring(index, nextIndex));
            index = nextIndex;
        }
        substrings.add(input.substring(index));
        return substrings;
    }
}
