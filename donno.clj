#!/usr/bin/env bb

(require '[babashka.cli :as cli])
(require '[donno.notes :as notes])

(def list-opts {
  :args->opts [:note-no]
  :desc "list specified number of notes"
  :default 1
})

(def table [
  {:cmds ["l"] :fn notes/list-notes :spec list-opts}
])

(cli/dispatch table *command-line-args*)
