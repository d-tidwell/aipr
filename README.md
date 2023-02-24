# AiPR
## Automated completion model generated pull request commit summary.


**This repository has been tested on Windows 11 with WSL2 Ubuntu 20.04 focal
The project was compiled in Java-19 Amazon Corretto v19.0.2**

Many Java versions 15+ should sufficient. To install multiple versions or switch try SDKman:
[https://www.twilio.com/blog/sdkman-work-with-multiple-versions-java](https://www.twilio.com/blog/sdkman-work-with-multiple-versions-java)

If you see **retrofit2 illegal reflective access errors** you are not using a compatible Java version. Use Java-19 Amazon Corretto v19.0.2.

### How to set up the functionality
The shell script must be first sourced to the terminal instance.


You must also **_make an OpenAi API account and get an api key_**. Create a java class called API_KEY.java and make that key a static variable
This key can also be passed to the System Environment variables and called directly but you need to uncomment that after the setup code.

### Steps to Reproduce Results
1. Clone this repository
2. In the terminal of this repository run:

        source .setup_request_pr.sh
        setup_request_pr
   
    It will copy this directory to the root and source the custom commands for use throughout shell.
    For further information run request_help
        
4. Remove the current repository

        cd ..
        rm -r -f aipr

5. from the directory **_you wish to create the pr summary_** run **_request_pr_** and a version of a valid git date option i.e.

        request_pr -1 
        request_pr 1.day
        request_pr 2023-1-10
        request_pr 10 days ago

   * 1st command: Only last commit - is the best solution for iterative commit summaries
   * 2nd command: Everything since 12AM - best for daily pr requests each diff for each commit will be commented
   * 3rd command: since this date - best for wrap up pr requests each diff for each commit will be commented
   * 4th command: starting from today go 10 days back - same same but different

6. the results will be available as a json file called pr_summary.json in ~/aipr/app and also printed to the command line
7. The last entry of each commit is the error counts for prompt errors and service down error counts
8. One key is dedicated to a total commit count for the service call called 'totalNumberOfCommits'

### Limitations - Banned Words - Java Version - Shell Version
Because of the current commit read in method **_do not put the word 'commit', 'diff' or '@@' anywhere in the code or comments_**.
Including these words will break the method of pulling the code changes.

Work small and commit often for best results. Limit your commits that you need for each pr. 