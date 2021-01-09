Original C# Killer... very close to the Scala version, using Linq
=================================================================

[simon@fedora killer]$ dotnet run
started...
[5, 2, 8, 4, 3, 7, 1, 6, 9, 4, 9, 6, 2, 5, 1, 3, 8, 7, 3, 1, 7, 6, 8, 9, 5, 2, 4, 7, 6, 1, 3, 9, 2, 8, 4, 5, 9, 5, 2, 7, 4, 8, 6, 3, 1, 8, 3, 4, 5, 1, 6, 9, 7, 2, 1, 4, 3, 8, 7, 5, 2, 9, 6, 2, 7, 5, 9, 6, 3, 4, 1, 8, 6, 8, 9, 1, 2, 4, 7, 5, 3]
finished. elapsed time = 5.1607571 seconds
[simon@fedora killer]$ 


Optimized C# Killer2... using more loops in place of Linq, and a few other optimizations to cache rules per cell and related cells
==================================================================================================================================

[simon@fedora killer2]$ dotnet run
started...
[5, 2, 8, 4, 3, 7, 1, 6, 9, 4, 9, 6, 2, 5, 1, 3, 8, 7, 3, 1, 7, 6, 8, 9, 5, 2, 4, 7, 6, 1, 3, 9, 2, 8, 4, 5, 9, 5, 2, 
 7, 4, 8, 6, 3, 1, 8, 3, 4, 5, 1, 6, 9, 7, 2, 1, 4, 3, 8, 7, 5, 2, 9, 6, 2, 7, 5, 9, 6, 3, 4, 1, 8, 6, 8, 9, 1, 2, 4, 7, 5, 3]
finished. elapsed time = 1.9519834 seconds
[simon@fedora killer2]$ 
