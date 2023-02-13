package aipr;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

public class OpenAiServe {

    public void makeRequest(prompt_code) {
        String token = System.getenv("OPENAI_TOKEN");
        OpenAiService service = new OpenAiService(token);
        StringBuilder prompt_build = new StringBuilder();
        prompt_build.append("The following is a block of code.");
        prompt_build.append("The lines starting with - are lines removed from the code.");
        prompt_build.append("The lines starting with + are additions to the code.");
        prompt_build.append("Make a github pull request style summary of the changes.");
        prompt_build.append("Infer why the changes were made and explain any necessary details.");
        prompt_build.append("Bullet point each summary being as clear and brief as possible about the changes.\n");
        prompt_build.append("CODE:\n");
        prompt_build.append(prompt_code);
        System.out.println("\nCreating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt(prompt_build.toString())
                .echo(true)
                .user("testing")
                .maxTokens(4000)
                .build();
        service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
    }

}
