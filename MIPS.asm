main:    li $t0, 5                     #Set t0 = 5
         addi $t1, $zero, 0            #Set t1 = 0
         li $t2, 5

ADD:     add $t1, $t1, 1            #t1 += 5
         addi $t0, $t0, -1             #Decrement $t0
         bge $t0, zero, ADD

         li $v0, 4 #print t1
         move $a0, $t1
         syscall

         li $v0, 10 #terminate the program
         syscall

