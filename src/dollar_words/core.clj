(ns dollar-words.core
  (:require [clojure.java.io :as io])
)

(def my-name "dollar-words")
(def max-args 2)
(defn parse-args
  "Parse arguments"
  [args]
  (cond
    (= (count args) 0)
      ((println "Error: expected filename")
       (println "USAGE:" my-name "<filename>")
       (System/exit 1))
    (> (count args) max-args)
      ((println "Error: too many arguments provided")
       (System/exit 1)))
)
(defn is-verbose?
  "Is the verbose argument passed to the program?"
  [args]
  (if (not= (count args) max-args)
    false
    (if (or (= (second args) "-v") (= (second args) "--verbose"))
      true false))
)
(defn letter-val
  "Returns the value of a letter: a=1, b=2, etc."
  [letter]
  (if (Character/isLetter (cast Character letter))
    (+ 1 (- (int letter) (int \a)))
    0
  )
)
(defn dollar-word?
  "Determines if the word is a dollar word or not
   Has the side effect of printing the word to stdout"
  [word verbose]
  (if (= 100 (reduce + (map letter-val (seq (.toLowerCase word)))))
    ; print the dollar word and return true
    (do (if verbose (println word))
        true)
    ; otherwise just return false
    false)
)
(defn process-file
  "Read the file"
  [filename verbose]
  (with-open [rdr (io/reader filename)]
    (count (filter #(dollar-word? % verbose) (line-seq rdr))))
)
(defn -main [& args]
  (parse-args args)
  (def filename (first args))
  (def verbose (is-verbose? args))
  (if (not (.exists (io/as-file filename)))
    ((println "Error: given file" filename "does not exist")
     (System/exit 1)))
  (println "dollar words:" (process-file filename verbose))
)
