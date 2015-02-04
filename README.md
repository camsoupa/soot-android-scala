# soot-android-scala
Runs soot analysis on an android apk from within Scala

## Get sbt for your platform (and all the scalas are yours)

http://www.scala-sbt.org/

## Then run this project on a sample apk:

```
$ git clone <this-repo>
$ cd soot-android-scala
$ sbt "run platforms 1.apk"
```

The platforms/ directory is placeholder.  You will want to get more platforms using the Android SDK Manager.
