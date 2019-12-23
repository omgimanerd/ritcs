"""
Linked list class for PLC lab 1 on linked lists
January 2018
"""

__author__ = "Alvin Lin (axl1439)"

from typing import Any, Callable, Tuple

from plc_list_spec import plc_list
from plc_node import plc_node

class linked_list( plc_list ):
    """
    A list implemented with plc_nodes linked together

    No operations herein modify any data. When necessary,
    a new linked_list is created, with new plc_nodes as needed, and the new
    linked_list is returned to satisfy the method's contract.
    """

    __slots__ = "head", "size" # These are read-only!

    def __init__( self: 'linked_list', head: plc_node = None, size: int = 0 ):
        """
        Initialize an empty list.
        head will be None, and size will be 0.
        """
        plc_list.__setattr__( self, "head", head )
        plc_list.__setattr__( self, "size", size )

    def __setattr__( self: 'linked_list', key: str, value: Any ):
        raise TypeError(
            "Trying to change attribute '" + key + "' in immutable object" )

    @staticmethod
    def make_from_repr( str_rep: str ) -> 'linked_list':
        """
        Create a linked_list from a string that looks the same as what
        the repr function would produce.
        Individual elements are determined by calling eval on each
        substring in the list.
        :param str_rep: a sequence of values separated by commas and
                        surrounded by square brackets
        :return: a linked_list ll such that repr(ll) would return str_rep
        :except SyntaxError: if the argument is malformed
        """
        if str_rep[ 0 ] != '[' or str_rep[ -1 ] != ']':
            raise SyntaxError( "missing bracket(s)" )
        elt_str = str_rep[ 1 : -1 ]
        result = linked_list()
        comma_loc = elt_str.find( ',' )
        while comma_loc > -1:
            result = result.append( eval( elt_str[ : comma_loc ] ) )
            elt_str = elt_str[ comma_loc + 1 : ]
            comma_loc = elt_str.find( ',' )
        result = result.append( eval( elt_str ) )
        return result

    def __repr__( self: 'linked_list' ) -> str:
        """
        Return a string representation of the list in the format
        used by Python's built-in lists.
        Example: [3,8,19,11]
        __repr__ is invoked on each element to include it in the string
        NB: If a class does not define __str__ it uses __repr__.
        """
        result = '['
        current = self.head
        while current:
            result += repr( current.data )
            if current.link: result += ','
            current = current.link
        result += ']'
        return result

    def __len__( self: 'linked_list' ) -> int:
        """
        Return the number of elements in this list.
        """
        return self.size

    def append( self: 'linked_list', element: Any ) -> 'linked_list':
        """
        Return a list containing all of self's elements, plus element at the
        end. (Implementation hint: append can call insert.)
        :param element: the value to append
        :return: the new list
        """
        return self.insert(lambda x: False, element)

    def count( self: 'linked_list', test: Callable[[Any], bool] ) -> int:
        """
        Return the number of times an element that satisfies test
        appears in this list.
        :param test: a predicate function for list elements
        :return: the count of satisfying elements
        """
        if self.head is None:
            return 0
        if test(self.head.data):
            return 1 + linked_list(self.head.link).count(test)
        else:
            return linked_list(self.head.link).count(test)

    def index( self: 'linked_list', test: Callable[[Any], bool] ) -> int:
        """
        Return the index number of the location of the first
        element that satifies the test, or the list's size if not found.
        :param test: a predicate function for list elements
        :return: the ordinal location of the first satisfying element
        """
        if self.head is None:
            return 0
        if test(self.head.data):
            return 0
        else:
            return 1 + linked_list(self.head.link, self.size - 1).index(test)

    def insert( self: 'linked_list', test: Callable[[Any], bool],
                element: Any ) -> 'linked_list':
        """
        Insert a new element before the first element in this list
        that satisfies a test. If the test never returns True, this
        function behaves as if append had been called.
        :param test: the predicate function described above.
        :param element: the element to be inserted
        :return: a new List into which the given element has been inserted
        """
        if self.head is None:
            return linked_list(plc_node(element, None), 1)
        else:
            return linked_list(self.head.insert(test, element), self.size + 1)

    def pop( self: 'linked_list' ) -> 'linked_list':
        """
        Remove the first element from the list.
        (Implementation hint: almost a special case of remove)
        :return: a new list like self but with the first element of self missing
        :except: IndexError if the list is empty.
        """
        if self.head is None or self.size == 0:
            raise IndexError("List is empty!")
        return self.remove(lambda x: True)

    def remove( self: 'linked_list', test: Callable[[Any], bool] ) -> 'linked_list':
        """
        Remove an element from the list.
        :param test: a test function to decide which element to remove
        :return: a new list like self but with the first element of self that
                 passes the test removed
        """
        head, removed = self.head.remove(test)
        return linked_list(head, self.size - removed)

    def remove_all( self: 'linked_list', test: Callable[[Any], bool] ) -> 'linked_list':
        """
        Remove elements from the list.
        :param test: a test function to decide which elements to remove
        :return: a new list like self but with the elements of self that
                 pass the test removed
        """
        head, removed = self.head.remove_all(test)
        return linked_list(head, self.size - removed)
