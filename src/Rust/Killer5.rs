use std::collections::HashSet;
use std::collections::HashMap;
use std::process;
use std::fs::File;
use std::io::{BufRead, BufReader};
use std::time::{SystemTime};


fn main() {
    
	let start = SystemTime::now();
    let f = File::open("puzzle.csv").unwrap();
    let f = BufReader::new(f);
	let values: Vec<Vec<usize>> = f.lines()
		.filter_map(
		|l| l.ok().map(
			|s| s.split(",")
			.filter_map(|word| word.parse().ok())
			.collect()))
		.collect();
	let mut parms = HashMap::new();
    
    for v in &values {
		parms.insert( v[v.len()-1], v);
        }
    println!("{:?}", parms);	
    
    				    
	let  mut grid_rules:[[usize;81];81]=[[0;81];81];

//for (&i, &_val ) in parms.iter() {
	let mut a = 0;
	while a < 81
	{	
		let mut x = 0;
		while  x < a
			{
				if same_row(a, x) || same_col(a, x) || same_block(a, x) {
                    grid_rules[a][x] = 1;
				}
             x = x+1;   
            }   
     a = a+1;        
    }

    fn same_row(i: usize, j: usize) -> bool {
        return (i / 9) >> 0 == (j / 9) >> 0;
    }

    fn same_col(k: usize, l: usize) -> bool {
        if k > l {
            return (k - l) % 9 == 0;
        } else {
            return (l - k) % 9 == 0;
        }
    }

    fn same_block(m: usize, n: usize) -> bool {
        return (m / 27) >> 0 == (n / 27) >> 0 && ((m % 9) / 3) >> 0 == ((n % 9) / 3) >> 0;
    }

    fn check_row(row_to_check: &mut Vec<usize>, vals: &HashMap<usize,&Vec<usize>>) -> bool {
        let pos: usize;
        if row_to_check.iter().position(|&x| x == 0).is_none() {
            pos = 80;
        } else {
            pos = row_to_check.iter().position(|&s| s == 0).unwrap();
        }
        if vals.contains_key(&(pos-1)) {
            let selected_parm: Vec<usize> = vals.get(&(pos -1)).unwrap().to_vec();
            let sub_parm = &selected_parm[1..selected_parm.len()];
            let total = sub_parm
                .iter()
                .map(|&cell_position| row_to_check[cell_position])
                .fold(0, |total, next| total + next);
            if total != selected_parm[0] {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    fn recursive_check(in_board: &[usize], vals: &HashMap<usize,&Vec<usize>>, grid:&[[usize; 81]; 81], start: &SystemTime) -> bool {
        if in_board.iter().position(|&x| x == 0).is_none() {
            return true;
        } else {
            let i: usize = in_board.iter().position(|&s| s == 0).unwrap();
            let mut excluded_numbers: HashSet<usize> = vec![].into_iter().collect();
            let mut x: usize = 0;
            let mut y: usize = 1;

            while x < i {
                if grid[i][x] == 1 {
                    excluded_numbers.insert(in_board[x]);
					//println!("{} and {}", i,x);
					//println!("{:?}", grid[i][x]);
                }

                x = x + 1;
            }

            while y < 10 {
                if !excluded_numbers.contains(&y) {
                    let mut new_board: Vec<usize> =
                        [&in_board[..i], &[y], &in_board[(i + 1) ..]].concat();
                    if check_row(&mut new_board, vals) {
                        if i < 80 {
                            recursive_check(&new_board, &vals, &grid, &start);
                        } else {
                            println!("resolved board: {:?}", new_board);
							match start.elapsed() {
							Ok(elapsed) => {
							// it prints '2'
								println!("{}", (elapsed.as_secs() * 1_000) + (elapsed.subsec_nanos() / 1_000_000) as u64);
							}
							Err(e) => {
							// an error occurred!
								println!("Error: {:?}", e);
							}
   }
                            process::exit(1);
                        }
                    }
                }
                y = y + 1;
            }
            return true;
        }
    }

    let initial_row: [usize; 81] = [0; 81];
    recursive_check(&initial_row, &parms, &grid_rules, &start);
}