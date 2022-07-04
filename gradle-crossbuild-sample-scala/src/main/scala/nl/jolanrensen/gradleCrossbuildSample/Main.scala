package nl.jolanrensen.gradleCrossbuildSample

private val scalaCompatVersion = /*$"\""+scalaCompat+"\""$*/ /*-*/ ""
private val scalaVersion = /*$"\""+scala+"\""$*/ /*-*/ ""
private val sparkVersion = /*$"\""+spark+"\""$*/ /*-*/ ""
private val sparkMinorVersion = /*$"\""+sparkMinor+"\""$*/ /*-*/ ""

// TODO - To run this, mark gradle-crossbuild-sample-kotlin/src/main/kotlin as sources root
// TODO - and unmark gradle-crossbuild-sample-kotlin/build/java-comment-preprocessor/preprocessMain
// TODO - I'm working on this
fun main() {
    println("Read spark version as $sparkVersion, scala version as $scalaVersion, scala compat version as $scalaCompatVersion, spark minor version as $sparkMinorVersion")

    //#if scalaCompat < 2.13
    //$println("This doesn't work on scala 2.13: ${scala.collection.mutable.WrappedArray::class.qualifiedName}")
    //#endif
}