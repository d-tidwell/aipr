package aipr;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

import javax.annotation.processing.Completion;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class OpenAiServe {

    private Map<String,ArrayList<String>> resultsMap;

    public OpenAiServe(){
        this.resultsMap = new HashMap<>();
    }

    public String makeRequest(String commitId, String prompt_code) throws SocketTimeoutException {
        //String token = System.getenv("OPENAI_TOKEN");
        //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
        OpenAiService service = new OpenAiService(
                "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", Duration.ofSeconds(25));

        StringBuilder prompt_build = new StringBuilder();
        //add the code prompt trimmed of new lines at the end or beginning
        prompt_build.append(prompt_code);
        //add the prefilled prompt for completion
        prompt_build.append("Summarize of the changes to the above code.");
        prompt_build.append("The lines starting with + are additions to the code.");
        prompt_build.append("The lines starting with - are lines removed from the code.");
        //prompt_build.append("Infer why the changes were made and explain any necessary details.");
        prompt_build.append("Use Bullet points to summarize changes, being as clear and brief as possible.");
        prompt_build.append("Comment:");

        //System.out.println("RAW PROMPT COUNT" + prompt_build.toString().length());

        int estimated_token_count = (int)((prompt_build.toString().length() * 0.3924));
        if (estimated_token_count + 500 > 3596) {
            System.out.println("Prompt size to large to complete request:  " + commitId);
            return null;
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
            System.out.println(commitId);
            System.out.println("ERROR" + RE.getCause());
            return null;
        }

    }

    public void addToMap() throws IOException {
        OpenAiServe ai = new OpenAiServe();
        CExtractor.extractcimmit("/mnt/c/code/");
        Map<String, ArrayList<String>> map = CExtractor.cimmitMap;
        String numberOfCommits = String.valueOf(map.size());
        for(String x: map.keySet()) {
            resultsMap.put(x,new ArrayList<>());
            for(int i=0; i < map.get(x).size(); i++) {
                if (map.get(x).get(i).contains("initial commit")) {
                    continue;
                }
                String completion =  ai.makeRequest(x, map.get(x).get(i));
                if(Objects.isNull(completion)){
                    continue;
                } else {
                    ArrayList<String> existing = resultsMap.get(x);
                    existing.add(completion);
                }
            }

        }
        System.out.println("Success, all changes have been commented, Have a nice day.");
    }

    public Map<String, ArrayList<String>> getResultsMap() {
        return resultsMap;
    }
}
