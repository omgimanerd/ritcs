# Project (Dijkstra's Algorithm)
# Filename: dijkstra.asm
# Author: Alvin Lin (axl1439)
#
# Description: Implementation of Dijkstra's Algorithm in MIPS assembly.




# CONSTANTS
        MAX_NODES = 20
        MAX_EDGES = 400
        SIZE_NODE = 12
        INFINITY  = 255

        PRINT_INT = 1
        PRINT_STRING = 4
        READ_INT = 5
        TERMINATE = 10




# DATA AREA
# String Constants
        .data
        .align  2
invalid_nodes_error:
        .asciiz "Invalid number of nodes. Must be between 1 and 20.\n"
invalid_edges_error:
        .asciiz "Invalid number of edges. Must be between 0 and 400.\n"
invalid_source_node_error:
        .asciiz "Invalid source for edge.\n"
invalid_destination_node_error:
        .asciiz "Invalid destination for edge.\n"
invalid_weight_error:
        .asciiz "Invalid weight for edge.\n"
invalid_starting_node_error:
        .asciiz "Invalid starting node.\n"
pathing_header:
        .asciiz "Node    Path : Distance\n"
dash:
        .asciiz "-"
space:
        .asciiz " "
tab:
        .asciiz "\t"
newline:
        .asciiz "\n"

# Important memory segments
        .align 2

# Used by main to hold the list of edges
edges:
        .space  120                             # For a maximum of 400 edges

# Used and returned by create_adjacency_matrix for computation
adjacency_matrix:
        .space  1600                            # 4 bytes * 20 * 20

# Used and returned by dijkstras_algorithm for computation
path_predecessors:
        .space  20
path_weights:
        .space  20
visited_nodes:
        .space  20




# PROGRAM AREA
# The code for reading the input, printing output, and executing Dijkstra's Algorithm.
        .text
        .align  2

# Prints the value stored in $a0 as an integer
# Params:
#       $a0: The integer value to print
# Returns: none
print_int:
        li      $v0, PRINT_INT
        syscall
        jr      $ra

# Prints a string whose address is at $a0
# Params:
#      $a0: The address of the string to print
# Returns: none
print_string:
        li      $v0, PRINT_STRING
        syscall
        jr      $ra

# Prints a dash character
# Params: none
# Returns: none
print_dash:
        la      $a0, dash
        li      $v0, PRINT_STRING
        syscall
        jr      $ra

# Prints a space character
# Params: none
# Returns: none
print_space:
        la      $a0, space
        li      $v0, PRINT_STRING
        syscall
        jr      $ra

# Prints a tab character
# Params: none
# Returns: none
print_tab:
        la      $a0, tab
        li      $v0, PRINT_STRING
        syscall
        jr      $ra

# Prints a newline character
# Params: none
# Returns: none
print_newline:
        la      $a0, newline
        li      $v0, PRINT_STRING
        syscall
        jr      $ra

# Read in an integer, $v0 will contain the integer read
# Params: none
# Returns:
#       $v0: The integer read from STDIN
read_int:
        li      $v0, READ_INT
        syscall
        jr      $ra

# Given the source and destination node, this function calculates the byte
# offset needed to reach that edge in the adjacency matrix.
# Params:
#       $a0: The source node
#       $a1: The destination node
#       $a2: The number of nodes
# Returns:
#       $v0: the byte offset of the edge
get_adjacency_matrix_address_offset:
        mult    $a0, $a2
        mflo    $v0
        add     $v0, $v0, $a1
        li      $t0, 4                          # Multiply by 4 to get bytes
        mult    $v0, $t0
        mflo    $v0
        jr      $ra

# Creates an adjacency matrix given the list of edges and the number of nodes.
# This function will use its designated memory chunk defined in the data
# section for computation and return the address of that segment.
# Params:
#       $a0: The memory address of the segment containing the list of edges
#            Edges are stored as (source, dest, weight) (12 bytes each)
#       $a1: The number of nodes
#       $a2: The number of edges
# Returns:
#       $v0: The address of the memory segment containing the adjacency matrix
create_adjacency_matrix:
        addi    $sp, $sp, -24                   # Save registers to stack
        sw      $ra, 0($sp)
        sw      $s0, 4($sp)
        sw      $s1, 8($sp)
        sw      $s2, 12($sp)
        sw      $s3, 16($sp)
        sw      $s4, 20($sp)

        la      $s0, adjacency_matrix           # Get adjacency matrix address
        move    $s1, $a0                        # Address of edges list
        move    $s2, $a1                        # Number of nodes
        move    $s3, $a2                        # Number of edges

        li      $s4, 0                          # Loop counter for edges
create_adjacency_matrix_edges_loop:
        beq     $s4, $s3, create_adjacency_matrix_edges_loop_done
        lw      $a0, 0($s1)                     # Source node of edge
        lw      $a1, 4($s1)                     # Destination node of edge
        move    $a2, $s2                        # The number of nodes
        jal     get_adjacency_matrix_address_offset
        add     $t1, $s0, $v0                   # Address in adjacency matrix
        lw      $t0, 8($s1)                     # Weight of edge
        sw      $t0, 0($t1)                     # Write weight into matrix
        addi    $s4, $s4, 1                     # Increment loop counter
        addi    $s1, $s1, SIZE_NODE             # Advance edges list pointer
        j       create_adjacency_matrix_edges_loop
create_adjacency_matrix_edges_loop_done:
        move    $v0, $s0                        # Return the matrix address
        
        lw      $ra, 0($sp)                     # Restore registers from stack
        lw      $s0, 4($sp)
        lw      $s1, 8($sp)
        lw      $s2, 12($sp)
        lw      $s3, 16($sp)
        lw      $s4, 20($sp)
        addi    $sp, $sp, 24
        jr      $ra

# Prints the contents of the given adjacency matrix.
# Params:
#       $a0: The memory address of the segment containing the adjacency matrix
#       $a1: The number of nodes
# Returns: none
print_adjacency_matrix:
        addi    $sp, $sp, -24                   # Save registers to stack
        sw      $ra, 0($sp)
        sw      $s0, 4($sp)
        sw      $s1, 8($sp)
        sw      $s2, 12($sp)
        sw      $s3, 16($sp)
        sw      $s4, 20($sp)

        move    $s0, $a0                        # Address of adjacency matrix
        move    $s1, $a1                        # Number of nodes

        # This loop prints the column header of the adjacency matrix
        jal     print_newline
        jal     print_space
        jal     print_space
        jal     print_space
        jal     print_space
        li      $s2, 0
print_adjacency_matrix_loop_0:
        beq     $s2, $s1, print_adjacency_matrix_loop0_done
        move    $a0, $s2
        li      $a1, 1
        jal     print_adjacency_matrix_element  # Print column header
        addi    $s2, $s2, 1
        j       print_adjacency_matrix_loop_0        
print_adjacency_matrix_loop0_done:
        jal     print_newline

        # This double loop prints out the contents of the adjacency matrix
        li      $s2, 0                          # Outer loop counter
print_adjacency_matrix_loop1:
        beq     $s2, $s1, print_adjacency_matrix_loop1_done
        move    $a0, $s2
        li      $a1, 1
        jal     print_adjacency_matrix_element  # Print row header
        li      $s3, 0                          # Inner loop counter
print_adjacency_matrix_loop2:
        beq     $s3, $s1, print_adjacency_matrix_loop2_done
        lw      $a0, 0($s0)                     # Load the current weight
        li      $a1, 0
        jal     print_adjacency_matrix_element  # Print the current weight
        addi    $s0, $s0, 4                     # Increment the matrix pointer
        addi    $s3, $s3, 1                     # Increment the loop counter
        j       print_adjacency_matrix_loop2
print_adjacency_matrix_loop2_done:
        jal     print_newline
        addi    $s2, $s2, 1                     # Increment the loop counter
        j       print_adjacency_matrix_loop1
print_adjacency_matrix_loop1_done:
        jal     print_newline

        lw      $ra, 0($sp)                     # Restore registers from stack
        lw      $s0, 4($sp)
        lw      $s1, 8($sp)
        lw      $s2, 12($sp)
        lw      $s3, 16($sp)
        lw      $s4, 20($sp)
        addi    $sp, $sp, 24
        jr      $ra

        # print adj matrix element
        # $a0: element
        # $a1: if this is zero, then we print a dash if element == 0
print_adjacency_matrix_element:
        addi    $sp, $sp, -8                    # Save register to stack
        sw      $ra, 0($sp)
        sw      $s0, 4($sp)

        move    $s0, $a0        
        bne     $s0, $zero, print_adjacency_matrix_element_nonzero
        jal     print_space
        jal     print_space
        beq     $a1, $zero, print_adjacency_matrix_element_dash
        move    $a0, $s0
        jal     print_int
        j       print_adjacency_matrix_element_done
print_adjacency_matrix_element_dash:
        jal     print_dash
        j       print_adjacency_matrix_element_done
print_adjacency_matrix_element_nonzero:
        addi    $t0, $s0, -100                  # If element is 3 digits
        bltz    $t0, print_adjacency_matrix_element_2digit
        move    $a0, $s0
        jal     print_int
        j       print_adjacency_matrix_element_done
print_adjacency_matrix_element_2digit:
        jal     print_space
        addi    $t0, $s0, -10                   # If element is 2 digits
        bltz    $t0, print_adjacency_matrix_element_1digit
        move    $a0, $s0
        jal     print_int
        j       print_adjacency_matrix_element_done
print_adjacency_matrix_element_1digit:
        jal     print_space
        move    $a0, $s0
        jal     print_int
print_adjacency_matrix_element_done:
        jal     print_space
        
        lw      $ra, 0($sp)                     # Restore registers from stack
        lw      $s0, 4($sp)
        addi    $sp, $sp, 8
        jr      $ra

        # Min function
        # $a0: value
        # $a1: value2
        # $v0: min of values
min:
        sub     $t0, $a0, $a1
        bltz    $t0, min_a0_less
        move    $v0, $a1
        jr      $ra
min_a0_less:
        move    $v0, $a0
        jr      $ra
        
        # Solves with Dijkstra's algorithm
        # $a0: addr of adjacency matrix
        # $a1: number of nodes
        # Returns:
        # $v0: addr of predecessor array
        # $v1: addr of path weights array
dijkstras_algorithm:
        addi    $sp, $sp, -36                   # Save registers to stack
        sw      $ra, 0($sp)
        sw      $s0, 4($sp)
        sw      $s1, 8($sp)
        sw      $s2, 12($sp)
        sw      $s3, 16($sp)
        sw      $s4, 20($sp)
        sw      $s5, 24($sp)
        sw      $s6, 28($sp)
        sw      $s7, 32($sp)

        move    $s0, $a0                        # Address of adjacency matrix
        move    $s1, $a1                        # Number of nodes
        move    $s2, $a2                        # The starting node
        la      $s3, path_predecessors          # Address of predecessor array
        la      $s4, path_weights               # Address of path weights array
        la      $s5, visited_nodes              # Address of visited nodes array

        # Initialize the predecessor and weights array
        li      $t0, 0                          # Loop counter
        li      $t1, -1                         # Undefined placeholder
        li      $t2, INFINITY                   # Infinity placeholder
dijkstras_algorithm_initialization_loop:
        beq     $t0, $s1, dijkstras_algorithm_initialization_loop_done
        sb      $t1, 0($s3)                     # Set predecessor to unknown
        sb      $t2, 0($s4)                     # Set weight to infinite
        addi    $t0, $t0, 1                     # Increment loop counter
        addi    $s3, $s3, 1                     # Shift array pointer
        addi    $s4, $s4, 1                     # Shift array pointer
        j       dijkstras_algorithm_initialization_loop
dijkstras_algorithm_initialization_loop_done:
        la      $s3, path_predecessors          # Reset memory address
        la      $s4, path_weights               # Reset memory address
        add     $t0, $s4, $s2                   # Starting node weight address
        sb      $zero, 0($t0)                   # Source to source = 0
        # End initialization
        
dijkstras_algorithm_path_computation_loop:
        # Check if we've visited all the nodes
        li      $t0, 0                          # Unvisited loop counter
        li      $t1, 1                          # Unvisited check (1 = visited)
dijkstras_algorithm_count_unvisited_loop:
        beq     $t0, $s1, dijkstras_algorithm_count_unvisited_loop_done
        lb      $t2, 0($s5)                     # Load visited status
        and     $t1, $t1, $t2                   # AND with node visited
        addi    $t0, $t0, 1                     # Increment loop counter
        addi    $s5, $s5, 1                     # Shift array address
        j       dijkstras_algorithm_count_unvisited_loop
dijkstras_algorithm_count_unvisited_loop_done:
        la      $s5, visited_nodes              # Reset memory address
        # Jump to the end of the path computation loop if we're done
        beq     $t1, $zero, dijkstras_algorithm_path_computation_loop_done
        # End node visitation check

        # Select a node to propagate from
        li      $t0, 1                          # Node selection loop counter
        li      $s6, 0                          # Selected node
        add     $s7, $s4, $t1                   # Selected node weight address
        lb      $s7, 0($s7)                     # Selected node weight
dijkstras_algorithm_node_selection_loop:
        beq     $t0, $s1, dijkstras_algorithm_node_selection_loop_done
        add     $t3, $s4, $t0                   # Get node weight address
        lb      $t3, 0($t3)                     # Load node weight
        sub     $t4, $s7, $t3                   # Compare selected to current
        bltz    $t4, dijkstras_algorithm_node_selection_loop_continue
        move    $s6, $t0                        # Update selected node
        move    $s7, $t3                        # Update selected node weight
dijkstras_algorithm_node_selection_loop_continue:       
        addi    $t0, $t0, 1                     # Increment loop counter
        j       dijkstras_algorithm_node_selection_loop
dijkstras_algorithm_node_selection_loop_done:
        add     $t0, $s6, $s5                   # Get visited array address
        li      $t2, 1
        sb      $t2, 0($t0)                     # Mark selected as visited
        # End node selection: $s6 is the selected node, $s7 is its path weight

        # Traverse neighbors of selected node
        li      $t0, 0                          # Loop counter (neighbor node)
dijkstras_algorithm_node_neighbors_loop:
        beq     $t0, $s1, dijkstras_algorithm_node_neighbors_loop_done
        move    $a0, $s6                        # Selected node
        move    $a1, $t0                        # Destination node (neighbor)
        move    $a2, $s1                        # The number of nodes
        jal     get_adjacency_matrix_address_offset
        add     $t1, $s0, $v0                   # Address in adjacency matrix
        lw      $t1, 0($t1)                     # Weight of path
        # If the weight is zero, then no path exists, skip this neighbor
        bne     $t1, $zero, dijkstras_algorithm_node_neighbors_loop_continue
        add     $t2, $t1, $s7                   # Temporary distance
        # Check if temporary distance is less than neighbor distance
        add     $t3, $s4, $t0                   # Address of neighbor weight
        lb      $t4, 0($t3)                     # Load neighbor weight
        sub     $t5, $t2, $t3
        # Temporary distance is less than neighbor distance, skip neighbor
        bltz    $t5, dijkstras_algorithm_node_neighbors_loop_continue
        # Else, we take this neighbor as path
        sb      $t2, 0($t3)                     # A shorter path found
        # Save predecessor
        add     $t6, $s3, $t0                   # Address of this predecessor
        sb      $s6, 0($t6)
dijkstras_algorithm_node_neighbors_loop_continue:       
        addi    $t0, $t0, 1
        j       dijkstras_algorithm_node_neighbors_loop
dijkstras_algorithm_node_neighbors_loop_done:
        # End neighbor traversal
        
        j       dijkstras_algorithm_path_computation_loop
dijkstras_algorithm_path_computation_loop_done:
        move    $v0, $s3                        # Return the path predecessors
        move    $v1, $s4                        # Return the path weights
        
        lw      $ra, 0($sp)                     # Restore registers from stack
        lw      $s0, 4($sp)
        lw      $s1, 8($sp)
        lw      $s2, 12($sp)
        lw      $s3, 16($sp)
        lw      $s4, 20($sp)
        lw      $s5, 24($sp)
        lw      $s6, 28($sp)
        lw      $s7, 32($sp)
        addi    $sp, $sp, 36
        jr      $ra
        
        
# Main: program execution starts here
main:
        addi    $sp, $sp, -36
        sw      $ra, 0($sp)
        sw      $s0, 4($sp)
        sw      $s1, 8($sp)
        sw      $s2, 12($sp)
        sw      $s3, 16($sp)
        sw      $s4, 20($sp)
        sw      $s5, 24($sp)
        sw      $s6, 28($sp)
        sw      $s7, 32($sp)

        # Read and validate the number of nodes from STDIN.
        # The number of nodes will be stored in $s0
        jal     read_int
        blez    $v0, print_invalid_nodes_error
        addi    $t0, $v0, -MAX_NODES
        bgtz    $t0, print_invalid_nodes_error
        move    $s0, $v0

        # Read and validate the number of edges from STDIN.
        # The number of edges will be stored in $s1
        jal     read_int
        bltz    $v0, print_invalid_edges_error
        addi    $t0, $v0, -MAX_EDGES
        bgtz    $t0, print_invalid_nodes_error
        move    $s1, $v0

        # Read and validate the edges.
        # The address of the memory segment storing the edges will be stored in
        # register $s2
        la      $s2, edges                      # Addr of edges memory segment.
        li      $t0, 0                          # A loop counter
edge_reading_loop:
        beq     $t0, $s1, edge_reading_loop_done
        # Read and validate the source node
        jal     read_int
        bltz    $v0, print_invalid_source_node_error
        addi    $t1, $v0, -MAX_NODES
        bgez    $t1, print_invalid_source_node_error
        sw      $v0, 0($s2)
        # Read and validate the destination node
        jal     read_int
        bltz    $v0, print_invalid_destination_node_error
        addi    $t1, $v0, -MAX_NODES
        bgez    $t1, print_invalid_destination_node_error
        sw      $v0, 4($s2)
        # Read and validate the edge weight
        jal     read_int
        blez    $v0, print_invalid_weight_error
        sw      $v0, 8($s2)
        addi    $s2, $s2, SIZE_NODE             # Increment edges memory addr
        addi    $t0, $t0, 1                     # Increment loop counter
        j       edge_reading_loop
edge_reading_loop_done:
        la      $s2, edges                      # Reset the address to start

        # Read and validate the starting node.
        # The starting node will be stored in $s3
        jal     read_int
        bltz    $v0, print_invalid_starting_node_error
        addi    $t0, $v0, -MAX_NODES
        bgez    $t0, print_invalid_starting_node_error
        move    $s3, $v0

        # Create the adjacency matrix
        # We will store the address of the adjacency matrix in $s4 for the rest
        # of the program.
        move    $a0, $s2                        # Address of edges list
        move    $a1, $s0                        # Number of nodes
        move    $a2, $s1                        # Number of edges
        jal     create_adjacency_matrix
        move    $s4, $v0

        # Print the adjacency matrix
        move    $a0, $s4                        # Address of adjacency matrix
        move    $a1, $s0                        # Number of nodes
        jal     print_adjacency_matrix

        # Execute Dijkstra's algorithm and find the paths
        move    $a0, $s4                        # Set the adjacency matrix
        move    $a1, $s0                        # Set the number of nodes
        move    $a2, $s3                        # Set the starting node
        jal     dijkstras_algorithm

        # Print the result of the algorithm
        la      $a0, pathing_header
        jal     print_string
        move    $t0, $v0                        # Path predecessors
        move    $t1, $v1                        # Path weights
        move    $a0, $t0
        jal     print_int
        jal     print_space
        move    $a0, $t1
        jal     print_int
        jal     print_newline

        # Temporary code to print out vars
        li      $t2, 0
loop:
        beq     $t2, $s0, loop_done
        lb      $a0, 0($t0)
        jal     print_int
        jal     print_space
        lb      $a0, 0($t1)
        jal     print_int
        jal     print_space
        jal     print_newline
        addi    $t0, $t0, 1
        addi    $t1, $t1, 1
        addi    $t2, $t2, 1
        j       loop
loop_done:
        jal     print_newline
        # End temporary code

        j       main_done

# PRINT ERROR CODE SUBROUTINES
# The following subroutines are in charge of printing a specific error code
# and exiting.
print_invalid_nodes_error:
        la      $a0, invalid_nodes_error
        jal     print_string
        j       main_done

print_invalid_edges_error:
        la      $a0, invalid_edges_error
        jal     print_string
        j       main_done

print_invalid_source_node_error:
        la      $a0, invalid_source_node_error
        jal     print_string
        j       main_done

print_invalid_destination_node_error:
        la      $a0, invalid_destination_node_error
        jal     print_string
        j       main_done

print_invalid_weight_error:
        la      $a0, invalid_weight_error
        jal     print_string
        j       main_done

print_invalid_starting_node_error:
        la      $a0, invalid_starting_node_error
        jal     print_string
        j       main_done
# END ERROR CODE SUBROUTINES

# Restores the return address and s registers before exiting.
main_done:
        lw      $ra, 0($sp)
        lw      $s0, 4($sp)
        lw      $s1, 8($sp)
        lw      $s2, 12($sp)
        lw      $s3, 16($sp)
        lw      $s4, 20($sp)
        lw      $s5, 24($sp)
        lw      $s6, 28($sp)
        lw      $s7, 32($sp)
        addi    $sp, $sp, 36
        jr      $ra
