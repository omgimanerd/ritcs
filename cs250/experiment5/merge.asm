#
# FILE:         $File$
# AUTHOR:       Phil White, RIT 2016
#               
# CONTRIBUTORS:
#		Alvin Lin (axl1439)
#
# DESCRIPTION:
#	This file contains the merge function of mergesort
#

#-------------------------------

#
# Numeric Constants
#

PRINT_STRING = 4
PRINT_INT = 1


#***** BEGIN STUDENT CODE BLOCK 2 ***************************


#
# Make sure to add any .globl that you need here
#
        .globl merge
        
#
# Name:         merge
# Description:  takes two individually sorted areas of an array and
#		merges them into a single sorted area
#
#		The two areas are defined between (inclusive)
#		area1:	low   - mid
#		area2:	mid+1 - high
#
#		Note that the area will be contiguous in the array
#
# Arguments:    a0:     a parameter block containing 3 values
#			- the address of the array to sort
#			- the address of the scrap array needed by merge
#			- the address of the compare function to use
#			  when ordering data
#               a1:     the index of the first element of the area
#               a2:     the index of the last element of the area
#               a3:     the index of the middle element of the area
# Returns:      none
# Destroys:     t0,t1
#              
merge:
        sub     $t0, $a2, $a1           # Handles the trivial case of 0
        beq     $t0, $zero, merge_return
        
        addi    $sp, $sp, -48           # Store necessary stuff to stack
        sw      $ra, 0($sp)
        sw      $s0, 4($sp)
        sw      $s1, 8($sp)
        sw      $s2, 12($sp)
        sw      $s3, 16($sp)
        sw      $s4, 20($sp)
        sw      $s5, 24($sp)
        sw      $s6, 28($sp)
        sw      $s7, 32($sp)
        
        lw      $s0, 0($a0)             # Load the addr of the array to sort
        lw      $s1, 4($a0)             # Load the addr of the scrap array
        lw      $s2, 8($a0)             # Load the addr of the compare fn
        
        move    $s3, $a3                # Copy the FIRST AREA END
        add     $s3, $s3, $s3           # Add it to itself 2 times to simulate
        add     $s3, $s3, $s3           # multiplication by 4 to get the addr
        add     $s3, $s3, $s0           # Add array addr to get element addr
                                        # FIRST AREA END is now a pointer
        
        move    $s4, $a2                # Copy the SECOND AREA END
        add     $s4, $s4, $s4           # Add it to itself 2 times to simulate
        add     $s4, $s4, $s4           # multiplication by 4 to get the addr
        add     $s4, $s4, $s0           # Add array addr to get element addr
                                        # SECOND AREA END is now a pointer
        
        move    $s5, $a1                # Copy the FIRST AREA START
        add     $s5, $s5, $s5           # Add it to itself 2 times to simulate
        add     $s5, $s5, $s5           # multiplication by 4 to get the addr
        add     $s5, $s5, $s0           # Add array addr to get element addr
                                        # FIRST AREA START is now a pointer
        
        addi    $s6, $a3, 1             # Calculate the SECOND AREA START
        add     $s6, $s6, $s6           # Add it to itself 2 times to simulate
        add     $s6, $s6, $s6           # multiplication by 4 to get the addr
        add     $s6, $s6, $s0           # Add array addr to get element addr
                                        # SECOND AREA START is now a pointer
        
        sw      $s1, 36($sp)            # Store the addr of scrap array
        sw      $s5, 40($sp)            # Store FIRST AREA START for later

        # Do the merge process and store the merged values in the scrap array
        # We will use FIRST AREA START as a loop counter up to FIRST AREA END
        # Likewise, SECOND AREA START will loop up to SECOND AREA END
merge_loop:
        sub     $t0, $s3, $s5           # Diff btwn first area start and end
        sub     $t1, $s4, $s6           # Diff btwn second area end and start
        add     $s7, $t0, $t1           # Total diff
        addi    $s7, $s7, 4             # Add 4 to include the end index

        bltz    $s7, merge_done         # If the diff less than 0, we are done
        
        lw      $a0, 0($s5)             # Load FIRST AREA START into a0
        lw      $a1, 0($s6)             # Load SECOND AREA START into a1
        bltz    $t0, second_area        # If first area is empty, put second
        bltz    $t1, first_area         # If second area is empty, put first
        jalr    $s2                     # Call the compare function otherwise
        beq     $v0, $zero, second_area # Act based on the result
        
first_area:
        sw      $a0, 0($s1)             # The value of a0 goes first
        addi    $s5, $s5, 4             # Shift FIRST AREA START (pointer)
        j       compare_done
        
second_area:
        sw      $a1, 0($s1)             # The value of a1 goes first
        addi    $s6, $s6, 4             # Shift SECOND AREA START (pointer)
        
compare_done:
        addi    $s1, $s1, 4             # Increment scrap array addr (pointer)
        j       merge_loop

merge_done:
        lw      $s1, 36($sp)            # Restore the original scrap array addr
        lw      $s5, 40($sp)            # Restore original FIRST AREA START

# Copy the scrap array back to the original array
copy_loop:
        sub     $t0, $s4, $s5           # Loop until FIRST AREA START >
        bltz    $t0, copy_done          #            SECOND AREA END
        lw      $t0, 0($s1)             # Load scrap array element
        sw      $t0, 0($s5)             # Store into result array
        
        addi    $s5, $s5, 4             # Increment FIRST AREA START (pointer)
        addi    $s1, $s1, 4             # Increment scrap array addr (pointer)
        j       copy_loop
        
copy_done:      
        lw      $ra, 0($sp)             # Restore necessary stuff from stack
        lw      $s0, 4($sp)
        lw      $s1, 8($sp)
        lw      $s2, 12($sp)
        lw      $s3, 16($sp)
        lw      $s4, 20($sp)
        lw      $s5, 24($sp)
        lw      $s6, 28($sp)
        lw      $s7, 32($sp)
        addi    $sp, $sp, 48

merge_return:   
        jr      $ra

# ********** END STUDENT CODE BLOCK 2 **********

#
# End of Merge routine.
#
