main:    addi $t0, $zero, 5                     #Set t0 = 5
         addi $t1, $zero, 0            #Set t1 = 0
         addi $t2, $zero, 5

ADD:     addi $t1, $t1, 1            #t1 += 5
         addi $t0, $t0, -1             #Decrement $t0
         bne $t0, $zero, ADD

         addi $v0, $zero, 4 #print t1
         add $a0, $zero, $t1
         syscall
	 sw $a2, $t0, 37
 
         lw $a2, 38
         addi $v0, $zero, 10 #terminate the program
         syscall

stored: .word 15
hexstored: .word 0x31
