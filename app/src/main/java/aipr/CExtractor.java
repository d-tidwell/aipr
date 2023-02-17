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

        //parses text file to extract the actual code changes foregoing the import statements for now
        for(String s: cimmitArr){
            String keyS = s.substring(0, cimmitCount+1).strip();
            cimmitMap.put(keyS, new ArrayList<>());
            List<String> diffs = getSubstrings(s, "diff");
            for(String d: diffs) {
                if (d.contains("@@")) {
                    String atsString = d.substring(d.indexOf("@@"), d.length()-1);
                    int index1 = atsString.indexOf("@@");
                    int index2 = atsString.indexOf("@@", index1+1);
                    int index3 = atsString.indexOf("@@", index2+1);
                    int index4 = atsString.indexOf("@@", index3+1);
                    ArrayList<String> atsList = cimmitMap.get(keyS);
                    atsList.add(atsString.substring(index4+2, atsString.length()-1));
                }
            }
        }
    }

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
