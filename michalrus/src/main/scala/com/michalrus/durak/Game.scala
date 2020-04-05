package com.michalrus.durak

import scala.annotation.tailrec

final case class Game(trump: Suit, playerA: Set[Card], playerB: Set[Card])

object Game {

  def play(game: Game): Int = {
    val s0 =
      State(game.trump, game.playerA, game.playerB, playerAIsAttacker = true, table = Set.empty)

    // TODO: the State monad could be incorporated, but `@tailrec` is arguably more readable in this case
    @tailrec def loop(si: State, numTurns: Int): Int = {
      endResult(si) match {
        case Some(res) => res
        // limit to 1000 turns, and return nonsense in case this is non-terminating for some inputs:
        case _ if numTurns >= 1000 => 9
        case _                     => loop(playTurn(si), numTurns + 1)
      }
    }

    loop(s0, numTurns = 0)
  }

  final case class State(
      trump: Suit,
      playerA: Set[Card],
      playerB: Set[Card],
      playerAIsAttacker: Boolean,
      table: Set[(Card, Option[Card])]
  )

  def playTurn(s: State): State = {
    (playFirstCard _ andThen passAll andThen defendReinforceLoop)(s)
  }

  def endResult(s: State): Option[Int] =
    if (s.playerA.isEmpty && s.playerB.isEmpty) Some(0)
    else if (s.playerA.isEmpty) Some(1)
    else if (s.playerB.isEmpty) Some(2)
    else None

  def attackerCards(s: State): Set[Card] = if (s.playerAIsAttacker) s.playerA else s.playerB

  def defenderCards(s: State): Set[Card] = if (s.playerAIsAttacker) s.playerB else s.playerA

  // 1.
  def playFirstCard(s: State): State = {
    val firstCard = (attackerCards(s).filter(_.suit != s.trump).minOption) orElse (attackerCards(s)
      .filter(_.suit == s.trump)
      .minOption)

    firstCard.fold(s)(card =>
      s.copy(
        table = s.table + ((card, None)),
        playerA = s.playerA - card,
        playerB = s.playerB - card
      )
    )
  }

  // 2.
  def passOne(s: State): State = {
    // “This can only be done if offense has enough cards to defend all of them.”
    val numberOnAttackersHand = attackerCards(s).size
    val currentlyNonCovered   = s.table.count(_._2.isEmpty)

    if (currentlyNonCovered >= numberOnAttackersHand) s
    else {
      val rankToMatch  = s.table.filter(_._2.isEmpty).minOption.map(_._1.rank)
      val matchingCard = defenderCards(s).filter(c => Some(c.rank) == rankToMatch).minOption
      matchingCard.fold(s)(card =>
        s.copy(
          table = s.table + ((card, None)),
          playerAIsAttacker = !s.playerAIsAttacker,
          playerA = s.playerA - card,
          playerB = s.playerB - card
        )
      )
    }
  }

  def passAll(s: State): State = {
    @tailrec def loop(si: State): State = {
      val sj = passOne(si)
      if (sj == si) sj else loop(sj)
    }

    loop(s)
  }

  // 3.
  def defend(s0: State): State = {
    @tailrec def loop(si: State): State = {
      val cardToDefendAgainst = {
        val allEmpty = si.table.filter(_._2.isEmpty).map(_._1)
        // Now, it isn't said in the instruction to defend against non-trump table cards first,
        // but let's assume that, based on provided examples…
        val (trump, nonTrump) = allEmpty.partition(_.suit == si.trump)
        nonTrump.minOption orElse trump.minOption
      }

      val sj = cardToDefendAgainst.fold(si)(attack => {
        val defendWith = {
          defenderCards(si)
            .filter(hand => hand.suit == attack.suit && hand.rank > attack.rank)
            .minOption
            .orElse {
              // We can defeat non-trump cards with lowest-rank trump ones:
              if (attack.suit == si.trump) None
              else defenderCards(si).filter(_.suit == si.trump).minOption
            }
        }

        defendWith.fold(si)(defence =>
          si.copy(
            playerA = si.playerA - defence,
            playerB = si.playerB - defence,
            table = si.table.map({
              case (card, None) if card == attack => (card, Some(defence))
              case x                              => x
            })
          )
        )
      })

      if (si == sj) sj else loop(sj)
    }
    loop(s0)
  }

  def areAllCovered(s: State): Boolean = !s.table.exists(_._2.isEmpty)

  // 4.
  def reinforce(s: State): State = {
    val ranksOnTable          = cardsOnTable(s).map(_.rank) // Set shouldn't be a functor :o
    val matchingOnHand        = attackerCards(s).filter(ranksOnTable contains _.rank)
    val numberOnDefendersHand = defenderCards(s).size
    val currentlyNonCovered   = s.table.count(_._2.isEmpty)
    val canReinforceWithNum   = numberOnDefendersHand - currentlyNonCovered
    val reinforceWith         = matchingOnHand.toList.sorted.take(canReinforceWithNum).toSet
    s.copy(
      playerA = s.playerA -- reinforceWith,
      playerB = s.playerB -- reinforceWith,
      table = s.table ++ reinforceWith.map((_, None))
    )
  }

  // 5.
  def defendReinforceLoop(s: State): State = {
    @tailrec def loop(si: State): State = {
      val sid = defend(si)
      val sir = reinforce(sid)

      if (areAllCovered(sir))
        sir.copy(
          table = Set.empty,
          playerAIsAttacker = !sir.playerAIsAttacker
        )
      else if (sir == si) tableToDefender(sir)
      else loop(sir)
    }

    loop(s)
  }

  def cardsOnTable(s: State): Set[Card] = s.table.map(_._1) ++ s.table.flatMap(_._2)

  def tableToDefender(s: State): State =
    s.copy(
      table = Set.empty,
      playerA = if (s.playerAIsAttacker) s.playerA else s.playerA ++ cardsOnTable(s),
      playerB = if (s.playerAIsAttacker) s.playerB ++ cardsOnTable(s) else s.playerB
    )
}
