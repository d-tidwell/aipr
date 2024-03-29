# AiPR
## Automated completion model generated pull request commit summary.

Command line solution for quick code change memos for pull requests.

---
### How to set up the functionality
The shell script must be first sourced to the terminal instance.


You must also **_make an OpenAi API account and get an api key_**. Create a java class called API_KEY.java and make that key a static variable
This key can also be passed to the System Environment variables and called directly, but you need to uncomment that after the setup code.

---
### Steps to Reproduce Results
1. Clone this repository
2. Create an OpenAi API key through [https://openai.com/api/](https://openai.com/api/)

    *Navigate to /aipr/app/src/main/java/aipr/ and a class called API_KEY.java with a single static string called KEYS with your API Key
      It should look like:

          package aipr;

          public class API_KEY {
              public static final String KEYS = "YOUR_API_KEY_HERE";
          }

3. In the terminal of this repository run:

        source .setup_request_pr.sh
        setup_request_pr
   
    It will copy this directory to the root and source the custom commands for use throughout shell.
    For further information run request_help
        
4. Move up one level in your directory and remove the current repository. A copy has been placed in a known directory.

        cd ..
        rm -r -f aipr

5. Go to the newly created directory and rebuild with gradle:
        
        cd ~/aipr
        ./gradlew build

6. You may also need to allow permissions for ./gradlew in scripts using the following:
   navigate to ~/aipr and run the following:
        
           chmod +x gradlew

7. From the directory **_you wish to create the pr summary from_** run **_request_pr_** and a version of a valid git date option i.e.

        request_pr -1 
        request_pr 1.day
        request_pr 2023-1-10
        request_pr 10 days ago

   * 1st command: Only last commit - is the best solution for iterative commit summaries
   * 2nd command: Everything since 12AM - best for daily pr requests each diff for each commit will be commented
   * 3rd command: since this date - best for wrap up pr requests each diff for each commit will be commented
   * 4th command: starting from today go 10 days back - same same but different
   
* The results will be available as a json file called pr_summary.json in ~/aipr/app and also printed to the command line
* One result key is dedicated to a total commit count for the service call called 'totalNumberOfCommits'

---
### Limitations
**This repository has been tested on Windows 11 with WSL2 Ubuntu 20.04 focal and Gradle 7.6
The project was compiled in Java-19 Amazon Corretto v19.0.2**

**This has now been tested on MacOS Monterey in Linux Terminal version 21.5.0 as well**

Many Java versions 15+ should sufficient. To install multiple versions or switch try SDKman:
[https://www.twilio.com/blog/sdkman-work-with-multiple-versions-java](https://www.twilio.com/blog/sdkman-work-with-multiple-versions-java)

If you see **retrofit2 illegal reflective access errors** you are not using a compatible Java version. Use Java-19 Amazon Corretto v19.0.2.

#### Banned Words
The current commit read in method is a basic text scrape **_do not put the word 'commit', 'diff' or '@@' anywhere in the code or comments_**.
Including these words will result in skipping these code reviews and logging the error in result.

Work small and commit often for best results. Limit your commits that you need for each pr.

---
#### The Model
This program is utilizing the "gpt-3.5-turbo" model.
This model is a child of GPT-3 (Generative Pre-trained Transformer 3), a large-scale language model that uses unsupervised deep learning, fine tuning and reinforcment techniques to produce natural and human-like text.
It is also limited to 4096 tokens or roughly 8000 words. Files with large file changes or a vast number
of lines will either be skipped or result is erratic completions. Keep that in mind while committing or in your expectations of results.
