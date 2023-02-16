package aipr;

import com.theokanning.openai.completion.CompletionChoice;
import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

import java.net.SocketTimeoutException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class OpenAiServe {

    public void makeRequest(String prompt_code) throws SocketTimeoutException {
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

        System.out.println("RAW PROMPT COUNT" + prompt_build.toString().length());

        int estimated_token_count = (int)((prompt_build.toString().length() * 0.3924));
        if (estimated_token_count + 500 > 3596) {
            System.out.println("Prompt size to large to complete request");
            return;
        }
        //System.out.println(newPrompt.length() + "    XXXXXXXXXXXXXXXXXXXXXXXPost Prompt Count");
        System.out.println(
                "Creating completion..." + "prompt length in tokens before ~ (" +
                        estimated_token_count + ") + words ("+ prompt_build.toString().length()+") \n\n ");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt(prompt_build.toString())
                .echo(false)
                .user("testing")
                .maxTokens(250)
                .build();


        //return first result
        try {
            CompletionChoice completion = service.createCompletion(completionRequest).getChoices().get(0);

            System.out.println("---------------------------------------------------------------------------");
            System.out.println(completion);
//            System.out.println(
//                    completion.toString().substring(completion.toString().indexOf("text="+1),completion.toString().indexOf("index=0")-3));
            System.out.println("---------------------------------------------------------------------------\n\n");
        } catch (RuntimeException RE) {
            System.out.println(RE.getCause());
            System.out.println("Model timeout request - skipping result");
            System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!\n\n");
        }

    }

}
