fun main() {
    fun isPossible(result: Long, numbers: List<Int>, intermediateResult: Long, doConcat: Boolean = false): Boolean {
        if (numbers.isEmpty()) {
            return intermediateResult == result
        }

        // shortcut
        if (numbers.sum() + intermediateResult == result) {
            return true
        }

        // do addition
        val additionRes = isPossible(result, numbers.drop(1), intermediateResult + numbers.first(), doConcat)
        if (additionRes) {
            return true
        }

        // do multiplication
        val multiRes = isPossible(result, numbers.drop(1), intermediateResult * numbers.first(), doConcat)
        if (multiRes) {
            return true
        }

        if (doConcat) {
            // do concatenation
            val newIntermediate = "$intermediateResult${numbers.first()}".toLong()
            return isPossible(result, numbers.drop(1), newIntermediate, true)
        }
        return false
    }

    fun part1(input: List<String>): Long {
        val equations = input.map { line ->
            val nums = line.replace(":", "").split(" ")
            Pair(nums.first().toLong(), nums.drop(1).map { it.toInt() })
        }

        return equations.filter { (result, nums) -> isPossible(result, nums.drop(1), nums.first().toLong()) }
            .sumOf { it.first }
    }

    fun part2(input: List<String>): Long {
        val equations = input.map { line ->
            val nums = line.replace(":", "").split(" ")
            Pair(nums.first().toLong(), nums.drop(1).map { it.toInt() })
        }

        return equations.filter { (result, nums) ->
            isPossible(
                result,
                nums.drop(1),
                nums.first().toLong(),
                doConcat = true
            )
        }.sumOf { it.first }
    }

    val input = readInput("day07")
    part1(input).println()
    part2(input).println()
}
