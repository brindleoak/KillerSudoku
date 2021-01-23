import sys
import csv
from datetime import datetime

def same_row(i,j): return (i//9 == j//9)  # Floor division of integers
def same_col(i,j): return (i-j) % 9 == 0   # if difference of i and j is 9 then they are in the same column
def same_block(i,j): return (i//27 == j//27 and i%9//3 == j%9//3)   #modulus and floor division to see if numbers are in the same block 
def get_parms():

	global parms
	parms = {}
	with open('puzzle.csv') as csvfile:
		parm_rows = csv.reader(csvfile, delimiter=',', quotechar='|')
		for row in parm_rows:
			parms[int(row[len(row)-1])] = row

def check_row(aRow,indexOfZero,newVal):
	if indexOfZero in parms:
		parm = parms[indexOfZero]        
		cells = parm[1:len(parm)]
		total = int( parm[0])
		sum = int(newVal)
		for t in cells:
			cell = (int(aRow[int(t)]))
			sum = sum + cell
		if sum != total: return False
	return True        

        
def display(values):
    x = 0
    y = ""
    while x <9:
        y = y + values[(x*9):((x+1)*9)] + '\r' + '\n'  
        x = x+1
    return y            
def recursive_check(a):
	global b
	i = a.find('0')
	if i == -1:
		dateTimeObj = datetime.now()
		timestampStr = dateTimeObj.strftime("%d-%b-%Y (%H:%M:%S.%f)")
		print('Current Timestamp : ', timestampStr)
		sys.exit(display(a))
	excluded_numbers = set()
	for j in range(i):
		if same_row(i,j) or same_col(i,j) or same_block(i,j):
			if a[j] != '0':
				excluded_numbers.add(a[j])

	for m in '123456789':
		if m not in excluded_numbers:
			if check_row(a,i,m):
				b = a[:i]+m+a[i+1:]
				recursive_check(b)
      
if __name__ == '__main__':
	sys.argv = [sys.argv[0], "000000000000000000000000000000000000000000000000000000000000000000000000000000000"]

	if len(sys.argv) == 2 and len(sys.argv[1]) == 81:
		input = (sys.argv[1])
		print(display(input))
		dateTimeObj = datetime.now()
		timestampStr = dateTimeObj.strftime("%d-%b-%Y (%H:%M:%S.%f)")
		print('Current Timestamp : ', timestampStr)
    
		get_parms()
		print(parms)
		recursive_check(input)
 
	else:
		print('Usage: python sudoku.py')
		print('  where puzzle.csv is a csv file with the first delimited value in each row being the sum value of each demarkated area and the subsequent delimited values are the cell positions making up the block from 0 to 80')
		print((sys.argv[1]))		
	
	
