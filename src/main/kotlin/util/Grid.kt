package util

// Currently unused
class Grid<T> (val width: Int, val height: Int) : Iterable<T?> {
    private val cells: MutableList<T?> = mutableListOf()

    init { for (i in 0 until width*height) cells.add(null) }

    private fun idx(x: Int, y: Int) : Int { return x + y * width }
    fun set(x: Int, y: Int, value: T?) { cells[idx(x, y)] = value }
    fun get(x: Int, y: Int) : T? { return cells[idx(x, y)] }
    override fun iterator(): Iterator<T?> { return cells.iterator() }
}
