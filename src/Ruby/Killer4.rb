require 'csv'

def sameRow(i,j)
	return (i/9)>>0 == (j/9)>>0
end

def sameCol(k,l)
	return (k-l)%9 == 0
end

def sameBlock(m,n)
	return ((m/27)>>0 == (n/27)>>0 && ((m%9)/3)>>0 == ((n%9)/3)>>0)
end

def checkRow(aRow, position, value) 
	if position !=nil
		selectedParm = $parms[position]
		if selectedParm != nil
			total = selectedParm.slice(1..-2).collect{|e| aRow[e]}.reduce(value){ |sum, num| sum + num }
			if total != selectedParm[0] 
				return false
			end
		end
	end			
	return true
end

def recursiveCheck(inBoard)
	pos = inBoard.index(0)
	if pos == nil
		puts "#{inBoard}"
		puts "#{Time.now}"
		exit!(true)
	else	
		excludedNumbers = Hash.new
		x = 0
		y = 1

		while x < pos
			if $checks[pos][x] == nil
				if sameRow(pos, x) || sameCol(pos, x) || sameBlock(pos, x) 
					$checks[pos][x] = true
					excludedNumbers[inBoard[x]] = true
				end	
			else
				excludedNumbers[inBoard[x]] = true
			end	
			x += 1
		end

		while y < 10 
			if excludedNumbers[y] == nil 
				
				if checkRow(inBoard,pos,y) 
				    newBoard = inBoard.dup() 
					newBoard[pos] = y 
					recursiveCheck(newBoard)
				end	
			end
		y += 1
		end
	end
	return true
end

puts "#{Time.now}"
$parms = CSV.read("/home/sandy/puzzle.csv",converters: :integer).to_h{ |x| [x.last(), x]}
nums = Array.new(81) {|e| e = 0}
puts "#{$parms}"
$checks = Array.new(81){Array.new(81)}
recursiveCheck(nums)
