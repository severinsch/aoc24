fun main() {
    fun part1(input: List<String>): Int {
        var stones = input.single().split(" ")
        fun applyTransform(st: List<String>): List<String> {
            return st.flatMap { s ->
                if (s.length % 2 == 0) {
                    return@flatMap listOf(s.take(s.length / 2), s.drop(s.length / 2).toLong().toString())
                }
                if (s == "0") {
                    return@flatMap listOf("1")
                }
                return@flatMap listOf((s.toLong() * 2024).toString())
            }
        }
        repeat(25) {
            stones = applyTransform(stones)
        }
        return stones.size
    }

    // this is the naive approach from part 1, but the runtime is too long for 75 iterations
    // tbd how to do it using a different approach
    fun part2(input: List<String>): Int {
        var stones = input.single().split(" ").map { it.toLong() }
        println("Initial: $stones")
        fun applyTransform(st: List<Long>): List<Long> {
            return st.flatMap { s ->
                if (s == 0L) {
                    return@flatMap listOf(1)
                }
                val st = s.toString()
                if (st.length % 2 == 0) {
                    return@flatMap listOf(st.take(st.length / 2).toLong(), st.drop(st.length / 2).toLong())
                }
                return@flatMap listOf(s * 2024)
            }
        }
        repeat(75) {
            println("Iteration: $it, stones size: ${stones.size}")
            stones = applyTransform(stones)
        }
        return stones.size
    }

    val input = readInput("day11")
    executeTimed(::part1, input)
    executeTimed(::part2, input)
}
