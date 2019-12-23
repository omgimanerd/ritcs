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
                stream.append((token, lexeme, self.line))
            self.position = match.end()
            match = self.regex.match(self.text, self.position)
        if self.position != len(self.text):
            raise SyntaxError(
                'Line {}: Syntax Error'.format(self.line))
        return stream

if __name__ == '__main__':
    with open('sample.kld') as f:
        lexer = Lexer(f.read())
        for token, lexeme, line in lexer.get_token_stream():
            print(token, lexeme, line)
