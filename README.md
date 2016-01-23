# scala-blackjack
A simple Blackjack game made with Scala.

How to install on Windows:
-
1. Install the Scala programming language distribution according to the guidelines given in this link: http://www.scala-lang.org/download/install.html

2. Add the environment variables according to the link above.

3. In the command prompt, navigate to the folder where the <code>PlayBlackjack.scala</code> resides on your computer.

4. In the command prompt, run <code>scalac PlayBlackjack.scala</code>.

5. In the command prompt, run <code>scala -Xnojline</code> to run the Scala REPL without JLine. (This way you can see what you type in the console.)

6. In Scala REPL, run <code>PlayBlackjack.main(Array.empty)</code> to play Blackjack. Enjoy!

Commands in Blackjack:
-
- When asked a yes/no question you can answer: <code>yes</code>, <code>YES</code>, <code>y</code>, <code>Y</code> or <code>no</code>, <code>NO</code>, <code>n</code>, <code>N</code>.
- When asked a hit/stand question you can answer: <code>hit</code>, <code>HIT</code>, <code>h</code>, <code>H</code> or <code>stand</code>, <code>STAND</code>, <code>s</code>, <code>S</code>.
