# AiPR
Automated completion model generated pull request commit summary.

### Limitations - Banned Words
Because of the current commit read in method do not put the word 'commit', 'diff' or '@@' anywhere in the code or comments.
Including these words will break the method of pulling the code changes. 

This repository has only been tested on Windows 11 with WSL2 Ubuntu 20.04 focal
The project was compiled in Java-19 Amazon Corretto v19.0.2

### How to setup the functionality
The shell script must be first sourced to the terminal instance or added to the .bashrc file for your profile.
Additionally, it is hardcoded to post a text file of the git log to a folder located at '/mnt/c/code' and creates a file 
called 'changefile.txt'.
You must make an OpenAi API account and get an api key. Create a java class called API_KEY.java and make that key a static variable
This key can also be passed to the System Environment variables and called directly but you need to uncomment the code.

### Steps to Reproduce Results
1. Make a directory at the root called aipr

        ➜  ~ mkdir aipr
        ➜  ~ cd aipr

2. source the .custom_bash_commands.sh in the command line
        ➜  aipr source ~/.custom_bash_commands.sh
3. from the directory you wish to create the pr summary run request_pr and a git date i.e.

        request_pr -1 
        request_pr 1.day
        request_pr 2023-1-10
        request_pr 10 days ago

   1st command Only last commit - best solution for iterative commit summaries
   2nd command Everything since 12AM 
   3rd command since this date
   4th command starting from today go 10 days back
4. the results will be available as a json file called pr_summary.json and also printed to the command line
5. The last entry of each commit is the error counts for prompt errors and service down error counts
6. One key is dedicated to a total commit count for the service call called 'totalNumberOfCommits'