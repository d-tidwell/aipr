package aipr;

import java.io.BufferedReader;
import java.io.File;
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
    public static String SOURCEFILE = "changefile.txt";

    /**
     Extracts commits and code changes from a text file and populates them in a HashMap.
     The method takes a file path string and reads the file as a string. It then extracts all the commit ids from the file,
     uses a helper function to parse the text file and extract the actual code changes, and stores them in a HashMap.
     The commit ids are used as keys in the HashMap, and the corresponding code changes are stored as values in ArrayLists.
     @param filepath a string representing the path of the file to be read
     @throws IOException if there is an error reading the file
     **/
    public static void extractcimmit(String filepath) throws IOException {
        //bring in the file
        System.out.println("HERE" + new File(filepath + SOURCEFILE).getAbsolutePath());
        BufferedReader realReader = new BufferedReader(new FileReader(filepath + SOURCEFILE));

        //string example of commit id to extract commit + hash number length for map key
        String tag = "cimmit 5d78304b7dfc861bfd57299fc3ca1c9a04a32078";
        int cimmitCount = tag.length();

        //read the file as a string
        Path fileName = Path.of(filepath + SOURCEFILE);
        String string = Files.readString(fileName);

        //calls helper function to get a list of Srings of all commits
        List<String> cimmitArr = getSubstrings(string, "commit");

        for(String s: cimmitArr){
            String keyS = s.substring(0, cimmitCount+1).strip();
            cimmitMap.put(keyS, new ArrayList<>());
            List<String> diffs = getSubstrings(s, "diff");
            for(String d: diffs) {
                if (d.contains("@@")) {
                    //keep track of index count
                    int end_of_ats = 0;
                    //make a list of indices
                    List<Integer> indicesAts = new ArrayList<>();
                    for (int i = 0; i < d.length() - 1; i++) {
                        if (d.charAt(i) == '@' && d.charAt(i+1) == '@') {
                            end_of_ats++;
                            indicesAts.add(i);
                        }
                    }

                    //if its not an empty list and has even number of indices
                    ArrayList<String> atsList = cimmitMap.get(keyS);
                    if (indicesAts.size() > 0 && indicesAts.size() % 2 == 0) {
                        //check off by one error here and test for completion
                        for(int y=1; y < indicesAts.size(); y += 2){

                            //if this only has one diff in the commit or
                            //if we are at the last index
                            if (indicesAts.size() == 2 || y == indicesAts.size() - 1) {
                                //just give me the rest of the string
                                atsList.add(d.substring(indicesAts.get(y)+2));
                                break;
                            }
                            //give me an odd to next even index
                            atsList.add(d.substring(indicesAts.get(y)+2, indicesAts.get(y+1)));
                        }
                    } else {
                        atsList.add("THIS FILE DIFF SHOWS NO CODE SUBSTANTIAL CHANGES OR POSSIBLE PARSING ERROR");
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
