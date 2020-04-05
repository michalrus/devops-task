# Subtasks

* [x] project skel

    * [x] https://github.com/benetis/benetis.me/blob/master/build.sbt

    * [x] wartremover

    * [x] scalafmt

    * [x] ScalaTest, ScalaCheck

* [x] streaming parsing of input files

* [ ] tests using provided input and outputs

    * [x] what happens to Git symlinks on Windows™? The repo has chmod +x on all files, so it was created on Windows™…

        * [x] [they cannot be used reliably](https://www.google.com/search?hl=en&q=git+symlinks+windows), so I’ll use `pwd` and point to `../` for now

* [ ] algorithm

* [ ] perhaps QuickCheck for some random decks

# Resources

* https://en.wikipedia.org/wiki/Durak

* ⏯ How To Play Durak @ https://www.youtube.com/watch?v=3JagmUmUJOc

* ⏯ Durak - In Plain English @ https://www.youtube.com/watch?v=37jAiFBQCz4

* https://www.pagat.com/beating/podkidnoy_durak.html

# Further research

1. Allow & implement *all* legal Durak rules.

2. See if there’s an optimal algorithmic strategy.

3. If not, teach a neural net to play it (by playing against itself?).

4. Use that NN for input data, and output true values w.r.t. full legal rules.

5. Add more players, teams etc.

6. …?
