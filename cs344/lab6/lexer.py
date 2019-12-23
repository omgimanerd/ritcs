#!/usr/bin/env python3
#
# Kladoi lexical analyzer written in Python. Python's regular expression
# documentation was used as a reference for the re library and some example
# tokenizer code.
# https://docs.python.org/3.2/library/re.html#writing-a-tokenizer
# Author: Alvin Lin (axl1439)

import re

LEXER_REGEX = [
    '(?P<ASSIGNMENT>:=)',
    '(?P<COMMENT>\|=.*=\|)',
    '(?P<IDENTIFIER>[a-zA-Z]+)',
    '(?P<LITERAL>[\-]*[0-9]+)',
    '(?P<OPERATION>[+\-*/])',
    '(?P<WHITESPACE>[\s]+)'
]

EXCLUDED_TOKENS = ['COMMENT', 'WHITESPACE']

"""
A class encapsulating a token type, containing the token type, the lexeme it
matched, and the line number in the program where it was found.
"""
class Token:
    def __init__(self, token, lexeme, line):
        self.token = token
        self.lexeme = lexeme
        self.line = line

    def __str__(self):
        return '({} {} {})'.format(self.token, self.lexeme, self.line)

"""
A class that takes a Kladoi program as text and returns a stream of tokens
through lexical analysis of the program.
"""
class Lexer:
    def __init__(self, text):
        self.text = text
        self.regex = re.compile('|'.join(LEXER_REGEX))
        self.line = 1
        self.position = 0

    def get_token_stream(self):
        stream = []
        match = self.regex.match(self.text)
        while match is not None:
            token = match.lastgroup
            lexeme = match.group(token)
            if token in EXCLUDED_TOKENS:
                self.line += lexeme.count('\n')
            else:
                stream.append(Token(token, lexeme, self.line))
            self.position = match.end()
            match = self.regex.match(self.text, self.position)
        if self.position != len(self.text):
            raise SyntaxError(
                'Line {}: Syntax Error'.format(self.line))
        return stream
