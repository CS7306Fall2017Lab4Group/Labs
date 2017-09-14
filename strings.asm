# DisplayHexidecimal
# Author: Lab Team 4
# August 28,2017
# Objective: Diaplay a menu with options 
#            a) function length, if this option is selected prompt the user for a string and return the length of the string
#            b) put in lowercase, if this option is selected promot the user for a string and return a string with all lowercase versions of the original characters
#            c) delete characters, if this option is selected prompt the user for a string, starting position and number of characters to delte.  return the origiinal string with the requested characters removed
#            d) quite, if this option is selected the program should end


.data 
prompt: .asciiz "Please Select an Option\na) function length\nb) put in lowercase\nc) delete characters\nd) quit"
prompt2: .asciiz "Enter a string"
prompt3: .asciiz "Enter a starting position"
prompt4: .asciiz "Enter the number of characters to delete"
LengthResult: .asciiz "\nLength of string is:"
LowerResult: .asciiz "\nLowercase string is: "
DeleteResult: .asciiz "\nString after delete is:"

buffer: .space 100
NewBuffer: .space 100
menuOpt: .space 4

.text
main:
	la $a0, prompt               # Set $a0 to prompt string form display	
	li $v0, 54                   # wait for user to enter input
	la $a1, menuOpt		     # place the base address for the buffer that will hold the input string in $a1
	li $a2,2		     # give a max length of characters (input followed by terminating null)
	syscall
	
	la $t0,menuOpt		     #Store the base Address for user input in $t0 for future user
	lb $t1,0($t0)		     #Store first byte (character) in $t1

evaluateChoice:
	beq $t1,97,FunctionLength    #  Check if user input A if so, branch to FunctionLength
	beq $t1,65,FunctionLength    #	Check if user input a if so, branch to FunctionLength
	
	beq $t1,66,Lowercase         # Check if user input b if so, branch to Lowercase
	beq $t1,98,Lowercase         # Check if user input B if so, branch to Lowercase
	
	beq $t1,67,DeleteCharAt      # Check if user input c if so, branch to DeleteCharAt
	beq $t1,99,DeleteCharAt      # Check if user input C if so, branck to DeleteCharAt
	
	beq $t1,68,quit		     # Check if user input d if so, branch to quit
	beq $t1,100,quit	     # Check if user input D if so, branch to quit
	j main			     # if user selected an invalid option then start over at main

FunctionLength:
	la $t0,($0)		     # set $t0 to zero. $t0 will hold the count of letters
	jal PromptForString	     # jump an link to the function to Prompt the user to enter a string
	la $s0,buffer		     # set $s0 to the base address for the buffer
	
CountLength:
	lb $t1,0($s0)		     # load the current byte into $t1
	beq $t1,10,PrintLengthResult # check if the current byte is a null character if so, print the result
	addi $t0,$t0,1		     # add 1 to the letter count if $t1 is not a null character
	addi $s0,$s0,1		     # increment the base address of $s0 to get the next byte
	j CountLength		     # jump back to Count Length to continue looping

PrintLengthResult:
	la $a0, LengthResult         # Set $a0 to prompt string form display
	li $v0, 4                    # Set $v0 to 4 for printing strings
	syscall                      # Print prompt string
	
	la $a0, ($t0)                # set $a0 to be the number of characters
	li $v0,1                     # set $v0 to 1 for printing int
	syscall
	
	j main                       # jump back to main
	
Lowercase:
	jal PromptForString          # jump and link to prompt user to enter a string
	la $s0,buffer		     # set $s0 to be the base address of the buffer
	
ReplaceWithLower:
	lb $t1,0($s0)                # load the current character into $t1
	beq $t1,10,PrintLowerResult  # if $t1 is a null character then brach to print the result
	blt,$t1,65,IncrementAndLoop  # if $t1 is less than A then it is not a letter, continue with the next character
	bgt,$t1,90,IncrementAndLoop  # if $t1 is greater than 90 then it is not a Capital letter, continue with the next character
	addi,$t1,$t1,32              # if $ti is a upper case character convert it to its lowercase 
	sb $t1,0($s0)                # store the lowercase version of the character in place of the upper case
IncrementAndLoop:
	addi $s0,$s0,1		     #increment to the next byte and continue looping
	j ReplaceWithLower
	
PrintLowerResult:
	la $a0, LowerResult         # Set $a0 to prompt string form display
	li $v0, 4                    # Set $v0 to 4 for printing strings
	syscall                      # Print prompt string
	
	la $a0,buffer		   # set $a0 to be the base address of the buffer
	li $v0,4		   # set $v0 to 4 for printing strings
	syscall
	
	j main

DeleteCharAt:
	jal PromptForString	    # Prompt user to enter a string then return
	jal PromptForStart	    # Prompt user to enter a starting position (starts at 0)
	jal PromptForNumCharacters  # Prompt user to enter number of characters to delet
	la $s1,NewBuffer	    # load the address of the New buffer (to store result) int $s1
	la $t0,($0)		    # set $t0 to 0 so it can serve as a counter
	la $s0,buffer		    # load the base addres of the buffer into $s0
	add $t4,$t3,$t4		    # add starting position plus number of characters and store in $t4
	
LoopAndDelete:
	lb $t1,0($s0)		     # load the current byte
	beq $t1,10,PrintDeleteResult # if we reach a null character we are at the end of the string. Print the result
	bge $t0,$t3,PastStartingBound # we are past the starting point to delete character and need to check if we are past the ending bounds
	
AddToNewString:
	sb $t1,0($s1)		      # write the current character to the string
	addi $s1,$s1,1		      # increment the base address of the new buffer by 1 byte
	j IncrementLoopDelete	      # jump to Increment Loop Delete label
	
PastStartingBound:
	blt $t0,$t4,IncrementLoopDelete # if we are past the ending bounds we need to add the characters to the new string
	j AddToNewString

IncrementLoopDelete:
	addi $s0,$s0,1		     #increment to the next byte and continue looping
	addi $t0,$t0,1		     #increment the counter
	j LoopAndDelete		     # jump back to the start of loop and delete
	
PrintDeleteResult:
	sb $t1,0($s1)		     # null terminate the new string
	la $a0, DeleteResult         # Set $a0 to prompt string form display
	li $v0, 4                    # Set $v0 to 4 for printing strings
	syscall                      # Print prompt string
	
	la $a0,NewBuffer	     # set $a0 to be the base address of the buffer
	li $v0,4		      # set $v0 to 4 for printing strings
	syscall
	
	jal ClearNewBuffer           # clear out the new buffer so use it again
	
	j main

ClearNewBuffer:
	la $t1,NewBuffer	    # load the addres of the new buffer into $t1
	
LoopClearBuffer:
	lb $t2,0($t1)		   # load the current byte into $t2
	beq $t2,10,LastCharToClear # check if $t2 holds a null character. This way we know we have cleared all but the last character
	sb $0,0($t1)		   # write 0 over the current byte
	addi $t1,$t1,1	           # increment the base address by one and keep looping
	j LoopClearBuffer	
	
LastCharToClear:
	sb $0,0($t1)		  # write 0 over the last character
	jr $ra			  # return back to the linked address
	
	

PromptForString:

	la $a0, prompt2              # Set $a0 to prompt string form display	
	li $v0, 54                   # wait for user to enter input
	la $a1, buffer		     # set $a1 to be the base address of the buffer
	li $a2,100		     # set $a2 to allow 100 characters (99 and 1 terminating null)
	syscall
	
	jr $ra			     # return to linked instruction
	
PromptForStart:
	la $a0, prompt3              # Set $a0 to prompt string form display	
	li $v0, 51                   # wait for user to enter input
	syscall
	
	la $t3,($a0)
	
	jr $ra			     # return to linked instruction
	
PromptForNumCharacters:
	la $a0, prompt4              # Set $a0 to prompt string form display	
	li $v0, 51                   # wait for user to enter input
	syscall
	
	la $t4,($a0)
	
	jr $ra			     # return to linked instruction

quit:     
	la $v0, 10		     # quit execution
	syscall
