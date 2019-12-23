#!/usr/bin/env python3

from bs4 import BeautifulSoup
import requests
from urllib.parse import urlparse
import argparse
import math
import sys
import os
import re

class AssignmentException(Exception):
    pass


class Assignment:
    # What kind of assignment is this?
    assignment_type = None
    # URL to assignment index page. To be filled in by subclasses.
    index = None
    # Folder under the URL above at which assignments are uploaded.
    hw_dir = "Hw%d/"
    # Regex to match problem sections, since this professor has no idea what
    # semantic markup is
    problem_regex = re.compile(r'^Problem \d\d?$')
    # Use a unique header so that the sysadmins can see this is something weird and
    # block it, if they choose to
    headers = {'user-agent': 'wel2138 hw-test-fetch/0.1'}
    # Local paths
    data_path = "data/hw%d/%s/"
    def __init__(self, number):
        self.soup = None
        self.number = number

    def get(self):
        full_url = self.index + (self.hw_dir % self.number) + 'index.html'
        dbgprint("Downloading %s assignment from %s..." % (self.assignment_type,
                                                           full_url))
        resp = requests.get(full_url, headers=self.headers)
        if (resp.status_code != 200):
            raise AssignmentException("It looks like Professor Bezakova hasn't "
                                      "posted the assignment yet! HTTP response"
                                      " code: %s" % (resp.status_code))
        self.soup = BeautifulSoup(resp.content, "lxml")
        self.identify_problems()
        self.identify_tests()
        self.download()

    # For whatever reason, Professor Bezakova doesn't use any sematic markup at
    # all. The problems are headed by font tags with "Problem" in their text.
    def __is_problem_header(self, tag):
        return (tag.name == 'font' and
                self.problem_regex.match(tag.text) is not None)

    def __is_input(self, tag):
        return tag.name == 'a' and 'input-' in tag['href']

    def __is_output(self, tag):
        return tag.name == 'a' and 'answer-' in tag['href']


    def identify_problems(self):
        self.problems = [] 
        tags = self.soup.find_all(self.__is_problem_header)
        for tag in tags:
            # Lowercase the problem name and strip the space out, for use as the
            # directory name this problem will go in
            prob_name = tag.text.lower().replace(' ', '')
            # Add it to the list
            self.problems.append(prob_name)
        dbgprint("Discovered problems: %s" % (self.problems,))

    def identify_tests(self):
        self.tests = {}
        input_a = self.soup.find_all(self.__is_input)
        output_a = self.soup.find_all(self.__is_output)
        for anchor in input_a:
            # Extract the URL
            url = urlparse(anchor['href'])
            # Use the last part of the URL's path as the filename
            filename = url.path.split('/').pop()
            # Take the part of the filename after the dash, parse it as a float, and
            # then floor the float to get it as an integer. Subtract 1 to ensure
            # the array indecies line up
            problem = math.floor(float(filename.split('-')[1])) - 1
            if not problem in self.tests.keys():
                self.tests[problem] = []
            self.tests[problem].append(anchor['href'])
        for anchor in output_a:
            # Extract the URL
            url = urlparse(anchor['href'])
            # Use the last part of the URL's path as the filename
            filename = url.path.split('/').pop()
            # Take the part of the filename after the dash, parse it as a float, and
            # then floor the float to get it as an integer. Subtract 1 to ensure
            # the array indecies line up
            print(filename)
            problem = math.floor(float(filename.split('-')[1])) - 1
            if not problem in self.tests.keys():
                self.tests[problem] = []
            self.tests[problem].append(anchor['href'])
        dbgprint("Found test files: %s" % str(self.tests))

    def __server_to_local_filename(self, server_filename):
        dash = server_filename.rfind('-')
        dot = server_filename.rfind('.')
        return server_filename[:dash] + server_filename[dot+1:]

    def download(self):
        print("self.problems:", self.problems)
        print("self.tests:", self.tests)
        # Get all of the keys in the dict of problems
        for i in range(0, len(self.problems)):
            dbgwrite("Downloading tests for %s: " % self.problems[i])
            # Loop over every test in the tests dict for problem k
            for j in range(0, len(self.tests[i])):
                # The full URL to get is the index + "Hw#/(input|output)-#.#"
                full_url = (self.index + (self.hw_dir % self.number) +
                            self.tests[i][j])
                # Figure out the problem number
                prob_num = self.problems[i][self.problems[i].index('m')+1:]
                # Create the local filename based on the server filename
                local_name = self.__server_to_local_filename(
                    self.tests[i][j].split('/').pop()
                )
                local_path = self.data_path % (prob_num, self.problems[i])
                if not os.path.exists(local_path):
                    os.makedirs(local_path)
                r = requests.get(full_url, headers=self.headers)
                if (r.status_code != 200):
                    raise AssignmentError(
                        "Unable to download %s: HTTP status %s" % (
                            self.tests[i][j],
                            r.status_code
                        )
                    )
                with open(local_path+local_name, "w") as f:
                    f.write(r.text)
                dbgwrite("%d, " % (j+1))
            dbgwrite("done\n")


class RegularAssignment(Assignment):
    index = "https://www.cs.rit.edu/~ib/Classes/CSCI261_01_Fall17-18/Homeworks/"
    assignment_type = "regular"


class HonorsAssignment(Assignment):
    index = "https://www.cs.rit.edu/~ib/Classes/CSCI264_Fall17-18/Homeworks/"
    assignment_type = "honors"


def main(argv):
    global verbose
    # Argparse to make things slightly easier for me
    parser = argparse.ArgumentParser(description="Downlaoad cs264 test cases.")
    parser.add_argument('hwnum', type=int, metavar='N', help=
                        'The number of the assignment to download')
    parser.add_argument('--verbose', '-v', action='store_true', help=
                        'Be verbose in printing output.')
    args = parser.parse_args(argv[1:])
    verbose = args.verbose
    h = HonorsAssignment(args.hwnum)
    r = RegularAssignment(args.hwnum)
    h.get()
    r.get()

def dbgwrite(message):
    if verbose:
        sys.stdout.write(message)
        sys.stdout.flush()

def dbgprint(message):
    if verbose:
        print(message)


if __name__ == "__main__":
    main(sys.argv)
