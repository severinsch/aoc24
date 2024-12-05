fun main() {
    fun checkReport(report: List<Int>): Boolean {
        val pairs = report.zipWithNext()
        val increasing = pairs[0].first < pairs[0].second
        for (pair in pairs) {
            var diff = pair.first - pair.second
            if (increasing) {
                diff *= -1
            }
            if ((diff < 1) or (diff > 3)) {
                return false
            }
        }
        return true
    }

    fun part1(input: List<String>): Int {
        return input.count { line ->
            line.split(" ").map { it.toInt() }.let { checkReport(it) }
        }
    }

    fun part2(input: List<String>): Int {
        fun checkReportWithDampener(report: List<Int>): Boolean {
            return checkReport(report) or report.indices.any {
                val newReport = report.toMutableList()
                newReport.removeAt(it)
                checkReport(newReport)
            }
        }
        return input.count { line ->
            line.split(" ").map { it.toInt() }.let { checkReportWithDampener(it) }
        }
    }

    val input = readInput("day02")
    part1(input).println()
    part2(input).println()
}
