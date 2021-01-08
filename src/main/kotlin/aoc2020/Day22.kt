package aoc2020

import java.io.File

class Day22 {

    data class Game(val subgames: ArrayList<Subgame>)
    data class Subgame(val rounds: ArrayList<Round>)

    data class Round(val decks: List<Deck>)
    data class Deck(val cards: List<Int>)

    private fun parseDeck(lines: List<String>) : Deck = Deck(lines.map { it.toInt() })

    private fun parseDecks(src: File) : List<Deck> {
        val lines = src.readLines()
        val blankLine = lines.indexOfFirst { it.isBlank() }
        return listOf(
            parseDeck(lines.slice(1 until blankLine)),
            parseDeck(lines.slice(blankLine+2 until lines.size))
        )
    }

    fun playCombat(startingDecks: List<Deck>) : List<Round> {
        var decks = startingDecks
        val result = ArrayList<Round>()
        while ( decks.none { it.cards.isEmpty() }) {
            result.add(Round(decks))
            // Peek top card from each deck (with deck index) and order highest first
            val topCards = decks
                .mapIndexed { i, deck -> Pair(i, deck.cards.first()) }
                .sortedByDescending { it.second }
            // Pop top card from each deck
            // Add all cards in order to the bottom of the winning deck
            decks = decks.mapIndexed { i, deck -> Deck(
                if (i == topCards[0].first) {
                    deck.cards.drop(1) + topCards.map { it.second }
                } else {
                    deck.cards.drop(1)
                }
            ) }
        }
        result.add(Round(decks))
        return result
    }

    fun solvePart1(src: File) : Int {
        val rounds = playCombat(parseDecks(src))
        // (last card in deck * 1) + (2nd last card * 2) + ...
        val decks = rounds.last().decks
        val winningDeck = decks.first { it.cards.isNotEmpty() }
        return winningDeck.cards
            .mapIndexed { i, card -> card * (winningDeck.cards.size - i) }
            .sum()
    }

    companion object {
        @JvmStatic fun main(arg: Array<String>) {
            val solver = Day22()
            val file = File("src/main/resources/aoc2020/day22.input")
            println("Part1: " + solver.solvePart1(file))
        }
    }

}