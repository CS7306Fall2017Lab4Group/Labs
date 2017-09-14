# nHelloWorld
# Author: Lab Group 4
# August 28,2017
# Objective: Print "Hello World" between 1 and 10 times based on user input

.data
prompt:   .asciiz "Enter an integer between 1 and 10:\n"
error:	  .asciiz "out of range\n"
myString: .asciiz "Hello World \n"

.text
main: 
	la   $a0, prompt      # load address of prompt for syscall
	li   $v0, 4           # specify Print String service (See MARS Help --> Tab System Calls)
	syscall               # System call to execute service # 4 (in Register $v0)
	li   $v0, 51          # specify Read Integer service (#51)
	syscall               # System call to execute service # 51 (in Register $v0) : Read the number. After this instruction, the number read is in Register $a0.
      
	li $v0 4              #set $v0 to 4 to print strings.
      
	bgt $a0,10,OutOfRange #if entered integer is greater than 10 branch to OutOfRange label
	blt $a0,1,OutOfRange  #if entered integer is less than 1 branch to OutOfRange label
      
	la $t0,($a0)          #store entered integer from $a0 in $t0
      
Hello :            
	la $a0, myString    #$a0 is one of the argument registers this command will store the address of myString in $a0
      	bge $t1,$t0,Quit    #will loop from 0 to 6 then break to the quit label
	syscall		    # syscall to execute requested service specified in Register $v0 (#4)
	addi $t1,$t1,1      # increments the counter stored in $t0 by 1
	j Hello             # jump to the Hello label
	
Quit :	li $v0, 10	    # specify Print String Service (#10)
	syscall	
			
OutOfRange :
	la $a0, error       # load the error string into $a0 from printing
	syscall             # syscall to execute requested service specified in Register $v0 (#4)
	j main              # jump back to main label so user can enter new integer

