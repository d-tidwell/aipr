/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package aipr;

import java.io.IOException;

import java.util.LinkedList;
import java.util.Map;


public class App {

    public static void main(String[] args) throws IOException{
        OpenAiServe runner = new OpenAiServe();
        runner.addToMap();
        Map<String, LinkedList<String>> results = runner.getResultsMap();
        JSON_result.saveMapToJson(results, "pr_summary.json");
        for (String cimmit: results.keySet()) {
            System.out.println("[][][][][][][][][][][][][][][][][][][][][][][][][]");
            System.out.println("**************************************************");
            System.out.println(cimmit);
            System.out.println("**************************************************");
            for(String gens: results.get(cimmit)) {
                System.out.println("-----------------------------------------------");
                System.out.println(gens);
            }
        }
        //feature to create ratio this to enums of outcomes for better user message
        System.out.println("Not Bad - Marginal Success, review comments and counts, Have a nice day.");

    }
}
