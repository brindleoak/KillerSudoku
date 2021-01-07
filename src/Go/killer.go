package main

import (
	"encoding/csv"
	"fmt"
	"io"
	"log"
	"os"
	"strconv"
	"sort"
	"time"
)

func main() {
	
	start := time.Now()
	// Open the file
	csvfile, err := os.Open("/home/sandy/Documents/puzzle.csv")
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
		for _,j := range record {
		   tempInt, err := strconv.Atoi(j)
		   if err == nil {
		   recordInt = append(recordInt,tempInt)
			}
		}
		parms = append(parms,recordInt)
		//fmt.Println(record)
	}
	sort.SliceStable(parms, func(i, j int) bool {
		return parms[i][len(parms[i])-1] < parms[j][len(parms[j])-1]
	})

	fmt.Println(parms)

	var startBoard [81]int


	recursiveCheck(startBoard, parms)
	
	end := time.Now()
	fmt.Println(start)
	fmt.Println(end)
}

func FilterParms(vs [][]int, f func([]int) bool) []int {
	vsf := make([]int, 0)
	for _, v := range vs {
		if f(v) {
			vsf = v
			return vsf
		}
	}
	return vsf
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
func IndexOf(array [81]int, val int) int {
	for i, v := range array {
		if v == val {
			return i
		}
	}
	return -1
}

func checkRow(aRow [81]int, parms [][]int) bool {
	var pos = IndexOf(aRow, 0)
	var selectedParm = (FilterParms(parms, func(v []int) bool {
		return v[len(v)-1] == pos-1
	}))
//	fmt.Println(selectedParm)

	if len(selectedParm) > 0 {
		var total = 0
		for i, j := range selectedParm {
			if i > 0 {
				total = total + aRow[j]
			}
		}
//		fmt.Println(total)
		if total != selectedParm[0] {
			return false
		} else {
			return true
		}
	} else {
		return true
	}
}
func recursiveCheck(inBoard [81]int, parms [][]int) bool {
	var i = IndexOf(inBoard, 0)
	if i == -1 {
		fmt.Println(inBoard)
		return true
	}

	var excludedNumbers = make(map[int]bool)
	var x int = 0
	var y int = 1

	for x < i {
		if sameRow(i, x) || sameCol(i, x) || sameBlock(i, x) {
			excludedNumbers[inBoard[x]] = true
		}

		x++
	}

	for y < 10 {
		if !excludedNumbers[y] {
			var newBoard [81]int 
			copy( newBoard[:],inBoard[0:81])
			newBoard[i] = y
			if checkRow(newBoard,parms) {
			recursiveCheck(newBoard, parms)
			}
		}
		y++
	}
	return true
}
