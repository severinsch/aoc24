enum class Direction {
    Up,
    Right,
    Down,
    Left
}

fun Direction.next(): Direction {
    return when (this) {
        Direction.Up -> Direction.Right
        Direction.Right -> Direction.Down
        Direction.Down -> Direction.Left
        Direction.Left -> Direction.Up
    }
}

fun main() {

    fun getNext(x: Int, y: Int, d: Direction): Pair<Int, Int> {
        return when (d) {
            Direction.Up -> Pair(x, y - 1)
            Direction.Right -> Pair(x + 1, y)
            Direction.Down -> Pair(x, y + 1)
            Direction.Left -> Pair(x - 1, y)
        }
    }

    fun part1(input: List<String>): Int {
        var direction = Direction.Up
        val sizeX = input.first().length
        val sizeY = input.size
        var currentY = input.indexOfFirst { it.contains("^") }
        var currentX = input[currentY].indexOf('^')
        val visited: MutableSet<Pair<Int, Int>> = mutableSetOf()

        fun isNextOccupied(x: Int, y: Int, d: Direction): Boolean {
            val (nextX, nextY) = getNext(x, y, d)
            if ((nextX !in (0..<sizeX)) or (nextY !in (0..<sizeY))) {
                return false
            }
            return input[nextY][nextX] == '#'
        }

        while (currentX in (0..<sizeX) && currentY in (0..<sizeY)) {
            visited.add(Pair(currentX, currentY))
            if (isNextOccupied(currentX, currentY, direction)) {
                direction = direction.next()
                continue
            }
            val (nextX, nextY) = getNext(currentX, currentY, direction)
            currentX = nextX
            currentY = nextY
        }

        return visited.size
    }

    fun part2(input: List<String>): Int {
        var direction = Direction.Up
        val sizeX = input.first().length
        val sizeY = input.size
        var currentY = input.indexOfFirst { it.contains("^") }
        var currentX = input[currentY].indexOf('^')
        val visited: MutableSet<Triple<Int, Int, Direction>> = mutableSetOf()
        var potentialObstaclePositions: MutableSet<Pair<Int, Int>> = mutableSetOf()

        fun isNextOccupied(x: Int, y: Int, d: Direction, field: List<String> = input): Boolean {
            val (nextX, nextY) = getNext(x, y, d)
            if ((nextX !in (0..<sizeX)) or (nextY !in (0..<sizeY))) {
                return false
            }
            return field[nextY][nextX] == '#'
        }

        fun checkIfTurnEntersLoop(x: Int, y: Int, d: Direction): Boolean {
            val newInput = input.toMutableList()
            val visitedAfterTurn = mutableSetOf(Triple(x, y, d))
            val (newObstacleX, newObstacleY) = getNext(x, y, d)
            val newLine = String(input[newObstacleY].toCharArray().also { it[newObstacleX] = '#' })
            newInput[newObstacleY] = newLine
            var newD = d.next()
            // run simulation from here until either leaving board or entering loop
            var newX = x
            var newY = y
            while (newX in (0..<sizeX) && newY in (0..<sizeY)) {
                if (Triple(newX, newY, newD) in visited || Triple(newX, newY, newD) in visitedAfterTurn) {
                    println("Turning from ($x,$y) facing $d DOES enter loop")
                    return true
                }
                visitedAfterTurn.add(Triple(newX, newY, newD))
                if (isNextOccupied(newX, newY, newD, newInput)) {
                    newD = newD.next()
                    continue
                }
                val (nextX, nextY) = getNext(newX, newY, newD)
                newX = nextX
                newY = nextY
            }
            println("Turning from ($x,$y) facing $d does not enter loop")
            return false
        }

        while (currentX in (0..<sizeX) && currentY in (0..<sizeY)) {
            visited.add(Triple(currentX, currentY, direction))
            if (isNextOccupied(currentX, currentY, direction)) {
                direction = direction.next()
                continue
            }
            val (nextX, nextY) = getNext(currentX, currentY, direction)

            if ((nextX in (0..<sizeX)) and (nextY in (0..<sizeY))) {
                if (Pair(nextX, nextY) !in potentialObstaclePositions) {
                    // check if turning now would add a previous path
                    if (checkIfTurnEntersLoop(currentX, currentY, direction)) {
                        potentialObstaclePositions.add(Pair(nextX, nextY))
                    }
                }
            }
            currentX = nextX
            currentY = nextY
        }

        return potentialObstaclePositions.size
    }

    val input = readInput("day06")
    part1(input).println()
    part2(input).println()
}
