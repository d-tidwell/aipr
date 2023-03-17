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
        prompt_build.append("Reason each bullet point interpolating not just what the changes are but why they were made.");
        prompt_build.append("Code Change Summary:");

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
    public void addToMap() throws IOException, InterruptedException {
        OpenAiServe ai = new OpenAiServe();
        //find the commit file and extract commits into a map
        CExtractor.extractcimmit();
        Map<String, ArrayList<String>> map = CExtractor.cimmitMap;
        String numberOfCommits = String.valueOf(map.size());
        resultsMap.put("totalNumberOfCommits", new LinkedList<>(Collections.singletonList(numberOfCommits)));
        System.out.print("Creating comments ...");

        List<Thread> threads = new ArrayList<>();
        for (String x : map.keySet()) {
            resultsMap.put(x, new LinkedList<>());

            for (int i = 0; i < map.get(x).size(); i++) {
                String commit = map.get(x).get(i);
                LinkedList<String> existing = resultsMap.get(x);

                // create a thread for each commit
                Thread thread = new Thread(() -> {
                    if (commit.contains("initial commit")) {
                        existing.addLast("ERROR: CONTAINS \"initial commit\" BANNED PHRASE");
                        System.out.print("X");
                        return;
                    }
                    if (commit.contains("diff")) {
                        existing.addLast("ERROR: CONTAINS \"diff\" BANNED WORD");
                        System.out.print("X");
                        return;
                    }
                    if (commit.contains("@@")) {
                        existing.addLast("ERROR: CONTAINS \"@@\" BANNED PHRASE");
                        System.out.print("X");
                        return;
                    }
                    // make the actual request to openai for comment
                    String completion = null;
                    try {
                        completion = ai.makeRequest(x, commit);
                    } catch (SocketTimeoutException e) {
                        existing.addLast(e.getMessage());
                        System.out.print("X");
                    }
                    if (completion.contains("SERVICE_ERROR_CAUSE:")) {
                        existing.addLast(completion);
                        System.out.print("X");
                    }

                    if (completion.isEmpty()) {
                        existing.addLast(
                                "ERROR: NULL or EMPTY STRING COMPLETION FOR:" + commit.substring(0, 25));
                        System.out.print("X");
                    } else {
                        existing.addLast(completion);
                        System.out.print("♥");
                    }
                });
                thread.start();
                threads.add(thread);
            }

            // wait for all threads to finish
            for (Thread thread : threads) {
                thread.join();
            }

        }
        System.out.println("\n");
    }


    public Map<String, LinkedList<String>> getResultsMap() {
        return resultsMap;
    }
}
