#!/usr/bin/env python3
#
# Kladoi parser written in Python.
# Author: Alvin Lin (axl1439)

from lexer import Lexer

import argparse
import sys

TOKEN = 0
LEXEME = 1
LINE = 2

BEGIN = 'begin'
END = 'end'
PASS = 'pass'
IF = 'if'
WHILE = 'while'
ASSIGN = ':='

IDENTIFIER = 'IDENTIFIER'
LITERAL = 'LITERAL'
OPERATION = 'OPERATION'

class Parser:

    def __init__(self, text, debug=False):
        self.debug = debug
        self.lexer = Lexer(text)
        self.stream = self.lexer.get_token_stream()

    def syntax_error(self):
        raise SyntaxError('Line {}: Syntax Error'.format(self.lexer.line))

    def peek(self):
        if len(self.stream) == 0:
            self.syntax_error()
        return self.stream[0]

    def pop(self):
        if len(self.stream) == 0:
            self.syntax_error()
        return self.stream.pop(0)

    def program(self):
        if self.debug:
            print('PROGRAM')
        if self.peek()[LEXEME] == BEGIN:
            self.pop()
            self.statement_sequence()
            if self.peek()[LEXEME] == END:
                self.pop()
            else:
                self.syntax_error()
        else:
            self.statement()

    def statement_sequence(self):
        if self.debug:
            print('STATEMENT SEQUENCE')
        self.statement()
        if self.peek()[LEXEME] != END:
            self.statement_sequence()

    def statement(self):
        if self.debug:
            print('STATEMENT')
        item = self.peek()
        if item[LEXEME] == IF:
            self.if_keyword()
        elif item[LEXEME] == WHILE:
            self.while_keyword()
        elif item[LEXEME] == PASS:
            self.pop()
        elif item[TOKEN] == IDENTIFIER:
            self.assign()
        else:
            self.syntax_error()

    def assign(self):
        if self.debug:
            print('ASSIGN')
        identifier = self.pop()
        assignment_operator = self.pop()
        if assignment_operator[LEXEME] != ASSIGN:
            self.syntax_error()
        self.expression()

    def if_keyword(self):
        if self.debug:
            print('IF')
        keyword = self.pop()
        self.expression()
        self.program()
        self.program()

    def while_keyword(self):
        if self.debug:
            print('WHILE')
        keyword = self.pop()
        self.expression()
        self.program()

    def expression(self):
        if self.debug:
            print('EXPRESSION')
        item = self.pop()
        if item[TOKEN] == IDENTIFIER:
            return
        elif item[TOKEN] == LITERAL:
            return
        elif item[TOKEN] == OPERATION:
            self.expression()
            self.expression()

    @staticmethod
    def parse_program(text):
        try:
            p = Parser(text)
            p.program()
            if len(p.stream) != 0:
                raise SyntaxError(
                    'Line {}: Extra code at end of program'.format(
                        p.lexer.line - 1))
            print('PASS')
        except SyntaxError as e:
            print(e)

if __name__ == '__main__':
    import sys
    if len(sys.argv) == 2:
        with open(sys.argv[1]) as f:
            Parser.parse_program(f.read())
    else:
        Parser.parse_program(sys.stdin.read())
