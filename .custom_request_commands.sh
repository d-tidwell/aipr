#!/bin/bash

function request_pr() {
    if [ -z "$1" ]
    then
        echo "git date format is required to create file"
        echo "try request_pr <date>"
    else
        echo "Creating changefile.txt with commits since $1"
        git log -p --stat --since="$1" > ~/aipr/changefile1.txt
        sleep 5
        (cd ~/aipr && exec ./gradlew run)
        
    fi
}

function test_request_pr() {
    echo java -version
    echo "test_request_pr complete please use Java 19 or higher"
    return 0
}

   