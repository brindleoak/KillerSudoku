import sys
import csv
from datetime import datetime

related = [[None]*81 for _ in range(81)]

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

def recursive_check(a):
	global b
	i = a.find('0')
	if i == -1:
		dateTimeObj = datetime.now()
		timestampStr = dateTimeObj.strftime("%d-%b-%Y (%H:%M:%S.%f)")
		print('End Timestamp   : ', timestampStr)
		print(a)
		sys.exit(0)
		
	excluded_numbers = set()
	for j in range(i):
		if related[i][j] == None:
			if (i//9 == j//9) or (i-j) % 9 == 0 or (i//27 == j//27 and i%9//3 == j%9//3):
				related[i][j] = 1
			else:
				related[i][j] = 0
		if related[i][j] == 1:		
			if a[j] != '0':
				excluded_numbers.add(a[j])

	for m in '123456789':
		if m not in excluded_numbers:
			if check_row(a,i,m):
				b = a[:i]+m+a[i+1:]
				recursive_check(b)
      
if __name__ == '__main__':
	board = "000000000000000000000000000000000000000000000000000000000000000000000000000000000"
	dateTimeObj = datetime.now()
	timestampStr = dateTimeObj.strftime("%d-%b-%Y (%H:%M:%S.%f)")
	print('Start Timestamp : ', timestampStr)
	get_parms()
	recursive_check(board)		