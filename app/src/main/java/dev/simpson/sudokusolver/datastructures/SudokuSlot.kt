package dev.simpson.sudokusolver.datastructures

/**
 * Represents a single square on a sudoku puzzle
 */
class SudokuSlot(val maxValue: Int, val row: Int, val column: Int, val num: Int = 0) {

    //stores the final (correct) value for this slot
    var number: Int
        private set
    //stores all possible values that could belong to this slot
    val possibleValues: MutableList<Int>
    //stores the index of which row this slot belongs to
    val rowIndex: Int
    //stores the index of which column this slot belongs to
    val columnIndex: Int

    /**
     * initializes a slot setting the list of possible values to all values that could possibly be on the board if the assigned number is 0,
     * otherwise the list of possible values is empty
     * @param rowIndex : the index of the row this slot belongs to
     * @param columnIndex: the index of the column this slot belongs to
     */
    init {
        number = num
        rowIndex = row
        columnIndex = column
        if (number > 0) {
            possibleValues = mutableListOf()
        } else {
            possibleValues = MutableList(maxValue) {index -> index + 1}
        }
    }

    /**
     * Sets a value in the slot, then removes all possible values from the possibleValues list if the assigned value is 0
     * @param value: The value to assign to the slot
     * @return the value of the slot
     */
    fun setValue(value:Int): Int {
        number = value
        if(value > 0) {
            possibleValues.clear()
        }
        return number
    }

    /**
     * Removes a value from the list of possible values
     * @param value: value to remove from the list
     * @return boolean for if removal was successful or not
     */
    fun removePossibleValue(value: Int): Boolean {
        return possibleValues.remove(value)
    }

    /**
     * Determines if this slot has a value assigned
     * @return true if slot number is not 0
     */
    fun hasValue(): Boolean {
        return number != 0
    }

    /**
     * @return current slot value and list of possible values as a string
     */
    override fun toString(): String {
        return "Value: $number, Possible Values: $possibleValues"
    }

}