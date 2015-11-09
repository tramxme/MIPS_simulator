# This test driver just loads a 32-bit word to get it's bits counted

main:  lw	$a0, test1	# Here is the test value

   jal	countbits	# Go count the bits in $a0

   or	$t0, $v0, $zero	# save $v0 in $t0, dangit, because the HALT cmd wipes it out!
   ori	$v0, $zero, 10	# TRAP HALT; whose bright idea was it to pass codes
   syscall			# in the $v registers?

# countbits.s - This function counts the number of '1' bits in a 32 bit word.
# It simply returns the number of 1's found.

# Count bits in $a0, returning $v0 as the count

# first, the "setup"

countbits:	move	$v0, $zero	# Initialize counter in $v0

            ori	$t0, $zero, 1	# Initialize with a 32-bit mask with a '1' in the low bit of $t0

# now, the loop to count 1's:

            loop:	and	$t1, $t0, $a0	# see if the bit is on
            beq	$t1, $zero, notset	# branch if bit was not set
            addiu	$v0, $v0, 1	# increment counter in $v0
            notset:	sll	$t0, $t0, 1	# Shift the mask in register $t0 left one bit
            bne	$t0, $zero, loop  # Loop back if the mask in $t0 still has a bit
            ret:	jr	$ra		# return to caller with count in $v0

.data

# Here's some random test data:

test1:	.word	0x0001004F	# test data : to count bits

.end

