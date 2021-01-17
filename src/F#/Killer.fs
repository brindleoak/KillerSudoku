// Learn more about F# at http://docs.microsoft.com/dotnet/fsharp

open System
open System.IO
open System.Diagnostics.CodeAnalysis

let t1 = DateTime.Now

let checkAllRules (board: int []) (rules: Map<int, int []>) (i: int) (m: int) =

    match rules.TryFind i with
    | Some p ->
        let cells =
            p.[1..p.Length - 2]
            |> Seq.map (fun f -> board.[f])
            |> Seq.append (Seq.singleton m)

        if not (Seq.exists ((=) 0) cells) then
            p.[0] = Seq.sum cells
        else
            true
    | None -> true

//Check the board is invalid - the value m at position i is not allowed
let checkBoardInvalid (board: int []) i m: bool =

    let sameRow i j = i / 9 = j / 9
    let sameCol i j = (i - j) % 9 = 0
    let sameBlock i j = ((i / 27 = j / 27) && (i % 9 / 3 = j % 9 / 3))

    seq { 0 .. 80 }
    |> Seq.tryFind
        (fun x ->
            if i <> x && board.[x] <> 0 then
                if sameRow i x || sameCol i x || sameBlock i x then
                    board.[x] = m
                else
                    false
            else
                false)
    |> function
        | Some _ -> true
        | None -> false

let rec recursiveCheck (board: int []) (rules: Map<int, int []>) =
    
    let i = 
        try
          Array.findIndex (fun e -> e = 0) board
        with  ex -> 
          printfn "%A" board
          let t2 = DateTime.Now - t1
          printfn "%f" t2.TotalSeconds
          Environment.Exit(0)
          0

    seq { 1 .. 9 }
        |> Seq.iter
            (fun m ->
                if not (checkBoardInvalid board i m) then
                    if checkAllRules board rules i m then
                        let newBoard = Array.copy board
                        Array.set newBoard i m
                        recursiveCheck newBoard rules |> ignore)

[<EntryPoint>]
let main argv =

    let rules =
        File.ReadLines("/home/simon/F#/Killer/puzzle.csv") // fsharplint:disable-line Hints
        |> Seq.map (fun f -> (f.Split(',') |> Seq.map int |> Seq.toArray))
        |> Seq.map (fun x -> (x.[x.Length - 1], x))
        |> Map.ofSeq

    let board: int array = Array.zeroCreate 81

    printf "started...\n"
    recursiveCheck board rules |> ignore
    0
