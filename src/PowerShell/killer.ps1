$rules = New-Object 'object[]' 81
$puzzle = Import-Csv -Path .\puzzle.csv -Header 'Total', 'Cell1', 'Cell2', 'Cell3', 'Cell4'
foreach ($line in $puzzle) {
    $t = $line.Total -as [int]
    $c1 = $line.Cell1 -as [int]
    $c2 = $line.Cell2 -as [int]
    $c3 = $line.Cell3 -as [int]
    $c4 = $line.Cell4 -as [int]
    if ($c3 -eq 0) {
        $rules[$c1] = $t, $c1, $c2
        $rules[$c2] = $t, $c1, $c2
    }
    elseif ($c4 -eq 0) {
        $rules[$c1] = $t, $c1, $c2, $c3
        $rules[$c2] = $t, $c1, $c2, $c3
        $rules[$c3] = $t, $c1, $c2, $c3
    }
    else {
        $rules[$c1] = $t, $c1, $c2, $c3, $c4
        $rules[$c2] = $t, $c1, $c2, $c3, $c4
        $rules[$c3] = $t, $c1, $c2, $c3, $c4
        $rules[$c4] = $t, $c1, $c2, $c3, $c4
    }    
}
function Test-Rules {param ([int[]]$b, [int]$i, [int]$m)

    $p = $rules[$i]
    $sum = $m

    for ($x = 1; $x -lt $p.Length; $x++) {
        if ($p[$x] -ne $i) {
            if ($board[$p[$x]] -eq 0) {
                return $true;
            }
            $sum += $b[$p[$x]];
        }
    }

    if ($sum -eq $p[0]) {
        return $true;
    }
    else {
        return $false;
    }
}

function Test-Cell {param ([int[]]$b, [int]$i, [int]$m)

	if (-Not (Test-Rules $b  $i  $m)) {
        return $false
    }

	for ([int]$j = 0; $j -lt $i; $j++)
	{
		if ($i -ne $j)
		{
            if (([Math]::Floor($i / 9) -eq [Math]::Floor($j / 9)) -or
                (($i - $j) % 9 -eq 0) -or
                ((([Math]::Floor($i / 27) -eq [Math]::Floor($j / 27)) -and ([Math]::Floor(($i % 9) / 3) -eq [Math]::Floor(($j % 9) / 3))))) {
				if ($b[$j] -eq $m)
				{
					return $false;
				}
			}
		}
	}

    return $true;
}

function Test-Recursive {param ([int[]]$board)
    
    [int] $i = $board.IndexOf(0)
    
    if ($i -eq -1) {
        Write-Host $board
        exit
    }

    foreach ($m in 1..9) {
        if (Test-Cell $board $i $m) {
            [int[]]$newBoard = $board.Clone()
            $newBoard[$i] = $m
            Test-Recursive $newBoard
        }
    }
}

$board = New-Object int[] 81
Test-Recursive $board
