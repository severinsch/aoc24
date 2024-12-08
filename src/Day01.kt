import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        val left = mutableListOf<Int>()
        val right = mutableListOf<Int>()

        input.forEach { line ->
            line
                .split("   ")
                .map { it.toInt() }
                .also { numbers -> left.add(numbers[0]) }
                .also { numbers -> right.add(numbers[1]) }
        }

        return left.sorted().zip(right.sorted()).sumOf { (l, r) -> abs(l - r) }
    }

    fun part2(input: List<String>): Int {
        val left = mutableListOf<Int>()
        val appearances = mutableMapOf<Int, Int>()

        input.forEach { line ->
            line
                .split("   ")
                .map { it.toInt() }
                .also { numbers -> left.add(numbers[0]) }
                .also { numbers -> appearances.merge(numbers[1], 1) { old, new -> old + new } }
        }

        return left.sumOf { it * appearances.getOrDefault(it, 0) }
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("day01")
    executeTimed(::part1, input)
    executeTimed(::part2, input)
}
