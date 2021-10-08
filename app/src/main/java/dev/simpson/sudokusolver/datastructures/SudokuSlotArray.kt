package dev.simpson.sudokusolver.datastructures

/**
 * Represents either a row or column of squares on a sudoku board
 */
//Initializes the collection with a size (should equal the length of one side of the board)
class SudokuSlotArray(length: Int) {

    //The set of suduoku slots that belong in the collection
    val squares = arrayOfNulls<SudokuSlot>(length)
    //Indicates if all slots in teh collection hav ea value other than 0
    var complete: Boolean = false
    //Tracks how many times a possible value occurs within the collection in order to narrow down values that occur only once
    val possibleValueOccurrences: MutableMap<Int,Int> = mutableMapOf()

    /**
     * Adds a new slot to the collection
     * @param slot: the slot to add to the collection
     */
    fun addSlot(slot: SudokuSlot, index: Int) {
        if(squares[index] == null) {
            squares[index] = slot
        }
    }

    /**
     * Calculates the initial possible values for each slot within the array
     */
    fun calculatePossibleValues() {
        //iterate over all slots in the list
        for(slot in squares) {
            //iterate over all slots again
            for(slot2 in squares) {
                //compare all slots to each other and if a slot has a value remove it from the other slot's possible values list
                slot2?.removePossibleValue(slot!!.number)
            }
        }
    }

    /**
     * Recalculate all possible values and possible value counts for all slots in the collection every time a slot is assigned a value
     * @param assignedValue: the new value assigned to a slot
     * @param previousPossibleValues: the list of possible values that could've belonged to the slot before it was assigned a value
     */
    fun recalculatePossibleValues(assignedValue: Int, previousPossibleValues: MutableList<Int>) {
        //iterate over all slots in the collection
        for(slot in squares) {
            //remove the value that was assigned to a slot from the possible values of the rest of the slots
            slot!!.removePossibleValue(assignedValue)
        }
        //since the value has now been assigned to a slot, remove that value from the map of how many times it occurs in possible values
        possibleValueOccurrences.remove(assignedValue)
        for(possibleValue in previousPossibleValues) {
            //for all values that could've belonged to the slot, decrement their count in the possibleValuesCount
            decrementPossibleValueCount(possibleValue)
        }
    }

    /**
     * Checks for slots that have the exact same possible values and if it makes sense to do so, remove those values as possible values from other slots
     * ex. if two slots have the possible values 1,2 remove 1 and 2 as possible values from every slot in the collection as those two slots because 1 and 2 have to belong to those two slots
     */
    fun accountForMatchingSlots() {
        for(slot in squares) {
            //create a collection to track the slots which have the exact same possible values as another slot
            var matchingPossibleValues: MutableList<SudokuSlot> = mutableListOf()
            for(slot2 in squares) {
                //if the current slot has the same possible values as slot2, keep track of slot2 for later
                if(slot!!.possibleValues.size > 0 && slot!!.possibleValues == slot2!!.possibleValues) {
                    matchingPossibleValues.add(slot2)
                }
            }

            //If the number of possible values is exactly the same as the number of slots...
            //it means these values have to go in these slots. Remove these values from the possible values of all other slots
            if(matchingPossibleValues.size > 0 && matchingPossibleValues.get(0).possibleValues.size == matchingPossibleValues.size) {
                for(slot2 in squares) {
                    // if slot doesn't have the exact same possible values as slot2, it means it is a slot we need to remove values from
                    if(slot!!.possibleValues != slot2!!.possibleValues) {
                        for(value in slot!!.possibleValues) {
                            //remove all of the current slot's values from slot2's possible values list
                            slot2.removePossibleValue(value)
                            //decrement the possible value count for every possible value we remove
                            decrementPossibleValueCount(value)
                        }
                    }
                }
            }
        }
    }

    /**
     * initial calculation of how many times each possible value occurs within the collection
     */
    fun calculatePossibleValueOccurrences() {
        possibleValueOccurrences.clear()
        //iterate over all slots in the collection
        for(slot in squares) {
            //iterate over all possible values for the slot
            for(value in slot!!.possibleValues) {
                //get the count of how many times the value occurs in the collection
                var valueCount = possibleValueOccurrences.get(value)
                //if this value exists in the map
                if(valueCount != null) {
                    //increment its count by 1
                    possibleValueOccurrences.put(value, (valueCount+1))
                } else {
                    //otherwise add a new entry for that value to the map initialized at 1
                    possibleValueOccurrences.put(value,1)
                }
            }
        }

    }

    /**
     * Performs all of the logic of assigning values to each slot within the row or column, based upon the values of other slots in the collection
     * @return Map of the slots that were assigned values, to the possible values that could've belonged to that slot before it was assigned a value
     */
    fun assignCorrectValues(): MutableMap<SudokuSlot,List<Int>> {
        var modifiedSlots: MutableMap<SudokuSlot,List<Int>> = mutableMapOf()
        //iterate over all slots in the collection
        for(slot in squares) {
            //if the slot only has one possible value
            if(slot!!.possibleValues.size == 1) {
                //assign the value to the slot because it is the only value that can belong to that slot
                slot.setValue(slot.possibleValues.get(0))
                //add that slot to the list of slots we assigned a value to
                modifiedSlots.put(slot,slot.possibleValues)
            }
        }
        //iterate over all possible values in the collection
        for(key in possibleValueOccurrences.keys) {
            //if a possible value exists only 1 time throughout the entire collection
            if(possibleValueOccurrences.get(key) == 1) {
                //iterate over all slots in the collection
                for(slot in squares) {
                    //find the only slot that the value could belong to
                    if(slot!!.possibleValues.contains(key)) {
                        //assign this value to this slot because it is the only slot this value can belong to
                        slot.setValue(key)
                        //add this slot to the list of slots we assigned a value to
                        modifiedSlots.put(slot,slot.possibleValues)
                    }
                }
            }
        }
        return modifiedSlots
    }

    /**
     * Calculates if each slot in the collection has a value or not and returns result
     * @return true if all slots in the collection have a value other than 0, otherwise returns false
     */
    fun isComplete(): Boolean {
        if(!complete) {
            var hasValue: Boolean = true
            //loop through all slots in the collection, if one is found without a value, slot array is not complete
            for(slot in squares) {
                if(slot!!.hasValue()) {
                    hasValue = false
                    break
                }
            }
            complete = hasValue
        }
        return complete
    }

    /**
     * decrements by 1 the number of times a possible value occurs in the collection
     * @param value: the value to be decremented
     */
    fun decrementPossibleValueCount(value: Int) {
        //get the count of how many times the value occurs in the collection
        var occurrenceCount: Int? = possibleValueOccurrences.get(value)
        if(occurrenceCount != null && occurrenceCount != 0) {
            if(occurrenceCount == 1) {
                //if the count of the value is only 1 remove it
                possibleValueOccurrences.remove(value)
            } else {
                possibleValueOccurrences.put(value,(occurrenceCount-1))
            }
        }
    }
}