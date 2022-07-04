package nl.jolanrensen.gradleCrossbuildSample

private val scalaCompatVersion = "2.12" 
private val scalaVersion = "2.12.8" 
private val sparkVersion = "3.3.0" 
private val sparkMinorVersion = "3.3" 

// TODO - To run this, mark gradle-crossbuild-sample-kotlin/src/main/kotlin as sources root
// TODO - and unmark gradle-crossbuild-sample-kotlin/build/java-comment-preprocessor/preprocessMain
// TODO - I'm working on this
fun main() {
    println("Read spark version as $sparkVersion, scala version as $scalaVersion, scala compat version as $scalaCompatVersion, spark minor version as $sparkMinorVersion")

    //JCP! if scalaCompat < 2.13
    println("This doesn't work on scala 2.13: ${scala.collection.mutable.WrappedArray::class.qualifiedName}")
    //JCP! endif
}
