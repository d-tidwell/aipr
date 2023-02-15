package aipr;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

import java.net.SocketTimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenAiServe {

//    final static String regExSpecialChars = "<([{\\^-=$!|]})?*+>";
//    final static String regExSpecialCharsRE = regExSpecialChars.replaceAll( ".", "\\\\$0");
//    final static Pattern reCharsREP = Pattern.compile( "[" + regExSpecialCharsRE + "]");
//
//    String valid = "The following is a block of code. Make a github pull request style summary of the changes. + / @ - * ; { } \r \n for(String s: commitArr){String keyS = s.substring(0, commitCount+1).strip();commitMap.put(keyS, new ArrayList<>());";
//    public static String quoteRegExSpecialChars(String s)
//    {
//        Matcher m = reCharsREP.matcher( s);
//        return m.replaceAll( "\\\\$0");
//    }
//
//    public static boolean containsIllegals(String toExamine) {
//        Pattern pattern = Pattern.compile("[~#@*+%{}<>\\[\\]|\"\\_^]");
//        Matcher matcher = pattern.matcher(toExamine);
//        return matcher.find();
//    }
    public void makeRequest(String prompt_code) throws SocketTimeoutException {
        //String token = System.getenv("OPENAI_TOKEN");
        //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
        OpenAiService service = new OpenAiService("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");
        StringBuilder prompt_build = new StringBuilder();
        //prompt_build.append("The following is a block of code.");
        //prompt_build.append("The lines starting with - are lines removed from the code.");
        prompt_build.append("Summarize of the changes to the following code.");
        prompt_build.append("The lines starting with + are additions to the code.");
        //prompt_build.append("Infer why the changes were made and explain any necessary details.");
        prompt_build.append("Use Bullet point to summarize, being as clear and brief as possible about the changes.");
        prompt_build.append("CODE:");
        //System.out.println(prompt_build.toString().length() + " !!!!!!!!!!!!!!!!!!!!!!!PrePromptCount");
        //need to clean this string somehow of invisible line breaks

        String cleaned_prompt =prompt_code.replaceAll("\\n", "").replaceAll("\\r", "").trim();
        prompt_build.append(cleaned_prompt);

        //String newPrompt = OpenAiServe.quoteRegExSpecialChars(prompt_build.toString());
        //System.out.println(newPrompt);
        //System.out.println(newPrompt.length() + "    XXXXXXXXXXXXXXXXXXXXXXXPost Prompt Count");
        System.out.println("\nCreating completion...");
        CompletionRequest completionRequest = CompletionRequest.builder()
                .model("text-davinci-003")
                .prompt(prompt_build.substring(prompt_code.indexOf("@@")))
                .echo(false)
                .user("testing")
                .maxTokens(450)
                .build();


        //return first result
        System.out.println(service.createCompletion(completionRequest).getChoices().get(0));
    }

}
