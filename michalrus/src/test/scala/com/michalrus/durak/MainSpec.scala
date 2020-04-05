package com.michalrus.durak

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.Checkers
import scala.util.Success

class MainSpec extends AnyFlatSpec with Matchers with Checkers {

  "Addition" should "be commutative" in {
    1 + 2 should be(2 + 1)
  }

  "String concat" should "work" in check((a: String, b: String) => a.size <= (a + b).size)

  "Trump lines" should "parse" in {
    new GameParser("H").TrumpLine.run() should be(Success(Suit.Hearts))
  }

  "Deal lines" should "parse" in {
    import Suit._, Rank._
    new GameParser("ST D2 CJ HQ DA | H2 D3 C4 S5 H6 D7 C8 S9").DealLine.run() should be(
      Success(
        (
          Set(
            Card(R10, Spades),
            Card(R2, Diamonds),
            Card(Jack, Clubs),
            Card(Queen, Hearts),
            Card(Ace, Diamonds)
          ),
          Set(
            Card(R2, Hearts),
            Card(R3, Diamonds),
            Card(R4, Clubs),
            Card(R5, Spades),
            Card(R6, Hearts),
            Card(R7, Diamonds),
            Card(R8, Clubs),
            Card(R9, Spades)
          )
        )
      )
    )
  }

  "Cards" should "be ordered properly" in {
    import Suit._, Rank._
    val set = Set[Card](
      Card(R7, Clubs),
      Card(R2, Spades),
      Card(R5, Hearts),
      Card(R10, Spades),
      Card(R2, Diamonds)
    )
    info(set.toList.sorted.toString)
    Card(R7, Clubs) should be > Card(R2, Spades)
    set.min should be(Card(R2, Diamonds))
  }

  def checkFile(dataPath: os.Path, resultPath: os.Path) = {
    ("Example data ‘" + dataPath.toString + "’") should ("return results from ‘" + resultPath.toString + "’") in {
      val games = GameParser.parseFile(dataPath).get
      val plays = games.map(game => Game.play(game.get))
      plays.toList.mkString("") should be(os.read(resultPath).trim)
    }
  }

  checkFile(os.pwd / os.up / "example-data1.txt", os.pwd / "example-result1.txt")
  checkFile(os.pwd / os.up / "example-data2.txt", os.pwd / "example-result2.txt")
  checkFile(os.pwd / os.up / "data.txt", os.pwd / os.up / "results.txt")

  "Tricky case № 14" should "work, too" in {
    val p = new GameParser("H5 SQ CT | S3 C5 HK D6").DealLine.run().get
    Game.play(Game(Suit.Hearts, p._1, p._2)) should be(1)
  }

  "Tricky case № 24" should "work, too" in {
    val p = new GameParser("H2 H6 | C9 S2 H3").DealLine.run().get
    info(
      "When passing, make sure offence has enough cards on hand to (potentially) respond to all on the table."
    )
    Game.play(Game(Suit.Hearts, p._1, p._2)) should be(1)
  }

  "Tricky case № 83" should "work, too" in {
    val p = new GameParser("C6 H5 S9 H3 | SQ D2 HJ DT").DealLine.run().get
    Game.play(Game(Suit.Hearts, p._1, p._2)) should be(0)
  }

  "Tricky case № 93" should "work, too" in {
    val p = new GameParser("C9 C4 SA SJ H8 | H7 CK C3 SQ S5").DealLine.run().get
    info(
      "If attacking card is trump suit, we cannot beat it with *any* other trump card, only with higher rank!"
    )
    Game.play(Game(Suit.Hearts, p._1, p._2)) should be(1)
  }
}
