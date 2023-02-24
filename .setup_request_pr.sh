#!/bin/sh
function setup_request_pr(){
	echo "Copying current repository to new directory"
	mkdir ~/aipr
	mkdir ~/tmprqpr
	cp -R . ~/aipr || echo "...attempt failed"
	sleep 3s
	echo "adding request_pr function to source"
	source ~/aipr/.custom_request_commands.sh || echo "...source failed"
	rm -f ~/aipr/.setup_request_pr.sh
	echo "Setup complete - navigate to the repository of which you want to generate comments and execute request_pr"
	echo "If you would like the function request_pr to perist termnial sessions add to your .bash_profile on mac"
	echo "or .bashrc on WSL2. If using ohmyZsh or anything else please refere to this:"
	echo " https://letmegooglethat.com/?q=how+to+add+custom+shell+commands+to+ohmyzsh"
	echo "  "
	echo "For further setup and error message explanations run request_help from any directory."
}