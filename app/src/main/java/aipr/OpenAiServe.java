package aipr;

import com.theokanning.openai.OpenAiService;
import com.theokanning.openai.completion.CompletionRequest;

public class OpenAiServe {

    public void makeRequest(String prompt_code) {
        //String token = System.getenv("OPENAI_TOKEN");
        //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
        OpenAiService service = new OpenAiService("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", 55);
        StringBuilder prompt_build = new StringBuilder();
        prompt_build.append("The following is a block of code.");
        prompt_build.append("The lines starting with - are lines removed from the code.");
        prompt_build.append("The lines starting with + are additions to the code.");
        prompt_build.append("Make a github pull request style summary of the changes.");
        prompt_build.append("Infer why the changes were made and explain any necessary details.");
        prompt_build.append("Bullet point each summary being as clear and brief as possible about the changes.");
        prompt_build.append("CODE:");
        System.out.println(prompt_build.toString().length() + " !!!!!!!!!!!!!!!!!!!!!!!PrePromptCount");
        prompt_build.append(prompt_code);
        String newPrompt = prompt_build.toString();
        System.out.println(newPrompt.length() + "    XXXXXXXXXXXXXXXXXXXXXXXPost Prompt Count");
        System.out.println("\nCreating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt(newPrompt)
                .echo(false)
                .user("testing")
                .maxTokens(4000)
                .build();
        //System.out.println(newPrompt);
        //System.out.println(newPrompt.length());
        //System.out.println(completionRequest.toString());
        System.out.println(service.createCompletion(completionRequest).getChoices().get(0));
        //service.createCompletion(completionRequest).getChoices().forEach(System.out::println);
    }
//"The following is a block of code. Make a github pull request style summary of the changes. for(String s: commitArr){String keyS = s.substring(0, commitCount+1).strip();commitMap.put(keyS, new ArrayList<>());"
}
