package com.michalrus.durak

import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck.Checkers

class MainSpec extends AnyFlatSpec with Matchers with Checkers {

  "Addition" should "be commutative" in {
    1 + 2 === 2 + 1
  }

  "String concat" should "work" in check((a: String, b: String) => a.size <= (a + b).size)

}
