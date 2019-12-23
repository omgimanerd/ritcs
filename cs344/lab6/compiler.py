#!/usr/bin/env python3
#
# Kladoi-to-Phyllo compiler written in Python.
# Author: Alvin Lin (axl1439)

from parser import Parser

# Constants representing the possible Phyllo instructions
PUSH = 'PUSH'
LOAD = 'LOAD'
STORE = 'STORE'
ADD = 'ADD'
SUB = 'SUB'
MULT = 'MULT'
DIV = 'DIV'
JUMP = 'JUMP'
BRZ = 'BRZ'

OPERATION_MAP = {
    '+': ADD,
    '-': SUB,
    '*': MULT,
    '/': DIV
}

# Grammar elements as constants
ASSIGNMENT = 'ASSIGNMENT'
BINARYOP = 'BINARYOP'
IDENTIFIER = 'IDENTIFIER'
LITERAL = 'LITERAL'
OPERATION = 'OPERATION'
IF = 'if'
WHILE = 'while'

"""
A class encapsulating a Phyllo instruction.
"""
class Instruction:
    def __init__(self, name, argument=None):
        self.name = name
        self.argument = argument

    def __str__(self):
        return '{} {}'.format(
            self.name, self.argument if self.argument is not None else '')

"""
A class that takes a Kladoi program as text and uses the the lexer and parser
to build an abstract syntax tree from it. It then uses the AST to generate a
list of Phyllo machine instructions.
"""
class Compiler:
    def __init__(self, text, debug=False):
        self.debug = debug
        self.parser = Parser(text, debug)
        self.tree = self.parser.parse_program()
        self.instructions = None

    def compile(self):
        self.instructions = self.compile_helper(self.tree)
        return self.instructions

    def compile_helper(self, root):
        instructions = []
        if root:
            if root.grammar == ASSIGNMENT:
                instructions += self.compile_helper(root.children[0])
                instructions.append(Instruction(STORE, root.data.lexeme))
            elif root.grammar == IF:
                instructions += self.compile_helper(root.children[0])
                true_clause = self.compile_helper(root.children[1])
                false_clause = self.compile_helper(root.children[2])
                instructions.append(Instruction(BRZ, len(true_clause) + 2))
                instructions += true_clause
                instructions.append(Instruction(JUMP, len(false_clause) + 1))
                instructions += false_clause
            elif root.grammar == WHILE:
                condition = self.compile_helper(root.children[0])
                instructions += condition
                body = self.compile_helper(root.children[1])
                instructions.append(Instruction(BRZ, len(body) + 2))
                instructions += body
                backjump = -len(body) - len(condition) - 1
                instructions.append(Instruction(JUMP, backjump))
            elif root.grammar == LITERAL:
                instructions.append(Instruction(PUSH, root.data.lexeme))
            elif root.grammar == IDENTIFIER:
                instructions.append(Instruction(LOAD, root.data.lexeme))
            elif root.grammar == BINARYOP:
                instructions += self.compile_helper(root.children[0])
                instructions += self.compile_helper(root.children[1])
                operation = root.data.lexeme
                instructions.append(Instruction(OPERATION_MAP[operation]))
            else:
                for child in root.children:
                    instructions += self.compile_helper(child)
        return instructions


if __name__ == '__main__':
    import sys
    if len(sys.argv) == 2:
        with open(sys.argv[1]) as f:
            compiler = Compiler(f.read())
    else:
        compiler = Compiler(sys.stdin.read())
    instructions = compiler.compile()
    print(Parser.preorder_format(compiler.tree, '', 0).strip())
    print()
    for instruction in instructions:
        print(instruction)
