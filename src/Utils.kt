import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText
import kotlin.time.measureTime

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("inputs/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)


/**
 * Measures the execution time and prints the result and the time
 */
fun executeTimed(f: (List<String>) -> Any, input: List<String>) {
    val res: Any
    val duration = measureTime {
        res = f(input)
    }
    println("$res, time: $duration")
}

// some helpful functions to make working with the given input a little easier

typealias Coords = Pair<Int, Int>

operator fun List<String>.get(point: Coords): Char {
    return this[point.second][point.first]
}

fun List<String>.getInt(point: Coords): Int {
    return this[point.second][point.first].digitToInt()
}

fun List<String>.isValidPoint(point: Coords): Boolean {
    return point.second in indices && point.first in this[0].indices
}

fun List<String>.allCoords(): List<Coords> {
    val res = mutableListOf<Coords>()
    for (y in this.indices) {
        for (x in this[0].indices) {
            res.add(Pair(x, y))
        }
    }
    return res
}