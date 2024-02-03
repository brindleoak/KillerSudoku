module Killer exposing (..)

import Array exposing (Array, get, set)
import Csv exposing (rulesFile)
import Dict exposing (Dict, fromList)
import Html exposing (Html, a, i, text)
import List exposing (map, range)
import List.Extra exposing (andThen, elemIndex)
import Shared exposing (tl, toInt)
import String exposing (fromInt)
import Tuple exposing (second)


type alias Position =
    ( Bool, Array Int )


type alias Rules =
    Dict Int (List Int)


main : Html msg
main =
    let
        rules : Rules
        rules =
            processRulesCsv rulesFile

        solution : Position
        solution =
            recursiveCheck ( False, List.repeat 81 0 |> Array.fromList ) rules
    in
    text
        (solution
            |> second
            |> Array.toList
            |> map fromInt
            |> String.join ", "
        )


processRulesCsv : String -> Rules
processRulesCsv csv =
    csv
        |> String.lines
        |> List.map (String.split ",")
        |> List.map (List.map toInt)
        |> andThen
            (\el ->
                tl el
                    |> List.map
                        (\cell -> ( cell, el ))
            )
        |> List.sortBy (\( a, _ ) -> a)
        |> Dict.fromList


relatedCells : Dict Int (List Int)
relatedCells =
    let
        sameRow : Int -> Int -> Bool
        sameRow i j =
            i // 9 == j // 9

        sameCol : Int -> Int -> Bool
        sameCol i j =
            modBy 9 (i - j) == 0

        sameBlock : Int -> Int -> Bool
        sameBlock i j =
            ((i // 27) == (j // 27)) && ((modBy 9 i // 3) == (modBy 9 j // 3))

        isRelated : Int -> Int -> Bool
        isRelated cell el =
            cell /= el && (sameRow cell el || sameCol cell el || sameBlock cell el)

        getRelatedCells : Int -> ( Int, List Int )
        getRelatedCells cell =
            ( cell, List.filter (isRelated cell) (range 0 80) )
    in
    List.map getRelatedCells (range 0 80) |> Dict.fromList


recursiveCheck : Position -> Rules -> Position
recursiveCheck ( solved, board ) rules =
    case elemIndex 0 (Array.toList board) of
        Just i ->
            let
                validAttempt : Int -> Bool
                validAttempt m =
                    let
                        getRule rules_ =
                            rules_ |> Dict.get i |> Maybe.withDefault []

                        getCell pos =
                            get pos board |> Maybe.withDefault 0
                    in
                    not
                        (relatedCells
                            |> getRule
                            |> List.map getCell
                            |> List.any ((==) m)
                        )
                        && ((rules
                                |> getRule
                                |> List.reverse
                                |> List.head
                                |> Maybe.withDefault 0
                            )
                                /= i
                                || (rules
                                        |> getRule
                                        |> List.tail
                                        |> Maybe.withDefault []
                                        |> List.map getCell
                                        |> List.sum
                                   )
                                + m
                                == (rules |> getRule |> List.head |> Maybe.withDefault 0)
                           )

                step : Int -> ( Bool, Array Int ) -> ( Bool, Array Int )
                step m ( solved_, board_ ) =
                    if not solved_ && validAttempt m then
                        case recursiveCheck ( False, set i m board_ ) rules of
                            ( False, _ ) ->
                                ( False, board )

                            solvedBoard ->
                                solvedBoard

                    else
                        ( solved_, board_ )
            in
            range 1 9 |> List.foldl step ( False, board )

        Nothing ->
            ( True, board )
