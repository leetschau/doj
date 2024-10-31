(ns doj.main
  (:require
    [clojure.java.io :as io]
    [babashka.cli :as cli]
    [babashka.fs :as fs]
    [clj-yaml.core :as yaml]
    [doj.searcher :as sr]
    [doj.config :as conf]))

(defn list-note [{{:keys [note-number]} :opts}]
  (println "list notes:" note-number))

(defn edit-note [{{:keys [note-index]} :opts}]
  (println "edit note:" note-index))

(defn help [{{:keys [topic]} :opts}]
  (let [docs (yaml/parse-string (slurp "./help.yml"))]
    (case topic
      nil (print (:global docs))
      "l" (print (:l docs))
      "e" (print (:e docs))
      "s" (print (:s docs)))))

(def table
  [{:cmds ["conf" "get"]
    :fn conf/get-config
    :args->opts [:key-name]}
   {:cmds ["conf" "set"]
    :fn conf/set-config!
    :args->opts [:ckey :cval]}
   {:cmds ["conf" "reset"]
    :fn conf/reset-configs!}
   {:cmds ["e"]
    :fn edit-note
    :args->opts [:note-index]
    :exec-args {:note-index 1}
    :validate {:note-number
               {:pred pos?
                :ex-msg (fn [arg] (str "Invalid arguments: "
                                       (:value arg)
                                       ", note number must be a positive integer!"))}}}
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
   {:cmds ["h"]
    :fn help
    :args->opts [:topic]}
   {:cmds []
    :fn help}])

(defn -main [args]
  (cli/dispatch table args))

(-main *command-line-args*)
