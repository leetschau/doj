(ns doj.config
  (:require
    [babashka.fs :as fs]
    [clojure.java.io :as io]
    [cheshire.core :as json]))

(def config-path (fs/file (fs/xdg-config-home) "donno.json"))

(def default-configs {:editor "nvim"
                      :viewer "nvim -R"
                      :app-home "~/.donno"
                      :default-notebook "/Misc"})

(defn load-configs!
  [inpfile]
  (with-open
    [rdr (io/reader inpfile)]
    (json/parse-stream rdr true)))

(defn save-configs!
  [filepath configs]
  (spit config-path
        (json/generate-string configs)))

(defn reset-configs! [_]
  (save-configs! config-path default-configs))

(def configs
  (if (fs/exists? config-path)
      (load-configs! config-path)
      (do (reset-configs!)
          default-configs)))

(defn get-config
  [{{:keys [key-name]} :opts}]
  (println
    (if (some? key-name)
        (get configs (keyword key-name))
        configs)))

(defn set-config!
  [{{:keys [ckey cval]} :opts}]
  (let [new-configs (assoc configs (keyword ckey) cval)]
    (save-configs! config-path new-configs)
    (println "Configuration updated:" new-configs)))

