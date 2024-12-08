fun main() {
    fun checkUpdate(numbers: List<Int>, ruleMap: Map<Int, Set<Int>>): Boolean {
        for (i in numbers.indices) {
            val current = numbers[i]
            val relevantNumbers = ruleMap[current]!!.intersect(numbers.toSet())
            val valid = relevantNumbers.all { numbers.indexOf(it) > i }
            if (!valid) {
                return false
            }
        }
        return true
    }

    fun parseInput(input: List<String>): Pair<Map<Int, Set<Int>>, List<List<Int>>> {
        val rules = input.takeWhile { it.isNotBlank() }

        val ruleMap = mutableMapOf<Int, Set<Int>>()
        rules.map {
            val numbers = it.split("|").map { it.toInt() }
            Pair(numbers.first(), numbers[1])
        }.forEach { (a, b) ->
            ruleMap.merge(a, setOf(b)) { s1, s2 -> s1.plus(s2) }
        }

        val updates = input.drop(rules.size + 1).map { update -> update.split(",").map { it.toInt() } }
        return Pair(ruleMap.withDefault { _ -> emptySet() }, updates)
    }

    fun part1(input: List<String>): Int {
        val (ruleMap, updates) = parseInput(input)
        val validUpdates = updates.filter { checkUpdate(it, ruleMap) }
        return validUpdates.sumOf { it[it.size / 2] }
    }

    fun part2(input: List<String>): Int {
        val (ruleMap, updates) = parseInput(input)

        val comp = Comparator<Int> { a, b ->
            if (ruleMap[a]!!.contains(b)) {
                return@Comparator -1
            }
            if (ruleMap[b]!!.contains(a)) {
                return@Comparator 1
            }
            return@Comparator 0
        }
        val invalidUpdates = updates.filterNot { checkUpdate(it, ruleMap) }
        val fixedUpdates = invalidUpdates.map { it.sortedWith(comp) }
        return fixedUpdates.sumOf { it[it.size / 2] }
    }

    val input = readInput("day05")
    executeTimed(::part1, input)
    executeTimed(::part2, input)
}
