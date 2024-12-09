fun main() {
    data class Block(
        var type: String,
        var size: Int,
        var index: Int,
    )

    fun Block.isFree(): Boolean {
        return this.type == "FREE"
    }

    fun printFS(fs: List<Int?>) {
        println(fs.joinToString(separator = "") { x -> x?.toString() ?: "." })
    }

    fun part1(input: List<String>): Long {
        val fileSystem = input.single()
        val blocks = fileSystem.indices.map { i ->
            val size = fileSystem[i].digitToInt()
            val type = if (i % 2 == 0) "DATA" else "FREE"
            Block(type, size, i / 2)
        }
        val newFilesystem: MutableList<Int?> = blocks.flatMap { b ->
            (0..<b.size).map {
                if (b.isFree()) null else b.index
            }
        }.toMutableList()

        for (i in newFilesystem.indices) {
            val value = newFilesystem[i]
            if (value != null) {
                continue
            }
            val lastDataIdx = newFilesystem.indexOfLast { it != null }
            if (lastDataIdx < i) {
                break
            }
            newFilesystem[i] = newFilesystem[lastDataIdx]
            newFilesystem[lastDataIdx] = null
        }
        return newFilesystem.mapIndexedNotNull { i, e -> e?.times(i.toLong()) }.sum()
    }

    fun part2(input: List<String>): Long {
        val fileSystem = input.single()
        val blocks = fileSystem.indices.map { i ->
            val size = fileSystem[i].digitToInt()
            val type = if (i % 2 == 0) "DATA" else "FREE"
            Block(type, size, i / 2)
        }

        val newFileSystem = blocks.toMutableList()

        for (block in newFileSystem.filter { !it.isFree() }.reversed()) {
            val spaceIdx = newFileSystem.indexOfFirst { it.index < block.index && it.isFree() && it.size >= block.size }
            if (spaceIdx == -1) {
                continue
            }
            val space = newFileSystem[spaceIdx]
            // "remove" original block
            block.type = "FREE"
            if (space.size == block.size) {
                // switch parameters of space and block
                space.type = "DATA"
                space.index = block.index
            } else {
                space.size -= block.size
                newFileSystem.add(spaceIdx, Block("DATA", block.size, block.index))
            }
        }

        return newFileSystem.flatMap { b ->
            (0..<b.size).map {
                if (b.isFree()) null else b.index
            }
        }.mapIndexedNotNull { i, e -> e?.times(i.toLong()) }.sum()

    }

    val input = readInput("day09")
    executeTimed(::part1, input)
    executeTimed(::part2, input)
}
