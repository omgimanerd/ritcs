#
# Makefile for CompOrg Experiment 3 - sub_strings
#

#
# Location of the processing programs
#
RASM  = /home/fac/wrc/bin/rasm
RLINK = /home/fac/wrc/bin/rlink

#
# Suffixes to be used or created
#
.SUFFIXES:	.asm .obj .lst .out

#
# Object files
#
#OBJFILES = sub_strings.obj sub_ascii_numbers.obj
OBJFILES = sub_ascii_numbers.obj sub_strings.obj

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
# Main target
#
sub_strings.out:	$(OBJFILES)
	$(RLINK) -m -o $*.out $(OBJFILES) > $*.map
