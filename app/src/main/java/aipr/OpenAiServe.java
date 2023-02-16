package aipr;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

import java.net.SocketTimeoutException;

public class OpenAiServe {

    public void makeRequest(String prompt_code) throws SocketTimeoutException {
        //String token = System.getenv("OPENAI_TOKEN");
        //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
        OpenAiService service = new OpenAiService("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");
        StringBuilder prompt_build = new StringBuilder();

        prompt_build.append("Summarize of the changes to the following code.");
        prompt_build.append("The lines starting with + are additions to the code.");
        prompt_build.append("The lines starting with - are lines removed from the code.");
        //prompt_build.append("Infer why the changes were made and explain any necessary details.");
        prompt_build.append("Use Bullet point to summarize changes, being as clear and brief as possible.");
        prompt_build.append("CODE:");

        String cleaned_prompt =prompt_code.trim();
        prompt_build.append(cleaned_prompt);
        prompt_build.append("\n");

        //System.out.println(newPrompt.length() + "    XXXXXXXXXXXXXXXXXXXXXXXPost Prompt Count");
        System.out.println("\nCreating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt(prompt_build.substring(prompt_code.indexOf("@@")))
                .echo(false)
                .user("testing")
                .maxTokens(500)
                .build();


        //return first result

        CompletionChoice completion = service.createCompletion(completionRequest).getChoices().get(0);
        System.out.println(completion);

    }

}
