#!/bin/bash

function request_pr() {
    if [ -z "$1" ]
    then
        echo "git date format is required to create file"
        echo "try request_pr <date>"
    else
        echo "Creating changefile.txt with commits since $1"
        git log -p --stat --since="$1" > ~/tmprqpr/changefile.txt
        sleep 10s
        (cd ~/aipr && exec ./gradlew run --quiet --console=plain)
        
    fi
}

function request_help() {
    echo "request_pr : will execute a subshell process calling './gradlew run' and call OpenAi with a custom prompt and your commit history passed to create a json file and "
    echo "print to the console an ai generated commit comment for each commit in date range"
    echo "__________________________________________________________________________________________________________________________________________"
    echo "  "
    echo "What is a valid date to be passed to request_pr <date>?: "
    echo "replace <date> with anything such as -1 for last commit in current project or 10 days ago. Best uses are -1, 1 day, a date such as 2023-1-10"
    echo "  "
    echo "__________________________________________________________________________________________________________________________________________"
    echo "  "
    echo "How to read the output?:"
    echo "Each commit stream is seperated by [][][][][][]"
    echo "Each commit hash is seperated by **************"
    echo "Each file difference is seperated by -----------"
    echo "Errors on file differences are logged where in the commit they occured. Because of prompting token length limitations some file diffs are not"
    echo "possible to generate good results. These will result is a ERROR: Prompt size to large to complete request for file in"
    echo "If your file diff includes one of the 3 banned language usages of commit,diff, or @@ these also will be logged in stream"
    echo "If for some reason there is a network time on OpenAi's response time the result will be logged in place as a SERVICE ERROR"
    echo "  "
    echo "__________________________________________________________________________________________________________________________________________"
    echo "  "
    echo "!!!  Please make sure before using you navigate to the project at root ~/aipr and make a java class API_KEY with the key given to you by OPENai"
    echo "without this unique API hash key you will not be able to make calls successfully to the service.  !!! "
    echo "!!!  DO NOT RUN request_pr on this repository itself.  "
}

   