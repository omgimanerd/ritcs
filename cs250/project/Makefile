#
# Makefile for CCS Experiment 5 - merge sort
#

#
# Location of the processing programs
#
RASM  = /home/fac/wrc/bin/rasm
RSIM  = /home/fac/wrc/bin/rsim
RLINK = /home/fac/wrc/bin/rlink

#
# Suffixes to be used or created
#
.SUFFIXES:	.asm .obj .lst .out

#
# Transformation rule: .asm into .obj
#
.asm.obj:
	$(RASM) -l $*.asm > $*.lst

#
# Transformation rule: .obj into .out
#
.obj.out:
	$(RLINK) -m -o $*.out $*.obj > $*.map

#
# Object files
#
OBJECTS = dijkstra.obj

#
# Main target
#
dijkstra.out:	$(OBJECTS)
	$(RLINK) -m -o dijkstra.out $(OBJECTS) > dijkstra.map

run:
	$(RSIM) dijkstra.out < input1
