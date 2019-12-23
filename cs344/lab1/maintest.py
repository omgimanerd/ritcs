"""
Do some tests of the linked list implementation.
January 2018
"""

__author__ = 'James Heliotis'

from linked_list import linked_list
from typing import Callable, Any

def equal_to( s: Any ) -> Callable[ [ Any ], bool ]:
    """
    Curry the equals function.
    :param s: the value against which others are compared
    :return: a function that compares a value to the saved s value
    """
    def func( t: Any ) -> bool:
        return s == t
    return func

def test() -> None:
    """
    Do a bunch of tests. Fairly good coverage.
    """
    lst = linked_list(); print( len( lst ), str( lst ) )
    lst = lst.append( 1 ); print( len( lst ), str( lst ) )
    lst = lst.append( 3 ); print( len( lst ), str( lst ) )
    lst = lst.append( 8 ); print( len( lst ), str( lst ) )
    lst = lst.append( 11 ); print( len( lst ), str( lst ) )
    lst = lst.pop(); print( len( lst ), str( lst ) )
    lst = lst.insert( lambda e: e == 11, 19 ); print( len( lst ), str( lst ) )

    print( "11 is at", lst.index( equal_to( 11 ) ) )
    print( "1 is at", lst.index( equal_to( 1 ) ) )
    print( lst.count( lambda e: True ), "elements in the list" )
    print( "False count test",
           "passed" if lst.count( lambda e: False ) == 0 else "failed" )
    print( lst.count( lambda e: e % 2 == 0 ), "even elements" )
    print( lst.count( lambda e: e % 2 == 1 ), "odd elements" )

    lst = lst.remove( equal_to( 8 ) ); print( len( lst ), str( lst ) )
    lst = lst.remove_all( lambda e: e > 20 ); print( len( lst ), str( lst ) )
    print( "(Saving as lst3 for later.)" )
    lst3 = lst
    lst = lst.remove( equal_to( 11 ) ); print( len( lst ), str( lst ) )
    lst = lst.remove_all( lambda e: True ); print( len( lst ), str( lst ) )
    try:
        lst = lst.pop()
        print( "Pop-from-empty test failed" )
    except IndexError as ie:
        print( "Pop-from-empty test passed" )
    except:
        print( "Pop-from-empty test failed (unexpected exception" )

    lst2 = linked_list.make_from_repr( "['how','are','you']" )
    print( len( lst2 ), str( lst2 ) )
    lst2 = lst2.remove( equal_to( "are" ) )
    print( len( lst2 ), str( lst2 ) )
    lst2 = lst2.insert( equal_to( "you" ), "old are" )
    print( len( lst2 ), str( lst2 ) )
    print( "lst3:", len( lst3 ), str( lst3 ) )

if __name__ == '__main__':
    test()

# Output should be as if follows.
#
# 0 []
# 1 [1]
# 2 [1,3]
# 3 [1,3,8]
# 4 [1,3,8,11]
# 3 [3,8,11]
# 4 [3,8,19,11]
# 11 is at 3
# 1 is at 4
# 4 elements in the list
# False count test passed
# 1 even elements
# 3 odd elements
# 3 [3,19,11]
# 3 [3,19,11]
# (Saving as lst3 for later.)
# 2 [3,19]
# 0 []
# Pop-from-empty test passed
# 3 ['how','are','you']
# 2 ['how','you']
# 3 ['how','old are','you']
# lst3: 3 [3,19,11]
