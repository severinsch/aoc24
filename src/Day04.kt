import kotlin.math.floor

fun main() {
    fun transpose(input: List<String>): List<String> {
        val res = input.first().indices.map { mutableListOf<Char>() }
        for(line in input) {
            for(j in line.indices) {
                res[j].addLast(line[j])
            }
        }
        return res.map { charArr -> charArr.joinToString(separator = "") }
    }

    fun getDiagonals(input: List<String>): List<String> {
        val size = input.size + input.first().length
        val res = (1..size).map { mutableListOf<Char>() }

        for(i in (input.size -1) downTo 0){
            val lengthOfDiagonal = input.size - i
            for (x in (1..lengthOfDiagonal)) {
                res[i].addFirst(input[input.size-x][lengthOfDiagonal-x])
                res[i+input.size].addFirst(input[lengthOfDiagonal-x][input.first().length-x])
            }
        }
        // drop the first element as the diagonal from corner to corner is included twice
        return res.drop(1).map { charArr -> charArr.joinToString(separator = "") }
    }

    fun part1(input: List<String>): Int {
        val regex = Regex("XMAS")

        fun checkHorizontal(board: List<String>): Int {
            return board.sumOf {
                line -> regex.findAll(line).count() + regex.findAll(line.reversed()).count()
            }
        }

        val forwardAndBackwards = checkHorizontal(input)

        val diagonals = getDiagonals(input)
        val diagonalsCountLeftToRight = checkHorizontal(diagonals)

        val diagonalsRightToLeft = getDiagonals(input.map { it.reversed() })
        val diagonalsCountRightToLeft = checkHorizontal(diagonalsRightToLeft)

        val transposed = transpose(input)
        val upAndDown = checkHorizontal(transposed)

        return forwardAndBackwards + upAndDown + diagonalsCountLeftToRight + diagonalsCountRightToLeft
    }

    fun part2(input: List<String>): Int {

        fun reorderDiagonals(diags: List<String>): List<String> {
            val firstPartSize = floor(diags.size/2.0).toInt()
            val firstPart = diags.take(firstPartSize).reversed()
            return firstPart + diags.drop(firstPartSize)
        }
        val diagonalsLeftToRightUnordered = getDiagonals(input)
        // reorder diagonals to be sorted from lower left to upper right
        val diagonalsLeftToRight = reorderDiagonals(diagonalsLeftToRightUnordered)

        val diagonalsRightToLeftUnordered = getDiagonals(input.map { it.reversed() })
        val diagonalsRightToLeft = reorderDiagonals(diagonalsRightToLeftUnordered)

        fun findIndicesOfA(diags: List<String>, search: String, leftToRight: Boolean): List<Pair<Int, Int>> {
            val newDiags = if (!leftToRight) {
                diags.map { it.reversed() }
            } else {
                diags
            }
            return newDiags.mapIndexedNotNull { lineIndex, line ->
                var startIndex = 0
                val res = mutableListOf<Pair<Int,Int>>()
                while(true){
                    val idxM = line.indexOf(search, startIndex)
                    if(idxM == -1) {
                        break
                    }
                    // all below is just calculating the index of the A in the given grid
                    val idx = idxM + 1
                    // calculate x,y index in grid of the A of MAS
                    // x, y from top left increasing
                    var x: Int = idx
                    var y: Int = idx

                    val xShift = if (leftToRight) lineIndex - diags.size/2 else diags.size/2 - lineIndex
                    val yShift = if (leftToRight) diags.size/2 - lineIndex else lineIndex - diags.size/2
                    // depending on whether diagonal is left to right or right to left, shift x for diagonals either below or above middle line
                    val xShiftNecessary = if (leftToRight) lineIndex > diags.size/2 else lineIndex < diags.size/2
                    val yShiftNecessary = if (leftToRight) lineIndex < diags.size/2 else lineIndex > diags.size/2
                    // e.g. for leftToRight, diagonals above middle line start at y=0 ==> no y shift
                    // but shift x depending on how far from x=0
                    if(xShiftNecessary) {
                        x += xShift
                    }
                    if(yShiftNecessary) {
                        // for leftToRight, diagonals below middle line start at x=0 => no x shift necessary
                        // but shift y
                        y += yShift
                    }
                    if(!leftToRight) {
                        // for right to left, indices count from bottom up, to fit to coordinate system this is done
                        y = input.size - y - 1
                    }
                    res.add(Pair(x, y)) // add index of the A

                    startIndex = idx + 2
                }
                res.ifEmpty { null }
            }.flatten()
        }

        // If the index of the found A in a LtoR diagonal is the same as the index of a found A in a RtoL diagonal, we have a X
        val indicesLeftToRight = findIndicesOfA(diagonalsLeftToRight, "MAS", true) + findIndicesOfA(diagonalsLeftToRight, "SAM", true)
        val indicesRightToLeft = findIndicesOfA(diagonalsRightToLeft, "MAS", false) + findIndicesOfA(diagonalsRightToLeft, "SAM", false)

        return indicesLeftToRight.count { indicesRightToLeft.contains(it) }
    }

    val input = readInput("day04")
    part1(input).println()
    part2(input).println()
}
