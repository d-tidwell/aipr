package aipr;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class OpenAiServe {

    private Map<String, LinkedList<String>> resultsMap;

    private String SOURCEDIR = "~/aipr/tmprqpr/";

    public OpenAiServe(){
        this.resultsMap = new HashMap<>();
    }

    /**
     This method makes a request to the OpenAI service using the OpenAiService class to generate a comment for a commit with
     a given commitId and prompt code. The method constructs a prompt to be used for the completion request, which includes
     the code prompt trimmed of new lines at the end or beginning, and additional instructions for summarizing the changes
     made to the code. The method also performs some error handling, such as checking the size of the prompt before making
     the request and logging errors if the service returns an error. The completed comment is returned as a string.
     @param commitId the ID of the commit to generate a comment for
     @param prompt_code the prompt code to use for generating the comment
     @return the completed comment as a string
     @throws SocketTimeoutException if the request to the OpenAI service times out
     */
    public String makeRequest(String commitId, String prompt_code) throws SocketTimeoutException {
        //String token = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(API_KEY.KEYS, Duration.ofSeconds(25));

        StringBuilder prompt_build = new StringBuilder();
        //add the code prompt trimmed of new lines at the end or beginning to help model completion
        prompt_build.append(prompt_code.trim());
        //add the prefilled prompt for completion
        prompt_build.append("Summarize the changes to the code above.\n");
        prompt_build.append("Lines in the above code starting with + are additions to the code.\n");
        prompt_build.append("Lines in the above code starting with - are lines removed from the code.\n");
        prompt_build.append("Bullet point all changes of importance in the code, being as clear and brief as possible.\n");
        prompt_build.append("Reason each bullet point interpolating not just what the changes are but why.");
        prompt_build.append("Code Summary:");

        int estimated_token_count = (int)((prompt_build.toString().length() * 0.3924));
        if (estimated_token_count + 500 > 3596) {
            return "ERROR: Prompt size to large to complete request for file in:  " + commitId;
        }
        List<ChatMessage> messages = new ArrayList<>();
        ChatMessage systemMessage = new ChatMessage(
                ChatMessageRole.SYSTEM.value(),
                "You are a helpful advanced semantic comprehension and conversational learning model tasked with summarizing code changes.");
                messages.add(systemMessage);
        ChatMessage userMessage = new ChatMessage(ChatMessageRole.USER.value(), prompt_build.toString());
                messages.add(userMessage);

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .logitBias(new HashMap<>())
                .build();

        //return first result
        try {
            String completion = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage().getContent();
            return completion;
        } catch (RuntimeException RE) {
            return "SERVICE_ERROR_CAUSE: " + commitId + ":" + RE.getCause();
        }

    }
    /**
     This method is used to extract commits from files located at "~/aipr" into a map, and then generates comments
     for each commit using the OpenAiServe class. It also performs some error handling such as checking for banned phrases
     in the commit message and logging errors if the OpenAI service returns an error. The comments are added to a map
     called 'resultsMap'.
     @throws IOException if an I/O error occurs while extracting the commits from the files
     */
    public void addToMap() throws IOException {
        OpenAiServe ai = new OpenAiServe();
        //find the commit file and extract commits into a map
        CExtractor.extractcimmit();
        Map<String, ArrayList<String>> map = CExtractor.cimmitMap;
        String numberOfCommits = String.valueOf(map.size());
        resultsMap.put("totalNumberOfCommits", new LinkedList<>(Collections.singletonList(numberOfCommits)));
        System.out.print("Creating comments ...");
        for(String x: map.keySet()) {
            resultsMap.put(x,new LinkedList<>());
            int diffErrorCount = 0;
            int serviceErrorCount =0;
            for(int i=0; i < map.get(x).size(); i++) {

                LinkedList<String> existing = resultsMap.get(x);

                if (map.get(x).get(i).contains("initial commit")) {
                    existing.addLast("ERROR: CONTAINS \"initial commit\" BANNED PHRASE");
                    diffErrorCount += 1;
                    System.out.print("X");
                    continue;
                }
                if (map.get(x).get(i).contains("diff")) {
                    existing.addLast("ERROR: CONTAINS \"diff\" BANNED WORD");
                    diffErrorCount += 1;
                    System.out.print("X");
                    continue;
                }
                if (map.get(x).get(i).contains("@@")) {
                    existing.addLast("ERROR: CONTAINS \"@@\" BANNED PHRASE");
                    diffErrorCount += 1;
                    System.out.print("X");
                    continue;
                }
                //make the actual request to openai for comment
                String completion =  ai.makeRequest(x, map.get(x).get(i));
                if (completion.contains("SERVICE_ERROR_CAUSE:")) {
                    existing.addLast(completion);
                    diffErrorCount += 1;
                    System.out.print("X");
                }

                if (completion.isEmpty()) {
                    existing.addLast(
                            "ERROR: NULL or EMPTY STRING COMPLETION FOR:" + map.get(x).get(i).substring(0,25));
                    diffErrorCount += 1;
                    System.out.print("X");
                } else {
                    existing.addLast(completion);
                    System.out.print("???");
                }
            }
            //log diff errors in last index or first if 0 size

            resultsMap.get(x).addLast("DIFF ERRORS = " + String.valueOf(diffErrorCount));
            resultsMap.get(x).addLast("SERVICE ERRORS = " + String.valueOf(serviceErrorCount));
        }
        System.out.println("\n");

    }

    public Map<String, LinkedList<String>> getResultsMap() {
        return resultsMap;
    }
}
