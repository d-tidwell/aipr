/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package aipr;

import java.io.IOException;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;


public class App {


    public static void main(String[] args) throws IOException{
        OpenAiServe runner = new OpenAiServe();
        runner.addToMap();
        Map<String, LinkedList<String>> results = runner.getResultsMap();
        for(String s: results.keySet()) {
            System.out.println("---------------------------------------");
            System.out.println("\n" + s);
            for(String diffed: results.get(s)) {
                System.out.println(diffed);
            }
            System.out.println("---------------------------------------");
        }

    }
}
