# AiPR
automated pull request commit summary

### Limitations - Banned Words
Because of the current commit read in method do not put the word 'commit', 'diff' or '@@' anywhere in the code or comments.
Including these words will break the method of pulling the code changes.

### How to setup the functionality
The shell script must be first sourced to the terminal instance or added to the .bashrc file for your profile.
Additionally, it is hardcoded to post a text file of the git log to a folder located at '/mnt/c/code' and creates a file 
called 'changefile.txt'.
You must make an OpenAi API account and get an api key. Create a java class called API_KEY.java and make that key a static variable
This key can also be passed to the System Environment variables and called directly but you need to uncomment the code.

### Steps to Reproduce Results
1. source the .custom_bash_commands.sh in the command line
2. from the directory you wish to create the pr summary run request_pr
3. the results will be available as a json file and printed to the command line
4. The last entry of each commit is the error counts for prompt errors and service down errors
5. One key is dedicated to a total commit count for the service call called 'totalNumberOfCommits'