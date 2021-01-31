function index(array,value)
	for i=0,#array do
		if array[i] == value then
			return i
		end	
	end
end

function table.copy(input)
	local output = {}
	for i=0,#input do
		output[i] = input[i]
	end
return output
end		

function get_rules()
	local rules = {}    
	local f = io.open("puzzle.csv","r")
	for line in f:lines() do
		local temp = {}
		for word in line:gmatch('[^,%s]+') do
			table.insert(temp, tonumber(word))
		end	
		rules[tonumber(temp[#temp])] = temp
	end
	return rules
end

function get_startBoard()
	local startBoard = {}
    for i= 0, 80 do
      startBoard[i] = 0
    end
    return startBoard
end    

function same_row(i,j)
    return (i // 9)  == (j // 9) ;
end

function same_col(k,l)
	if k > l then return (k - l) % 9 == 0
	else return (l - k) % 9 == 0
	end
end

function same_block(m,n)
        return (m // 27) == (n // 27) and ((m % 9) // 3) == ((n % 9) // 3) 
end

function get_validCombo()    
validCombo = {}          
    for i=0,80 do
      validCombo[i] = {}     
      for j=0,80 do
        validCombo[i][j] = same_row(i,j) or same_col(i,j) or same_block(i,j)
      end
    end
    return validCombo
end       

function checkRow(aRow, position, value) 

	local selectedRule = rules[position]
	if selectedRule ~= nil then
		for i,val in ipairs(selectedRule) do
				if i > 1 then
					value = value + aRow[val]
				end	
			end	
			if value ~= selectedRule[1] then
				return false
			else
				return true
			end
	return true
	end
return true
end

function recursiveCheck(inBoard)
	
	local pos = index(inBoard, 0)
	if pos == nil then
		print("finished")
	else	
		local excludedNumbers = {}
		local x = 0
		local y = 1
		while x < pos do
			if validCombos[pos][x] then 
				excludedNumbers[inBoard[x]] = true
			end	
			x = x + 1
		end

		while y < 10 do
			if excludedNumbers[y] == nil then	
				if pos == 80 then
					inBoard[80] = y
					local str = ""
					for i=0,#inBoard do
						str = str.." ".. inBoard[i]
					end
					print(str)	
					print(string.format("elapsed time: %.2f\n", os.clock() - x))
					os.exit()
				end	
				if checkRow(inBoard,pos,y) then
				    local newBoard = table.copy(inBoard)
					newBoard[pos] = y 
					recursiveCheck(newBoard)
				end	
			end
		y = y + 1
		end
	end
	return true
end


x = os.clock()
rules = get_rules()
validCombos = get_validCombo()
local startBoard = get_startBoard()
recursiveCheck(startBoard)


