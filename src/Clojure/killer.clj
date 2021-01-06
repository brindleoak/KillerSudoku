(ns sudoku.core
   (:gen-class))

(def range80 (range 80))

(defn parse-csv
  "Convert a CSV into a list of a list of integers"
  [string]
  (letfn [(parse-vec-of-str [vec-of-str] (map #(Integer/parseInt %) vec-of-str))]
    (->> (clojure.string/split string #"\n")
         (map #(clojure.string/split % #","))
         (map #(parse-vec-of-str %)))))

(def parms (sort-by last (parse-csv (slurp "resources/puzzle.csv"))))

(defn excluded-numbers
  "For a given board and cell number, return a seq of the values in all related cells"
  [board i] 
  (letfn [(same-row   [a b] (= (quot a 9) (quot b 9)))
          (same-col   [a b] (= (rem (- a b) 9) 0))
          (same-block [a b] (and (= (quot a 27) (quot b 27))
                                 (= (quot (rem a 9) 3) (quot (rem b 9) 3))))]
  (reduce
    (fn [excl j]
      (if (and (not= i j)
               (or (same-row i j) (same-col i j) (same-block i j))
               (> (board j) 0))
        (conj excl (board j))
         excl))
    #{}
    range80)))

(defn check-rule
 "Check if the given rule fails on the given board. A rule is a list where the first item
  is the required total and the remaining items are the cell positions that must be summed"
 [board rule]
 (let [cells (map #(board %) (rest rule))]
  (if-not (some #{0} cells)
    (= (first rule) (reduce + cells))
    true
  )))

(defn check-all-rules
  "Check all the rules given in parms against the given board "
  [board filtered-parms]
  (reduce (fn [_ rule]
            (if-not (check-rule board rule)
              (reduced false)
              true))
          true
          filtered-parms))

(defn recursive-check
  "Finds the first non-zero cell then try the values 1, 2, 3...
   If the attempt satisfies the rules the recurse to solve this new position"
  [board]
  (let [first-zero (first (filter #(= (second %) 0) board))
        cell (first first-zero)
        filtered-parms (filter (fn [x] (= (last x) cell)) parms)]
    (if (nil? first-zero)
      (do (println board)
          (System/exit 0))
      (doseq [m [1 2 3 4 5 6 7 8 9]]
         (let [newboard (assoc board cell m)]
           (if (and (check-all-rules newboard filtered-parms)
                    (not (contains? (excluded-numbers newboard cell) m)))
             (recursive-check newboard)))))))

(defn -main []
  (let [board (into (sorted-map) (zipmap (range 0 81) (repeat 0)))]
    (println "started...")
    (recursive-check board)))
