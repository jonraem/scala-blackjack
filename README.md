# scala-blackjack
A simple Blackjack game made with Scala.

How to install on Windows:
-
1. Install the Scala programming language distribution according to the guidelines given in this link: http://www.scala-lang.org/download/install.html

2. Add the environment variables according to the link above.

3. In the command prompt, navigate to the folder where the file 'PlayBlackjack.scala' resides on your computer.

4. In the command prompt, run 'scalac PlayBlackjack.scala'.

5. In the command prompt, run 'scala -Xnojline' to run the Scala REPL without JLine. (This way you can see what you type in the console.)

6. In Scala REPL, run 'PlayBlackjack.main(Array.empty)' to play Blackjack. Enjoy!

Commands in Blackjack:
-
- When asked a yes/no question you can answer: 'yes', 'YES', 'y', 'Y' or 'no', 'NO', 'n', 'N'
- When asked a hit/stand question you can answer: 'hit', 'HIT', 'h', 'H' or 'stand', 'STAND', 's', 'S'
- When the hand has been played and the console asks if you'd like to play again, you can cash out the chips in your pot by answering: 'cashout', 'CASHOUT', 'c', 'C'
