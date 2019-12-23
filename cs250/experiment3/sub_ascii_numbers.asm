# File:		sub_ascii_numbers.asm
# Author:	K. Reek
# Contributors:	P. White, W. Carithers
#		Alvin Lin (axl1439)
#
# Updates:
#		3/2004	M. Reek, named constants
#		10/2007 W. Carithers, alignment
#		09/2009 W. Carithers, separate assembly
#
# Description:	sub two ASCII numbers and store the result in ASCII.
#
# Arguments:	a0: address of parameter block.  The block consists of
#		four words that contain (in this order):
#
#			address of first input string
#			address of second input string
#			address where result should be stored
#			length of the strings and result buffer
#
#		(There is actually other data after this in the
#		parameter block, but it is not relevant to this routine.)
#
# Returns:	The result of the subtraction, in the buffer specified by
#		the parameter block.
#

	.globl	sub_ascii_numbers

sub_ascii_numbers:
A_FRAMESIZE = 40

#
# Save registers ra and s0 - s7 on the stack.
#
	addi 	$sp, $sp, -A_FRAMESIZE
	sw 	$ra, -4+A_FRAMESIZE($sp)
	sw 	$s7, 28($sp)
	sw 	$s6, 24($sp)
	sw 	$s5, 20($sp)
	sw 	$s4, 16($sp)
	sw 	$s3, 12($sp)
	sw 	$s2, 8($sp)
	sw 	$s1, 4($sp)
	sw 	$s0, 0($sp)
	
# ##### BEGIN STUDENT CODE BLOCK 1 #####
	lw	$s0, 0($a0)	   # get and store the addr of minuend
	lw	$s1, 4($a0)        # get and store the addr of subtrahend
	lw	$s2, 8($a0)        # get and store the addr of result block
	lw	$s3, 12($a0)       # get and store the length of strings
	li	$s4, 0		   # initialize a loop counter to 0
        li      $s5, 0             # use s5 to store whether or not we carry

        add     $s0, $s0, $s3      # offset the memory to start of number
        addi    $s0, $s0, -1       # shift back 1 because 0 indexing
        add     $s1, $s1, $s3      # offset the memory to start of number
        addi    $s1, $s1, -1       # shift back 1 because 0 indexing
        add     $s2, $s2, $s3      # offset the memory to start of number
        addi    $s2, $s2, -1       # shift back 1 because 0 indexing
        
digits_loop:
	beq	$s4, $s3, digits_loop_done
        lb      $t0, 0($s0)        # load the digit in the minuend
        lb      $t1, 0($s1)        # load the digit in the subtrahend
        sub     $t2, $t0, $t1      # subtract the digits

        beq     $s5, $zero, no_previous_carry
        addi    $t2, $t2, -1       # if we needed to carry from previous digit
        li      $s5, 0             # reset the carry tracker
        
no_previous_carry:
        bgez    $t2, no_carry_needed
        addi    $t2, $t2, 10       # add 10 if we need to carry
        li      $s5, -1            # remember to remove 1 for carry

no_carry_needed:        
        addi    $t2, $t2, 48       # add back 48 or 0 ascii
	sb	$t2, 0($s2)        # store the digit in the result block
        
        addi    $s0, $s0, -1       # shift the minuend block pointer
        addi    $s1, $s1, -1       # shift the subtrahend block pointer
        addi    $s2, $s2, -1       # shift the result block pointer
	addi	$s4, $s4, 1        # increment the loop counter
	j	digits_loop
digits_loop_done:
	
	
# ###### END STUDENT CODE BLOCK 1 ######

#
# Restore registers ra and s0 - s7 from the stack.
#
	lw 	$ra, -4+A_FRAMESIZE($sp)
	lw 	$s7, 28($sp)
	lw 	$s6, 24($sp)
	lw 	$s5, 20($sp)
	lw 	$s4, 16($sp)
	lw 	$s3, 12($sp)
	lw 	$s2, 8($sp)
	lw 	$s1, 4($sp)
	lw 	$s0, 0($sp)
	addi 	$sp, $sp, A_FRAMESIZE

	jr	$ra			# Return to the caller.
