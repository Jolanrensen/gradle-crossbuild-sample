package nl.jolanrensen.gradleCrossbuildSample


object Main {

    val scalaCompatVersion = /*$"\""+scalaCompat+"\""$*/ /*-*/ ""
    val scalaVersion = /*$"\""+scala+"\""$*/ /*-*/ ""
    val sparkVersion = /*$"\""+spark+"\""$*/ /*-*/ ""
    val sparkMinorVersion = /*$"\""+sparkMinor+"\""$*/ /*-*/ ""

    // TODO - To run this, mark gradle-crossbuild-sample-scala/src/main/scala as sources root
    // TODO - and unmark gradle-crossbuild-sample-scala/build/java-comment-preprocessor/preprocessMain
    // TODO - I'm working on this
    def main(args: Array[String]): Unit = {
        println(f"Read spark version as $sparkVersion, scala version as $scalaVersion, scala compat version as $scalaCompatVersion, spark minor version as $sparkMinorVersion")

        //#if scalaCompat < 2.13
        //$println("This doesn't work on scala 2.13: ${scala.collection.mutable.WrappedArray::class.qualifiedName}")
        //#endif
    }
}