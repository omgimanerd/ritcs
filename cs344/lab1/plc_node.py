"""
Linked list node class for PLC lab 1 on linked lists
January 2018
"""

__author__ = "Alvin Lin (axl1439)"

from typing import Any, Callable, Tuple

class plc_node( object ):
    """
    A storage unit used in linked data structures.
    Each plc_node contains a value and a link to another plc_node,
    or None if this plc_node has no successor.

    No operations herein modify any data. When necessary,
    new plc_nodes are created and returned to realize the needed results.
    """

    __slots__ = "data", "link" # These are read-only!

    def __init__( self: 'plc_node', data: Any = None, link: 'plc_node' = None ):
        """
        Initialize a plc_node to the values given as parameters.
        :param data: the data "element" value the plc_node should contain
        :param link: the next plc_node in the chain, or None if this is the last
        """
        object.__setattr__( self, "data", data )
        object.__setattr__( self, "link", link )

    def __setattr__( self: 'plc_node', key: str, value: Any ):
        raise TypeError(
            "Trying to change attribute '" + key + "' in immutable object" )

    def insert(
            self: 'plc_node',
            test: Callable[ [ Any ], bool ],
            element: Any
        ) -> 'plc_node':
        """
        A private method that performs the recursive operation of
        creating a new chain of plc_nodes containing the new element.
        The new element appears before the first existing plc_node whose
        data passes the test, or at the very end of the list if
        the test never passes.
        :param test: the predicate function applied to each plc_node's data
        :param element: the new data value to be inserted
        :return: a the head of a new chain of nodes containing the same
                 chain as the old one, plus the new inserted data plc_node
        """
        if test(self.data):
            return plc_node(element, self)
        elif self.link is None:
            return plc_node(self.data, plc_node(element))
        else:
            return plc_node(self.data, self.link.insert(test, element))

    def remove(
                self: 'plc_node',
                test: Callable[ [ Any ], bool ] )\
            -> Tuple[ 'plc_node', int ]:
        """
        A private method that performs the recursive operation of
        creating a new chain of plc_nodes not containing one existing element.
        The element to be removed is the first existing plc_node whose
        data passes the test. If no data element passes the test, an
        equivalent of the original list is returned.
        :param test: the predicate function applied to each plc_node's data
        :return: a tuple of two elements...
          The first is the head of a new chain of nodes not containing
            the removed plc_node.
          The second is 0 if no nodes were removed, or 1 if a plc_node was
            removed.
        """
        if test(self.data):
            return self.link, 1
        elif self.link is None:
            return self, 0
        else:
            head, removed = self.link.remove(test)
            return plc_node(self.data, head), removed
       
    def remove_all(
                self: 'plc_node',
                test: Callable[ [ Any ], bool ] )\
            -> Tuple[ 'plc_node', int ]:
        """
        A private method that performs the recursive operation of
        creating a new chain of plc_nodes not containing some of the existing
        elements. The elements to be removed are those nodes whose
        data pass the test. If no data element passes the test, an
        equivalent of the original list is returned.
        :param test: the predicate function applied to each plc_node's data
        :return: a tuple of two elements...
          The first is the head of a new chain of nodes not containing
            the removed plc_node.
          The second is the number of elements removed.
        """
        if self.link is None:
            if test( self.data ):
                return None, 1
            else:
                return self, 0
        else:
            new_head, count = self.link.remove_all( test )
            if test( self.data ):
                return new_head, count + 1
            else:
                return plc_node( self.data, new_head ), count
