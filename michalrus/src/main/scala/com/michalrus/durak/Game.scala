package com.michalrus.durak

final case class Game(trump: Suit, playerA: Set[Card], playerB: Set[Card])

object Game {}
