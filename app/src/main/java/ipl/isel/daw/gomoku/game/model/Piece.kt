package ipl.isel.daw.gomoku.game.model

import ipl.isel.daw.gomoku.R


enum class Type(private val letter: Char) {
    SPACE('+'),
    PLAYER_1('1'),
    PLAYER_2('2');

    fun get(): Char = letter

    override fun toString(): String {
        return letter.toString()
    }

    fun toImage(): Int = when (letter) { //TODO deal with this
        '+' -> R.drawable.grid
        '2' -> R.drawable.knuckles_piece
        '1' -> R.drawable.sonic_piece
        else -> R.drawable.grid
    }
}

class Piece(var type: Type) {

    override fun equals(other: Any?) = if (other !is Piece) false else (type == other.type)

    override fun toString() = this.type.toString().uppercase()

    override fun hashCode() = type.hashCode()

}

fun Char.toPiece(): Piece = this.toPieceOrNull() ?: throw IllegalArgumentException()


fun Char.toPieceOrNull(): Piece? = when (this) {
    '+' -> Piece(Type.SPACE)
    '2' -> Piece(Type.PLAYER_2)
    '1' -> Piece(Type.PLAYER_1)
    else -> null
}
