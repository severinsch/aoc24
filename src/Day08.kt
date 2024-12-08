fun main() {

    fun isInField(spot: Pair<Int, Int>, input: List<String>): Boolean {
        return spot.first in input.first().indices && spot.second in input.indices
    }

    // for part 1
    fun calculateAntinodes(s1: Pair<Int, Int>, s2: Pair<Int, Int>, input: List<String>): List<Pair<Int, Int>> {
        val xDiff = s1.first - s2.first
        val yDiff = s1.second - s2.second
        val potentialSpot1 = Pair(s1.first + xDiff, s1.second + yDiff)
        val potentialSpot2 = Pair(s2.first - xDiff, s2.second - yDiff)

        val result = mutableListOf<Pair<Int, Int>>()
        if (isInField(potentialSpot1, input)) {
            result.add(potentialSpot1)
        }
        if (isInField(potentialSpot2, input)) {
            result.add(potentialSpot2)
        }
        return result
    }

    // for part 2
    fun calculateAntinodesAllDistances(
        s1: Pair<Int, Int>,
        s2: Pair<Int, Int>,
        input: List<String>
    ): List<Pair<Int, Int>> {
        val xDiff = s1.first - s2.first
        val yDiff = s1.second - s2.second

        val result = mutableListOf<Pair<Int, Int>>()

        var currentSpot = s1
        while (isInField(currentSpot, input)) {
            result.add(currentSpot)
            currentSpot = Pair(currentSpot.first + xDiff, currentSpot.second + yDiff)
        }
        currentSpot = s2
        while (isInField(currentSpot, input)) {
            result.add(currentSpot)
            currentSpot = Pair(currentSpot.first - xDiff, currentSpot.second - yDiff)
        }

        return result
    }

    fun getAntinodesForFrequency(input: List<String>, freq: Char, allDistances: Boolean = false): Set<Pair<Int, Int>> {
        val allStations = input.flatMapIndexed { idx, line ->
            line.mapIndexedNotNull { charIdx, char ->
                if (char == freq) Pair(charIdx, idx) else null
            }
        }

        val allAntinodes = mutableSetOf<Pair<Int, Int>>()

        for (s1 in allStations) {
            for (s2 in allStations) {
                if (s1 == s2) {
                    continue
                }
                val antinodes = if (allDistances) {
                    calculateAntinodesAllDistances(s1, s2, input)
                } else {
                    calculateAntinodes(s1, s2, input)
                }

                allAntinodes.addAll(antinodes)
            }
        }
        return allAntinodes
    }

    fun part1(input: List<String>): Int {
        val allFrequencies = input.joinToString(separator = "").toSet() - setOf('.')
        return allFrequencies.flatMap { getAntinodesForFrequency(input, it) }.toSet().size
    }

    fun part2(input: List<String>): Int {
        val allFrequencies = input.joinToString(separator = "").toSet() - setOf('.')
        return allFrequencies.flatMap { getAntinodesForFrequency(input, it, true) }.toSet().size
    }

    val input = readInput("day08")
    part1(input).println()
    part2(input).println()
}
