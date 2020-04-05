# Running

To test the solution:

```
$ git clone <this-repo>
$ cd devops-task/
$ git checkout michalrus
$ cd michalrus/
```

… and then either test provided input files with:

```
sbt test
```

… or give an `stdin` of your liking to:

```
sbt run
```

# Subtasks

* [x] project skel

    * [x] https://github.com/benetis/benetis.me/blob/master/build.sbt

    * [x] wartremover

    * [x] scalafmt

    * [x] ScalaTest, ScalaCheck

* [x] streaming parsing of input files

* [x] tests using provided input and outputs

    * [x] what happens to Git symlinks on Windows™? The repo has chmod +x on all files, so it was created on Windows™…

        * [x] [they cannot be used reliably](https://www.google.com/search?hl=en&q=git+symlinks+windows), so I’ll use `pwd` and point to `../` for now

* [x] algorithm

  * It isn't said in the instruction to defend against non-trump table cards first, but let's assume that, based on provided examples…

* [ ] perhaps QuickCheck for some random decks

* [ ] in `main()`, read from `stdin`, and output to `stdout`, as per:
        > For each given line, print 0 if there is a tie (both players have no cards in their hands), 1 if p1 wins, 2 if p2 wins.

# Resources

* https://en.wikipedia.org/wiki/Durak

* ⏯ How To Play Durak @ https://www.youtube.com/watch?v=3JagmUmUJOc

* ⏯ Durak - In Plain English @ https://www.youtube.com/watch?v=37jAiFBQCz4

* https://www.pagat.com/beating/podkidnoy_durak.html

# Further research

1. Make more optimal decisions w.r.t. where to sort, convert to lists etc. in `Game.scala`.

1. Allow & implement *all* legal (and real) Durak rules.

1. See if there’s an optimal algorithmic strategy.

1. If not, teach a neural net to play it (by playing against itself?).

1. Use that NN for input data, and output true values w.r.t. full legal rules.

1. Add more players, teams, talon etc.

1. …?
