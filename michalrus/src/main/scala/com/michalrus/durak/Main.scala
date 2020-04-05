package com.michalrus.durak

object Main {

  // TODO: maybe get rid of `Try#get`, but `main()` is so simple currently,
  // that we probably want to crash early with a full stack trace.
  @SuppressWarnings(Array("org.wartremover.warts.TryPartial"))
  def main(args: Array[String]): Unit = {
    args match {
      case Array(path) =>
        println(
          GameParser
            .parseFile(os.Path(path, base = os.pwd))
            .get
            .map(game => Game.play(game.get))
            .mkString("")
        )
      case _ =>
        System.err.println("Usage: $0 <input-file>")
        System.exit(-1)
    }
  }

}
