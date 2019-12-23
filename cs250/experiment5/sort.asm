#
# FILE:         $File$
# AUTHOR:       Phil White, RIT 2016
# CONTRIBUTORS:
#		W. Carithers
#		Alvin Lin (axl1439)
#
# DESCRIPTION:
#	This program is an implementation of merge sort in MIPS
#	assembly 
#
# ARGUMENTS:
#	None
#
# INPUT:
# 	The numbers to be sorted.  The user will enter a 9999 to
#	represent the end of the data
#
# OUTPUT:
#	A "before" line with the numbers in the order they were
#	entered, and an "after" line with the same numbers sorted.
#
# REVISION HISTORY:
#	Aug  08		- P. White, original version
#

#-------------------------------

#
# Numeric Constants
#

PRINT_STRING = 4
PRINT_INT = 1

#-------------------------------

#

# ********** BEGIN STUDENT CODE BLOCK 1 **********

#
# Make sure to add any .globl that you need here
#

        .globl merge
        .globl sort
        
# Name:         sort
# Description:  sorts an array of integers using the merge sort
#		algorithm
# 		Arguments Note: a1 and a2 specify the range inclusively
#
# Arguments:    a0:     a parameter block containing 3 values
#                       - the address of the array to sort
#                       - the address of the scrap array needed by merge
#                       - the address of the compare function to use
#                         when ordering data
#               a1:     the index of the first element in the range to sort
#               a2:     the index of the last element in the range to sort
# Returns:      none
#

sort:
        sub     $t0, $a2, $a1           # Handles the trivial case of 0
        beq     $t0, $zero, sort_return
        
        addi    $sp, $sp, -48           # Store necessary stuff to stack
        sw      $ra, 0($sp)
        sw      $s0, 4($sp)
        sw      $s1, 8($sp)
        sw      $s2, 12($sp)
        sw      $s3, 16($sp)
        sw      $s4, 20($sp)

        move    $s0, $a0                # Make a copy of parameter block
        move    $s1, $a1                # Copy the first element
        move    $s2, $a2                # Copy the last element
        
        add     $t0, $a1, $a2           # Add the first and last index
        li      $t1, 2                  # Load a 2 for division
        div     $t0, $t1                # Divide index sum by 2
        mflo    $s3                     # Load the quotient into $a3
        addi    $s4, $s3, 1             # Add 1 to get second area start

        move    $a0, $s0                # Set first recursive call params
        move    $a1, $s1
        move    $a2, $s3
        jal     sort                    # Recursively sort first half

        move    $a0, $s0                # Set second recursive call params
        move    $a1, $s4                
        move    $a2, $s2
        jal     sort                    # Recursively sort second half

        move    $a0, $s0                # Set merge function parameters
        move    $a1, $s1
        move    $a2, $s2
        move    $a3, $s3
        jal     merge                   # Merge the two halves

        lw      $ra, 0($sp)             # Restore necessary stuff from stack
        lw      $s0, 4($sp)
        lw      $s1, 8($sp)
        lw      $s2, 12($sp)
        lw      $s3, 16($sp)
        lw      $s4, 20($sp)
        addi    $sp, $sp, 48
        
sort_return:      
        jr      $ra

# ********** END STUDENT CODE BLOCK 1 **********

#
# End of Merge sort routine.
#
