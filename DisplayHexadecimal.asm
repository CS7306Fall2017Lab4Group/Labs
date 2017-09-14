# DisplayHexidecimal
# Author: Lab Team 4
# August 28,2017
# Objective: Prompt user to input an integer then display it as a Hexidecimal

.data 
prompt: .asciiz "Enter the decimal number to convert:\n"
HexStart: .asciiz "0x"

 
.text
main:
	la $a0, prompt               # Set $a0 to prompt string form display
	li $v0, 4                    # Set $v0 to 4 for printing strings
	syscall                      # Print prompt string
	
	li $v0, 51                   # wait for user to enter input
	syscall
	
	move $t2, $a0                # store value in $a0 in $t2 so we can reuse $a0
	
	la $a0, HexStart             # Set $a0 to HexStart
	li $v0, 4                    # Set $v0 to 4 for printing strings
	syscall                      # print HexStart (0x)
	
	li $t0, 8                    # set counter ($t0) to 8 
	    
LoopThroughHex:     
	beqz $t0, quit               # if $t0 = 0 then branch to quit    
	rol $t2, $t2, 4              # we rotate 4 bits to the left so we can print in the correct order     
	and $t4, $t2, 0xf            # mask with 1111 to get only the currect Hex digit   
	ble $t4, 9, ZeroThruNine     # if less $t4 <= 9 we branch to ZeroThruNine     
	addi $t4, $t4, 55            # if $t4 > 9 we add 55 to get (A, B, C, D, E, or F)     
	b PrintDigit
	     
ZeroThruNine:    
	addi $t4, $t4, 48            # add 48 to get (0, 1, 2, 3, 4, 5, 6, 7, 8, or 9)
PrintDigit:     
	la $v0, 11          	     # set $v0 to 4 for printing Characters   
	la $a0,($t4)                 # set $a0 to print the current Character
	syscall   
	addi $t0, $t0, -1            # decrement loop counter 
	j LoopThroughHex             # jump to LoopThroughHex to continue executing
quit:     
	la $v0, 10
	syscall
