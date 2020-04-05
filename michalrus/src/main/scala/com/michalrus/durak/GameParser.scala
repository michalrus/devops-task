package com.michalrus.durak

import geny.Generator
import org.parboiled2._

import scala.util.Try

@SuppressWarnings(Array("org.wartremover.warts.All"))
class GameParser(val input: ParserInput) extends Parser {

  def TrumpLine: Rule1[Suit]                  = rule { WS0 ~ SuitR ~ WS0 ~ EOI }
  def DealLine: Rule1[(Set[Card], Set[Card])] = rule { DealR ~ EOI }

  def DealR: Rule1[(Set[Card], Set[Card])] = rule {
    HandR ~ WS0 ~ ch('|') ~ HandR ~ WS0 ~> ((a: Set[Card], b: Set[Card]) => (a, b))
  }

  def HandR: Rule1[Set[Card]] = rule {
    WS0 ~ optional(CardR) ~ zeroOrMore(WS1 ~ CardR) ~> ((head: Option[Card], tail: Seq[Card]) =>
      head.toSet ++ tail.toSet
    )
  }

  def CardR: Rule1[Card] = rule {
    SuitR ~ RankR ~> ((suit, rank) => Card(rank, suit))
  }

  def SuitR: Rule1[Suit] = rule {
    valueMap(Map("H" -> Suit.Hearts, "D" -> Suit.Diamonds, "C" -> Suit.Clubs, "S" -> Suit.Spades))
  }

  def RankR: Rule1[Rank] = rule {
    valueMap(
      Map(
        "2" -> Rank.R2,
        "3" -> Rank.R3,
        "4" -> Rank.R4,
        "5" -> Rank.R5,
        "6" -> Rank.R6,
        "7" -> Rank.R7,
        "8" -> Rank.R8,
        "9" -> Rank.R9,
        "T" -> Rank.R10,
        "J" -> Rank.Jack,
        "Q" -> Rank.Queen,
        "K" -> Rank.King,
        "A" -> Rank.Ace
      )
    )
  }

  def WS0: Rule0 = rule { quiet(zeroOrMore(anyOf(" \t"))) }
  def WS1: Rule0 = rule { quiet(oneOrMore(anyOf(" \t"))) }

}

object GameParser {

  def parseFile(path: os.Path): Try[Generator[Try[Game]]] =
    for {
      stream    <- Try(os.read.lines.stream(path))
      trumpLine <- Try(stream.head)
      trump     <- new GameParser(trumpLine).TrumpLine.run()
    } yield stream
      .drop(1)
      .map(line => {
        new GameParser(line).DealLine
          .run()
          .map({ case (playerA, playerB) => Game(trump, playerA, playerB) })
      })

}
