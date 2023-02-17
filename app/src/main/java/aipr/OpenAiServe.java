package aipr;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

import javax.annotation.processing.Completion;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OpenAiServe {

    private Map<String, LinkedList<String>> resultsMap;

    public OpenAiServe(){
        this.resultsMap = new HashMap<>();
    }

    public String makeRequest(String commitId, String prompt_code) throws SocketTimeoutException {
        //String token = System.getenv("OPENAI_TOKEN");
        //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
        OpenAiService service = new OpenAiService(
                "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", Duration.ofSeconds(25));

        StringBuilder prompt_build = new StringBuilder();
        //add the code prompt trimmed of new lines at the end or beginning to help model completion
        prompt_build.append(prompt_code);
        //add the prefilled prompt for completion
        prompt_build.append("Summarize the changes to the above code.");
        prompt_build.append("The lines in the above code starting with + are additions to the code.");
        prompt_build.append("The lines in the above code starting with - are lines removed from the code.");
        prompt_build.append("Use Bullet points to communicate changes, being as clear and brief as possible.");
        prompt_build.append("Comment:");

        int estimated_token_count = (int)((prompt_build.toString().length() * 0.3924));
        if (estimated_token_count + 500 > 3596) {
            return "ERROR: Prompt size to large to complete request for file in:  " + commitId;
        }
        //System.out.println(newPrompt.length() + "    XXXXXXXXXXXXXXXXXXXXXXXPost Prompt Count");
        //System.out.println(
//                "Creating completion..." + "prompt length in tokens before ~ (" +
//                        estimated_token_count + ") + words ("+ prompt_build.toString().length()+") \n\n ");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt(prompt_build.toString())
                .echo(false)
                .user("testing")
                .maxTokens(250)
                .build();

        //return first result
        try {
            String completion = service.createCompletion(completionRequest).getChoices().get(0).getText();
            return completion;
        } catch (RuntimeException RE) {
            return "SERVICE_ERROR_CAUSE: " + commitId + ":" + RE.getCause();
        }

    }

    public void addToMap() throws IOException {
        OpenAiServe ai = new OpenAiServe();
        //find the commit file and extract commits into a map
        CExtractor.extractcimmit("/mnt/c/code/");
        Map<String, ArrayList<String>> map = CExtractor.cimmitMap;
        String numberOfCommits = String.valueOf(map.size());
        resultsMap.put("totalNumberOfCommits", new LinkedList<>(Collections.singletonList(numberOfCommits)));
        System.out.print("Creating comments ...");
        for(String x: map.keySet()) {
            resultsMap.put(x,new LinkedList<>());
            int diffErrorCount = 0;
            for(int i=0; i < map.get(x).size(); i++) {

                if (map.get(x).get(i).contains("initial commit")) {
                    LinkedList<String> existing = resultsMap.get(x);
                    existing.addFirst("ERROR: CONTAINS \"initial commit\" BANNED PHRASE");
                    diffErrorCount += 1;
                    System.out.print("X");
                    continue;
                }
                if (map.get(x).get(i).contains("diff")) {
                    LinkedList<String> existing = resultsMap.get(x);
                    existing.addFirst("ERROR: CONTAINS \"diff\" BANNED WORD");
                    diffErrorCount += 1;
                    System.out.print("X");
                    continue;
                }
                if (map.get(x).get(i).contains("@@")) {
                    LinkedList<String> existing = resultsMap.get(x);
                    existing.addFirst("ERROR: CONTAINS \"@@\" BANNED PHRASE");
                    diffErrorCount += 1;
                    System.out.print("X");
                    continue;
                }
                //make the actual request to openai for comment
                String completion =  ai.makeRequest(x, map.get(x).get(i));
                if (completion.contains("SERVICE_ERROR_CAUSE:")) {
                    LinkedList<String> existing = resultsMap.get(x);
                    existing.addFirst(completion);
                    diffErrorCount += 1;
                    System.out.print("X");
                }
                LinkedList<String> existing = resultsMap.get(x);

                if (completion.isEmpty()) {
                    existing.addFirst("ERROR: NULL or EMPTY STRING COMPLETION FOR:" + map.get(x).get(i).substring(0,25));
                    diffErrorCount += 1;
                    System.out.print("X");
                } else {
                    existing.addFirst(completion);
                    System.out.print("♥");
                }
            }

            //log diff errors in last index or first if 0 size

            resultsMap.get(x).addLast("DIFF ERRORS = " + String.valueOf(diffErrorCount));

        }
        System.out.println("\n");
        //want to ratio this to enums of outcomes for better user message
        System.out.println("Not Bad - Marginal Success, review comments and counts, Have a nice day.");
    }

    public Map<String, LinkedList<String>> getResultsMap() {
        return resultsMap;
    }
}
