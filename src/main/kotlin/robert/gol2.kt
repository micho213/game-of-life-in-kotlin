package robert

import java.util.Random

var rows = 20
var cols = 90
var obj = "*"

// create the grid ( populate it initially )
fun ClosedRange<Int>.random() = Random().nextInt(endInclusive + 1 - start) + start
var grid = List(rows, { _ -> List(cols, { _ -> (0..5).random() == 0 }) })

// Console class is responsible for rendering ( printing ) to the std out
// console also clears the screen
class Console {
   private fun clear() {
       print(String.format("%c[0;0f", 0x1B))
   }

   fun render(grid: List<List<Boolean>>) {
       clear()
       println("   " + "-".repeat(cols))
       for (row in grid) {
           print(" | ")
           for (col in row) {
               print(if (!col) ' ' else obj)
           }
           println(" | ")
       }
       println("   " + "-".repeat(cols))
   }
}

// data class reponsible for storing the point/cell and information about it
data class Point(val row: Int, val col: Int) {
   // return the neighbours of a cell
   private fun myNeighbours() = arrayOf(
       Point(row - 1, col - 1), Point(row - 1, col), Point(row - 1, col + 1),
       Point(row, col - 1),                          Point(row, col + 1),
       Point(row + 1, col - 1), Point(row + 1, col), Point(row + 1, col + 1)) 

   // check if the position is valid 
   private fun isValidPosition(point:Point) = point.row in 0 until rows && point.col in 0 until cols
   // return the position of the cell in the grid 
   fun positionState(point:Point = this) = grid[point.row][point.col]
   // return the number of neighbours for a given cell
   fun countNeighbours() = myNeighbours()
       .filter { isValidPosition(it) }
       .map { positionState(it) }
       .count { it }
}

// evolve function is what is called every 0.2 s (as per thread.sleep(200) )
// this function is responsible for evolving the cells according to the rules
fun evolve() = (0 until rows).map { row ->
       (0 until cols).map { col ->
           val point = Point(row, col)
           val neighbours = point.countNeighbours()
           neighbours == 3 || (neighbours == 2 && point.positionState())
       }
   }

// this main function runs the program and asks the user to specify the cols and rows
// this main function also asks for a character for the cells ( what charcter is displayed )
fun main(args: Array<String>) {
   print("enter rows: and col: ")
   val (a,b ) = readLine()!!.split(' ').map(String::toInt) 
   println("enter the character for the cells" )
   val string = readLine()	
   obj = string.toString()   
   cols = b
   rows = a
   grid =  List(rows, { _ -> List(cols, { _ -> (0..5).random() == 0 }) })
   val console = Console()
   while (true) {
       console.render(grid)
       grid = evolve()
       Thread.sleep(200)
   }
}


