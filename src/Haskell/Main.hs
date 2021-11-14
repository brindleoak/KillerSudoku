{-# LANGUAGE BlockArguments #-}
{-# LANGUAGE TupleSections #-}
module Main where

import Data.List.Split(splitOn)
import qualified GHC.List as L
import qualified Data.Vector.Unboxed as V
import GHC.Arr (Array (Array), array,(!))
import GHC.Base (join)
import Debug.Trace (trace)

main = do
    rulesFile <- readFile "puzzle.csv"
    let rules = getRules rulesFile
    let solution = recursiveCheck (False, V.fromList (replicate 81 0)) rules
    print solution

getRules s =
    let l = (map (map (read::String->Int) . splitOn ",") . lines) s
        f =  map (\l -> map (, l) (tail l)) l
    in  array (0, 80) (concat f)

relatedCells = array (0, 80) (map (\x ->  (x, getRelatedCells x)) [0..80])
    where
        sameRow i j = i `quot` 9 == j `quot` 9
        sameCol i j = (i - j) `mod` 9 == 0
        sameBlock i j = (i `quot` 27 == j `quot` 27) && (i `rem` 9 `quot` 3 == j `rem` 9 `quot` 3)
        isRelated cell el = cell /= el && (sameRow cell el || sameCol cell el || sameBlock cell el)
        getRelatedCells cell = filter (isRelated cell) [0..80]

recursiveCheck (True, board) rules = (True, board)  
recursiveCheck (solved, board) rules =
    let zeroCell = V.elemIndex 0 board
    in case zeroCell of
        Nothing -> (True, board)
        Just i -> foldl step (False, board) [1 .. 9]
                  where
                     validAttempt m = not (L.elem m (map (board V.!) (relatedCells!i)))
                                       && (last (rules!i) /= i
                                           || sum (map (board V.!) (tail(rules!i))) + m == head (rules!i))
                     step acc m
                        | fst acc = acc
                        | validAttempt m = recursiveCheck (False, board V.// [(i, m)]) rules
                        | otherwise = (False, board)
