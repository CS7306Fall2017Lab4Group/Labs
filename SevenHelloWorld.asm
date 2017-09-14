# SevenHelloWorld
# Author: Lab Group 4
# August 28, 2017
# Objective: print "Hello World" 7x then exit

.text

main :
	li $v0, 4			    # specify Print String Service (#4) I Moved this call before the subroutine call becuase it only needs to be set once
	jal Hello			    #call Hello subroutine jal is the jump and link command

quit :	li $v0, 10			    # specify Print String Service (#10) li is load immediate will load 10 into register v0 wich is reserved for syscall
	syscall				    # syscall to execute requested service specified in Register $v0 (#10) 10 is the exit call
	
# Subroutine to print Hello World
.data
myString: .asciiz "Hello World \n"          #.asciiz creates a null terminated string
	
.text
Hello :
	bgt $t0,6,quit      		    #will loop from 0 to 6 then break to the quit label
	la $a0, myString    		    #$a0 is one of the argument registers this command will store the address of myString in $a0
	syscall				    # syscall to execute requested service specified in Register $v0 (#4)
	addi $t0,$t0,1      		    # increments the counter stored in $t0 by 1
	j Hello             		    # jump to the Hello label
		


