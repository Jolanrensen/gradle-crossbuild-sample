package nl.jolanrensen.gradleCrossbuildSample

private val kotlinVersion = /*$"\""+kotlin+"\""$*/ /*-*/ ""
private val scalaCompatVersion = /*$"\""+scalaCompat+"\""$*/ /*-*/ ""
private val scalaVersion = /*$"\""+scala+"\""$*/ /*-*/ ""
private val sparkVersion = /*$"\""+spark+"\""$*/ /*-*/ ""

fun main() {
    println("Read spark version as $sparkVersion, scala version as $scalaVersion, scala compat version as $scalaCompatVersion, kotlin version as $kotlinVersion")

    //#if scalaCompat < 2.13
    //println("This doesn't work on scala 2.13 ${scala.collection.mutable.WrappedArray::class.qualifiedName}")
    //#endif
}