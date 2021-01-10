package main

import (
	"encoding/csv"
	"fmt"
	"io"
	"log"
	"os"
	"strconv"
	"time"
)

var related [81][81]byte
var start = time.Now()
var rule [81][]int

func main() {

	// Open the file
	csvfile, err := os.Open("/home/simon/Scala/killer/killer/resources/puzzle.csv")
	if err != nil {
		log.Fatalln("Couldn't open the csv file", err)
	}

	// Parse the file
	r := csv.NewReader(csvfile)
	//r := csv.NewReader(bufio.NewReader(csvfile))
	r.FieldsPerRecord = -1

	var parms [][]int

	// Iterate through the records
	for {
		// Read each record from csv
		record, err := r.Read()
		if err == io.EOF {
			break
		}
		if err != nil {
			log.Fatal(err)
		}
		var recordInt []int
		for _, j := range record {
			tempInt, err := strconv.Atoi(j)
			if err == nil {
				recordInt = append(recordInt, tempInt)
			}
		}
		parms = append(parms, recordInt)
	}

	//put the rull array into the appropriate cell rule
	for _, p := range parms {
		for c := 1; c < len(p); c++ {
			rule[p[c]] = p
		}
	}

	fmt.Println(parms)

	var startBoard [81]int

	recursiveCheck(startBoard, parms)
}

func sameRow(i int, j int) bool {
	return (i/9)>>0 == (j/9)>>0
}
func sameCol(k int, l int) bool {
	return (k-l)%9 == 0
}
func sameBlock(m int, n int) bool {
	return ((m/27)>>0 == (n/27)>>0 && ((m%9)/3)>>0 == ((n%9)/3)>>0)
}
func indexOf(array [81]int, val int) int {
	for i := range array {
		if array[i] == val {
			return i
		}
	}
	return -1
}

func checkRow(aRow [81]int, parms [][]int, i int, m int) bool {
	var selectedParm = rule[i]
	var total = m
	for x := 1; x < len(selectedParm); x++ {
		if selectedParm[x] != i {
			if aRow[selectedParm[x]] == 0 {
				return true
			}
			total = total + aRow[selectedParm[x]]
		}
	}

	if total != selectedParm[0] {
		return false
	}
	return true
}

func recursiveCheck(inBoard [81]int, parms [][]int) bool {
	var i = indexOf(inBoard, 0)
	if i == -1 {
		fmt.Println(inBoard)
		end := time.Now()
		fmt.Println(start)
		fmt.Println(end)
		os.Exit(0)
	}

	var excludedNumbers = make(map[int]bool)
	var x int = 0
	var y int = 1

	for x < i {
		if related[i][x] == 0 {
			if sameRow(i, x) || sameCol(i, x) || sameBlock(i, x) {
				related[i][x] = 'Y'
			} else {
				related[i][x] = 'N'
			}
		}
		if related[i][x] == 'Y' {
			excludedNumbers[inBoard[x]] = true
		}

		x++
	}

	for y < 10 {
		if !excludedNumbers[y] {
			if checkRow(inBoard, parms, i, y) {
				inBoard[i] = y
				recursiveCheck(inBoard, parms)
			}
		}
		y++
	}
	return true
}
