# Kladoi Tests
Tests for the Kladoi language from PLC Lab 5

# Usage

To use theses tests, clone the repository into the root of your project. For example, if you have the project in `~/kladoi-parser` then use the following to clone:

```sh
cd ~/kladoi-parser
echo kladoi-tests/ >> .gitignore
git clone git@github.com:cra2801/kladoi-tests.git
```

The root of your project (where you clone the tests into) should contain a file called `kladoi-parser.sh`, which should have execution rights. This script is required for try submission anyways, so you should already have it.

The script file, `./kladoi-tests/run-tests.sh` is designed to execute `kladoi-parser.sh` for each test case. This script expects to be run from the root of your project, and makes the assumption that your execution script is also in the root. As a result your project structure look resemble the following:

```
project-name/
    kladoi-parser.sh
    kladoi-tests/
        run-tests.sh
```

To execute the scripts, type the following (from the root of your project)

```sh
./kladoi-tests/run-tests.sh
```

*Note for Windows users: You will need to use the Git Bash in order to run sh files. The Git Bash is included with the install of Git (which you should already have). No need to install anything.*

I would advise using make or Heliotis' provided `kladoi-parser.sh` file (if using java) to reduce the time it takes to run the test suite.

The script will print the name of the test files that did not match the expected results. If your parser passes every test, then the test script will print nothing to stdout. For the tests that fail, the expected output and actual output (from your program) will be printed out so that you can see the differences.

I will also be adding more tests over the next two days, I would periodically pull the repository to get new updates. To do so (from the root of the project).

```sh
pushd ./kladoi-tests
git pull
popd
```

If you are familiar with git subtrees (or the older submodules) then use those, but this should be fine for most people and I wouldn't recommend learning them in a short period of time (workflow is different).

# Issues

If You have any issues with the test files, disputes, problems running the scripts, etc, etc. Let me know so that I can fix them. You may discuss issues directly here, or on myCourses. I can respond to issues on GitHub faster.

# Contributing

_DO NOT, UNDER ANY CIRCUMSTANCES COMMIT/PUSH SOLUTIONS TO ANY BRANCH OF THIS REPOSITORY_

The master branch is locked down, so if you want to add a new test, or submit a change to a test, then create a branch with the change, commit and push, then perform a pull request on github. A pull request will require a code review from me. I will pull the branch onto my machine to see if the test works with my solution (which currently passes all the try tests) before accepting the PR.

If creating a new test, please name the file something descriptive that explains the purpose of the test. This way, people can look at the filename of the test that failed to figure out what they should fix (generally). File names shall use kebab-case, which means lower case letters separated by dashes, not underscores or spaces. Test files should use the .kld file extension as this is needed to the test script.

You should not create a file in the expect directory. Your pull request (in the details) should include the expected output of the test(s). I will personally verify that this is the case before merging. It should be noted that the files in expect/ are automatically generated.

_DO NOT NAME FILES WITH SPACES IN THEM, TEST SCRIPTS MIGHT BREAK_

## Indentation
Use spaces, with indentation equal to 4 spaces, unless the file is testing weird whitespace issues. Otherwise, make the files look clean.

## Newlines
Use CRLF or LF in test files. Your parsers should handle both. Do not mix these in one file.
