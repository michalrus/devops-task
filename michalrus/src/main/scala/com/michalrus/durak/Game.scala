package com.michalrus.durak

final case class Game(trump: Suit, playerA: Set[Card], playerB: Set[Card])

object Game {

  def play(game: Game): Int = {
    val _ = game
    7
  }

}
