fun main() {

    fun getSurroundingCoords(point: Pair<Int, Int>, input: List<String>): List<Pair<Int, Int>> {
        val (x, y) = point
        val up = Pair(x, y - 1)
        val right = Pair(x + 1, y)
        val down = Pair(x, y + 1)
        val left = Pair(x - 1, y)
        return listOf(up, right, down, left).filter { p ->
            input.isValidPoint(p)
        }
    }

    fun getTrailheads(input: List<String>): List<Pair<Int, Int>> {
        return input.allCoords().filter { input[it] == '0' }
    }

    fun part1(input: List<String>): Int {
        val trailheads = getTrailheads(input)

        fun getReachableGoals(point: Pair<Int, Int>): Set<Pair<Int, Int>> {
            val value = input.getInt(point)
            if (value == 9) {
                return setOf(point)
            }
            val surroundingRelevant = getSurroundingCoords(point, input).filter { p ->
                (input.getInt(p)) == value + 1
            }
            return surroundingRelevant.map { getReachableGoals(it) }.reduceOrNull { a, b -> a.union(b) } ?: emptySet()
        }

        return trailheads.sumOf { getReachableGoals(it).size }
    }

    fun part2(input: List<String>): Int {
        val trailheads = getTrailheads(input)

        fun getPaths(point: Pair<Int, Int>, currentPath: List<Pair<Int, Int>>): Set<List<Pair<Int, Int>>> {
            val value = input.getInt(point)
            val newPath = currentPath + listOf(point)
            if (value == 9) {
                return setOf(newPath)
            }
            val surroundingRelevant = getSurroundingCoords(point, input).filter { p ->
                (input.getInt(p)) == value + 1
            }
            return surroundingRelevant.map { getPaths(it, newPath) }.reduceOrNull { a, b -> a.union(b) } ?: emptySet()
        }

        return trailheads.sumOf { getPaths(it, emptyList()).size }
    }

    val input = readInput("day10")
    executeTimed(::part1, input)
    executeTimed(::part2, input)
}
