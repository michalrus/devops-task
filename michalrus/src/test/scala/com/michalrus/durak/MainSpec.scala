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

  def checkFile(dataName: os.Path, resultName: os.Path) = {
    ("Example data ‘" + dataName.toString + "’") should ("return results from ‘" + resultName.toString + "’") in {
      val games = GameParser.parseFile(dataName).get
      info(games.toList.toString)
    }
  }

  checkFile(os.pwd / os.up / "example-data1.txt", os.pwd / "example-result1.txt")
  // checkFile(os.pwd / os.up / "example-data2.txt", os.pwd / "example-result2.txt")
  // checkFile(os.pwd / os.up / "data.txt", os.pwd / os.up / "result.txt")

}
