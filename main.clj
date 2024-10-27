#!/usr/bin/env bb

(ns doj.main
  (:require
    [babashka.cli :as cli]
    [doj.searcher :as sr]))

(defn list-note [m]
  (println "list notes: " m))

(defn edit-note [m]
  (println "edit note:" m))

(defn help [m]
  (println "help:" m))

(def table
  [{:cmds ["e"]
    :fn edit-note
    :args->opts [:note-index]}
   {:cmds ["l"]
    :fn list-note
    :args->opts [:note-number]
    :exec-args {:note-number 5}
    :validate {:note-number
               {:pred pos?
                :ex-msg (fn [arg] (str "Invalid arguments: "
                                       (:value arg)
                                       ", note number must be a positive integer!"))}}}
   {:cmds ["s"]
    :fn sr/search-notes
    :coerce {:terms []}
    :args->opts (repeat :terms)}
   {:cmds []
    :fn help}])

(defn -main [args]
  (cli/dispatch table args))

(-main *command-line-args*)
