# aipr
automated pull request summary

### Banned Words
Because of the current commit read in method do not put the word commit, diff or @@ anywhere in the code or comments.
Including these words will break the method of pulling the code changes.

### How to setup the functionality
Currently this project must live in the same folder as the repository of which you are seeking to get a summary.
Additionally, it is hardcoded to post a text file of the git log to a folder located at '/mnt/c/code' and creates a file 
called 'changefile.txt'.
You must make an OpenAi API account and get a key.
This key must be passed to the System Environment variable.

### Steps to Reproduce Results
