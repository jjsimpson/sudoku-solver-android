package dev.simpson.sudokusolver.datastructures

import org.junit.Test

import org.junit.Assert.*

/**
 * Unit Tests for SudokuSlot data structure
 */
class SudokuSlotTest {
    @Test
    fun constructor_withoutValue() {
        var slot: SudokuSlot = SudokuSlot(9,0,0)
        assertEquals(0, slot.number)
        assertEquals(MutableList(9) {index -> index+1},slot.possibleValues)
    }

    @Test
    fun constructr_withValue() {
        var slot: SudokuSlot = SudokuSlot(9,0,0, 5)
        assertEquals(5, slot.number)
        var expectedPossible: MutableList<Int> = mutableListOf()
        assertEquals(expectedPossible,slot.possibleValues)
    }

    @Test
    fun setValue() {
        var slot: SudokuSlot = SudokuSlot(9,0,0)
        assertEquals(0, slot.number)
        assertEquals(MutableList(9) {index -> index+1},slot.possibleValues)
        slot.setValue(7)
        assertEquals(7, slot.number)
        var expectedPossible: MutableList<Int> = mutableListOf()
        assertEquals(expectedPossible,slot.possibleValues)
    }

    @Test
    fun removePossibleValue() {
        var slot: SudokuSlot = SudokuSlot(9,0,0)
        assertEquals(MutableList(9) {index -> index+1},slot.possibleValues)
        slot.removePossibleValue(3)
        assertEquals(mutableListOf(1,2,4,5,6,7,8,9),slot.possibleValues)
        slot.removePossibleValue(8)
        assertEquals(mutableListOf(1,2,4,5,6,7,9),slot.possibleValues)
        slot.removePossibleValue(1)
        assertEquals(mutableListOf(2,4,5,6,7,9),slot.possibleValues)
        slot.removePossibleValue(15)
        assertEquals(mutableListOf(2,4,5,6,7,9),slot.possibleValues)
        slot.removePossibleValue(2)
        assertEquals(mutableListOf(4,5,6,7,9),slot.possibleValues)
        slot.removePossibleValue(4)
        assertEquals(mutableListOf(5,6,7,9),slot.possibleValues)
        slot.removePossibleValue(5)
        assertEquals(mutableListOf(6,7,9),slot.possibleValues)
        slot.removePossibleValue(6)
        assertEquals(mutableListOf(7,9),slot.possibleValues)
        slot.removePossibleValue(7)
        assertEquals(mutableListOf(9),slot.possibleValues)
        slot.removePossibleValue(9)
        var expectedPossible: MutableList<Int> = mutableListOf()
        assertEquals(expectedPossible,slot.possibleValues)
        slot.removePossibleValue(4)
        assertEquals(expectedPossible,slot.possibleValues)
    }

    @Test
    fun hasValue() {
        var slot: SudokuSlot = SudokuSlot(9,0,0)
        assertFalse(slot.hasValue())
        slot.setValue(3)
        assertTrue(slot.hasValue())
    }
}