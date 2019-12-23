"""
Interface definition for the PLC lab assignment on list implementation
January 2018
"""

__author__ = 'James Heliotis'

from abc import abstractmethod, ABCMeta
from typing import Any, Callable

class plc_list( object, metaclass=ABCMeta ):
    """
    An interface for a class that represents sequences of objects
    """

    __slots__ = () # no data values in this abstract class

    @abstractmethod
    def append( self: 'plc_list', element: Any ) -> 'plc_list':
        """
        Return a list containing all of self's elements, plus element
        at the end. (Implementation hint: append can call insert.)
        :param element: the value to append
        :return: the new list
        """
        pass

    @abstractmethod
    def count( self: 'plc_list', test: Callable[ [ Any ], bool ] ) -> int:
        """
        Return the number of times an element that satisfies test
        appears in this list.
        :param test: a predicate function for list elements
        :return: the count of satisfying elements
        """
        pass

    @abstractmethod
    def index( self: 'plc_list', test: Callable[ [ Any ], bool ] ) -> int:
        """
        Return the index number of the location of the first
        element that satifies the test, or the list's size if not found.
        :param test: a predicate function for list elements
        :return: the ordinal location of the first satisfying element
        """
        pass

    @abstractmethod
    def insert(
            self: 'plc_list',
            test: Callable[ [ Any ], bool ], element: Any
        ) -> 'plc_list':
        """
        Insert a new element before the first element in this list
        that satisfies a test. If the test never returns True, this
        function behaves as if append had been called.
        :param test: the predicate function described above.
        :param element: the element to be inserted
        :return: a new List into which the given element has been inserted
        """
        pass

    @abstractmethod
    def pop( self: 'plc_list' ) -> 'plc_list':
        """
        Remove the first element from the list.
        (Implementation hint: almost a special case of remove)
        :return: a new list like self but with the first element of self missing
        :except: IndexError if the list is empty.
        """
        pass

    @abstractmethod
    def remove(
            self: 'plc_list',
            test: Callable[ [ Any ], bool ]
        ) -> 'plc_list':
        """
        Remove an element from the list.
        :param test: a test function to decide which element to remove
        :return: a new list like self but with the first element of self that
                 passes the test removed
        """
        pass

    @abstractmethod
    def remove_all(
            self: 'plc_list',
            test: Callable[ [ Any ], bool ]
        ) -> 'plc_list':
        """
        Remove elements from the list.
        :param test: a test function to decide which elements to remove
        :return: a new list like self but with the elements of self that
                 pass the test removed
        """
        pass

    @abstractmethod
    def __repr__( self: 'plc_list' ) -> str:
        """
        Return a string representation of the list in the format
        used by Python's built-in lists.
        Example: [3,8,19,11]
        __repr__ is invoked on each element to include it in the string
        NB: If a class does not define __str__ it uses __repr__.
        """
        pass

    @abstractmethod
    def __len__( self: 'plc_list' ) -> int:
        """
        Return the number of elements in this list.
        """
        pass
