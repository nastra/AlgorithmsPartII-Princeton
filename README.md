AlgorithmsPartII-Princeton
=====================

Code for programming assignments from Algorithms Part II (https://class.coursera.org/algs4partII-003).

Requires the stdlib.jar and algs4.jar to run.

Week 1 - WordNet
----------------------------
 - **WordNet.java** - A WordNet represented as a directed graph.
 - **SAP.java** - Represents an immutable data type for shortest ancestral paths.
 - **Outcast.java** - Identifies outcasts.

Week 2 - Seam Carving (Content-Aware Resizing)
--------------------------------
 - **SeamCarver.java** - Seam-carving is a content-aware image resizing technique where the image is reduced in size by one pixel of height (or width) at a time.
 

Week 3 - Baseball Elimination
--------------------------------
 - **BaseballElimination.java** - We use maximum flow algorithms to solve the baseball elimination problem. 
 
Week 4 - Boggle
-------------------------
Boggle is a word game designed by Allan Turoff and distributed by Hasbro. It involves a board made up of 16 cubic dice, where
each die has a letter printed on each of its sides. At the beginning of the game, the 16 dice are shaken and randomly distributed into a 4-by-4 tray, with
only the top sides of the dice visible.

- ** BoggleBoard.java** - Represents a Boggle board.
- ** BoggleSolver.java** - Implements an algorithm to solve Boggle boards.


Week 5 - Burrows-Wheeler Compression Algorithm
----------------------------------------------
Implement the Burrows-Wheeler data compression algorithm. This revolutionary algorithm outcompresses gzip and PKZIP, is
relatively easy to implement, and is not protected by any patents. It forms the basis of the Unix compression utililty bzip2.

- **CircularSuffixArray.java** - Describes the abstraction of a sorted array of the N circular suffixes of a string of length N.
- ** MoveToFront.java** - Maintains an ordered sequence of the 256 extended ASCII characters and provides encoding and decoding steps.
- **BurrowsWheeler.java** - Implements the actual encoding and decoding algorithms.
