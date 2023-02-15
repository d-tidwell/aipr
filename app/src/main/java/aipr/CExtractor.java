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
    public static Map<String, ArrayList<String>> commitMap= new HashMap<>();

    public static void extractCommit(String filepath) throws IOException {
        BufferedReader realReader = new BufferedReader(new FileReader(filepath + "changelog.txt"));
        String line;
        String tag = "commit 5d78304b7dfc861bfd57299fc3ca1c9a04a32078";
        int commitCount = tag.length();
        Path fileName = Path.of(filepath + "changelog.txt");
        String string = Files.readString(fileName);
        List<String> commitArr = getSubstrings(string, "commit");

        for(String s: commitArr){
            String keyS = s.substring(0, commitCount+1).strip();
            commitMap.put(keyS, new ArrayList<>());
            List<String> diffs = getSubstrings(s, "diff");
            for(String d: diffs) {

                if (d.contains("@@")) {
                    //System.out.println("NEW @@");
                    String atsString = d.substring(d.indexOf("@@"), d.length()-1);
                    int index1 = atsString.indexOf("@@");
                    int index2 = atsString.indexOf("@@", index1+1);
                    //System.out.println(atsString.substring(index2+2, atsString.length()-1));
                    ArrayList<String> atsList = commitMap.get(keyS);
                    atsList.add(atsString.substring(index2+2, atsString.length()-1));
                }

            }
        }
//        for (ArrayList<String> result : commitMap.values()){
//            for (String these: result) {
//                System.out.println(these.length());
//            }
//
//        }
        //System.out.println(commitMap);

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
