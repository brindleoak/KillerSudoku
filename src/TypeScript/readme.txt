PS /home/simon/Killer/TypeScript> tsc killer.ts 

PS /home/simon/Killer/TypeScript> node killer.js

[
  5, 2, 8, 4, 3, 7, 1, 6, 9, 4, 9, 6,
  2, 5, 1, 3, 8, 7, 3, 1, 7, 6, 8, 9,
  5, 2, 4, 7, 6, 1, 3, 9, 2, 8, 4, 5,
  9, 5, 2, 7, 4, 8, 6, 3, 1, 8, 3, 4,
  5, 1, 6, 9, 7, 2, 1, 4, 3, 8, 7, 5,
  2, 9, 6, 2, 7, 5, 9, 6, 3, 4, 1, 8,
  6, 8, 9, 1, 2, 4, 7, 5, 3
]
1.814
PS /home/simon/Killer/TypeScript> 


Version 2 is a re-write using a pure functional approach... no mutatiions.
So this is looking very close to the Scala, Haskell, Elm veriants now.
[simon@nixos:~/GitHub/TsKiller]$ time node killer.js
5 2 8 4 3 7 1 6 9 4 9 6 2 5 1 3 8 7 3 1 7 6 8 9 5 2 4 7 6 1 3 9 2 8 4 5 9 5 2 7 4 8 6 3 1 8 3 4 5 1 6 9 7 2 1 4 3 8 7 5 2 9 6 2 7 5 9 6 3 4 1 8 6 8 9 1 2 4 7 5 3

real    0m1.511s
user    0m1.529s
sys     0m0.028s
