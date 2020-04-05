package com.michalrus.durak

import enumeratum.{Enum, EnumEntry}

sealed trait Suit extends EnumEntry with Ordered[Suit] {
  def compare(that: Suit) = Suit.indexOf(this) compare Suit.indexOf(that)
}

object Suit extends Enum[Suit] {
  val values = findValues

  case object Hearts   extends Suit
  case object Diamonds extends Suit
  case object Clubs    extends Suit
  case object Spades   extends Suit
}

sealed trait Rank extends EnumEntry with Ordered[Rank] {
  def compare(that: Rank) = Rank.indexOf(this) compare Rank.indexOf(that)
}

object Rank extends Enum[Rank] {
  val values = findValues
  case object R2    extends Rank
  case object R3    extends Rank
  case object R4    extends Rank
  case object R5    extends Rank
  case object R6    extends Rank
  case object R7    extends Rank
  case object R8    extends Rank
  case object R9    extends Rank
  case object R10   extends Rank
  case object Jack  extends Rank
  case object Queen extends Rank
  case object King  extends Rank
  case object Ace   extends Rank
}

final case class Card(rank: Rank, suit: Suit) extends Ordered[Card] {
  def compare(that: Card): Int = (this.rank, this.suit) compare ((that.rank, that.suit))
}
