package com.ccom.uno

import android.graphics.Color
import java.lang.IllegalArgumentException

data class UnoCard(var suit: Suit, var type: Type) {
    enum class Suit(val value: Int, val color: Int) {
        RED(100, Color.RED),
        GREEN(200, Color.GREEN),
        BLUE(300, Color.BLUE),
        YELLOW(400, Color.YELLOW),
        WILD(500, Color.BLACK)
    }

    enum class Type(val value: Int, val displayText: String) {
        ZERO(0, "0"),
        ONE(1, "1"),
        TWO(2, "2"),
        THREE(3, "3"),
        FOUR(4, "4"),
        FIVE(5, "5"),
        SIX(6, "6"),
        SEVEN(7, "7"),
        EIGHT(8, "8"),
        NINE(9, "9"),
        DRAW_TWO(10, "+2"),
        SKIP(11, "SKIP"),
        REVERSE(12, "REVERSE"),
        WILD(13, "WILD"),
        WILD_DRAW_FOUR(14, "+4")
    }

    fun jsonEncode(): Int = suit.value + type.value

    infix fun cmpCard(card: UnoCard) = suit == card.suit || type == card.type || suit == Suit.WILD || card.suit == Suit.WILD

    companion object {
        fun jsonDecode(value: Int): UnoCard {
            val suit = when (value / 100) {
                1 -> Suit.RED
                2 -> Suit.GREEN
                3 -> Suit.BLUE
                4 -> Suit.YELLOW
                5 -> Suit.WILD
                else -> throw IllegalArgumentException("Invalid card suit")
            }
            val type = when (value % 100) {
                0 -> Type.ZERO
                1 -> Type.ONE
                2 -> Type.TWO
                3 -> Type.THREE
                4 -> Type.FOUR
                5 -> Type.FIVE
                6 -> Type.SIX
                7 -> Type.SEVEN
                8 -> Type.EIGHT
                9 -> Type.NINE
                10 -> Type.DRAW_TWO
                11 -> Type.SKIP
                12 -> Type.REVERSE
                13 -> Type.WILD
                14 -> Type.WILD_DRAW_FOUR
                else -> throw IllegalArgumentException("Invalid card type")
            }
            return UnoCard(suit, type)
        }

        fun createDeck(): MutableList<UnoCard> {
            val deck = mutableListOf<UnoCard>()
            Type.values().forEach { type ->
                type.ordinal
                when (type) {
                    Type.ZERO -> {
                        Suit.values().forEach { suit ->
                            if (suit !== Suit.WILD) deck.add(UnoCard(suit, type))
                        }
                    }
                    in Type.ONE..Type.REVERSE -> {
                        Suit.values().forEach { suit ->
                            if (suit !== Suit.WILD) repeat(2) { deck.add(UnoCard(suit, type)) }
                        }
                    }
                    Type.WILD, Type.WILD_DRAW_FOUR -> {
                        repeat(4) { deck.add(UnoCard(Suit.WILD, type)) }
                    }
                    else -> throw IllegalArgumentException("Invalid card type")
                }
            }
            deck.shuffle()
            return deck
        }
    }
}