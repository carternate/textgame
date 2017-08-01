(ns brcagame.core
  (:gen-class))

(def ^:dynamic *game* (atom {}))

(defmacro defcmd [name args body]
  `(def ~name (fn ~args
                (compare-and-set! *game* @*game*
                                  ~body)
                (str (:response @*game*)))))

;;;
;; Here's a sample function showing how to use defcmd.
;; Every time you call (counter) it will add 1 to a 
;; count field :counter tracked in @*game* and print
;; out the current count. To directly see the state of
;; @*game* just evalute @*game* in the REPL.

(defcmd counter [] 
  (-> @*game*
      ;; The following (or ...) form just helps bootstrap a 0 into the first call
      (assoc :counter (+ 1 (or (:counter @*game*) 0)))
      ;; Note that this copy of @*game* is still the previous value. 
      ;; Updates to @*game* aren't readable by your code until after this 
      ;; cmd exits.
      (assoc :response (str "The old count was " (:counter @*game*)))))
