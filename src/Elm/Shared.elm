module Shared exposing (..)

import Array exposing (Array)


hd : List Int -> Int
hd l =
    case l of
        [] ->
            -1

        x :: _ ->
            x


tl : List Int -> List Int
tl l =
    case l of
        [] ->
            []

        _ :: xs ->
            xs


toInt : String -> Int
toInt s =
    Maybe.withDefault -1 (String.toInt s)
