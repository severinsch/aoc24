fun main() {
    fun part1(input: List<String>): Int {
        val regex = Regex("mul\\((?<num1>\\d+),(?<num2>\\d+)\\)")
        val program = input.joinToString()
        val matches = regex.findAll(program)

        return matches.sumOf { matchResult ->
            matchResult.groupValues.drop(1).map { it.toInt() }.reduce { a, b -> a * b }
        }

    }

    fun part2(input: List<String>): Int {
        val program = input.joinToString()
        val regex = Regex("mul\\((?<nums>\\d+,\\d+)\\)|do\\(\\)|don't\\(\\)")
        val matches = regex.findAll(program)
        // convert to list of "do()", "don't()" and strings like "3,4" or "123,54"
        val parts = matches.map { match ->
            when (match.value) {
                "do()" -> match.value
                "don't()" -> match.value
                else -> match.groupValues.drop(1).single()
            }
        }
        return parts.fold(Pair(0, true)) { (current, enabled), operation ->
            when (operation) {
                "do()" -> Pair(current, true)
                "don't()" -> Pair(current, false)
                else -> {
                    if (enabled) {
                        val (a, b) = operation.split(",").map { it.toInt() }
                        Pair(current + a * b, enabled)
                    } else {
                        Pair(current, enabled)
                    }
                }
            }
        }.first
    }

    val input = readInput("day03")
    part1(input).println()
    part2(input).println()
}
