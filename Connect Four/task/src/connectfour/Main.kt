package connectfour

class ConnectFour {
    private var board = mutableListOf(0, 0)
    private var playerFigure = "o"
    private var gameField = MutableList(board[0]) {MutableList(board[1]) { " " } }
    private var gameOver = ""
    private var numberOfGames: Int = 0
    private var countdown = 1
    private val score = mutableListOf(0, 0)



    fun startGame() {
        println("Connect Four\nFirst player's name:")
        val firstPlayer = readln()
        println("Second player's name:")
        val secondPlayer = readln()
        checkUp()
        checkCountGames()

        println("""
            $firstPlayer VS $secondPlayer
            ${board[0]} X ${board[1]} board""".trimIndent())
        if (numberOfGames == 1) println("Single game") else println("Total $numberOfGames games\nGame #1")
        printGameField(gameField)

        while (true) {
            println("${whoseTurn(firstPlayer, secondPlayer)}'s turn:")
            when (val input = readln().trim()) {
                "end" -> {
                    println("Game over!")
                    break
                }
                else -> gameContinue(input)
            }
            if (gameOver == "won") {
                println("Player ${whoseTurn(firstPlayer, secondPlayer)} won")
                if (numberOfGames == 1) {
                    println("Game over!")
                    break
                }
                if (numberOfGames > 1) {
                    if (playerFigure == "o") score[0] +=2 else score[1] +=2
                    println("Score\n$firstPlayer: ${score[0]} $secondPlayer: ${score[1]}")
                    playerFigure = if (playerFigure == "o") "*" else "o"
                    if (numberOfGames == countdown) {
                        println("Game over!")
                        break
                    }
                    if (numberOfGames != countdown) {
                        gameOver = ""
                        countdown++
                        gameField = MutableList(board[0]) {MutableList(board[1]) { " " } }
                        println("Game #$countdown")
                        printGameField(gameField)
                    }
                }

            }
            if (checkDraw()) {
                println("It is a draw")
                if (numberOfGames > 1) {
                    score[0] +=1
                    score[1] +=1
                    println("Score\n$firstPlayer: ${score[0]} $secondPlayer: ${score[1]}")
                }
                if (numberOfGames == countdown) {
                    println("Game over!")
                    break
                } else {
                    countdown++
                    gameField = MutableList(board[0]) {MutableList(board[1]) { " " } }
                    println("Game #$countdown")
                    printGameField(gameField)
                }
            }
        }
    }

    private fun checkCountGames() {
        println("""
            Do you want to play single or multiple games?
            For a single game, input 1 or press Enter
            Input a number of games:""".trimIndent())
        val read = readln()
        val r = read.toIntOrNull() ?: 0
        if (read.isBlank()) {
            numberOfGames = 1
        } else {
            if (r < 1) {
                println("Invalid input")
                checkCountGames()
            } else numberOfGames = r
        }
    }

    private fun gameContinue(input: String) {
        val check = input.toIntOrNull()
        if (check == null) {
            println("Incorrect column number")
        } else {
            if (check !in 1..board[1]) {
                println("The column number is out of range (1 - ${board[1]})")
            } else stepPlayer(check)
        }
    }

    private fun stepPlayer(check: Int) {
        if (gameField[0][check - 1] != " ") {
            println("Column $check is full")
            return
        }
        for (i in board[0] - 1 downTo 0) {
            if (gameField[i][check - 1] == " ") {
                gameField[i][check - 1] = playerFigure
                printGameField(gameField)
                if (checkWin()) {
                    gameOver = "won"
                    break
                }
                playerFigure = if (playerFigure == "o") "*" else "o"
                break
            }
        }
    }

    private fun whoseTurn(first: String, second: String): String {
        return  if (playerFigure == "o") first else second
    }

    private fun checkUp() {
        println("""
            Set the board dimensions (Rows x Columns)
            Press Enter for default (6 x 7)""".trimIndent())
        val input = readln().lowercase().filter { !it.isWhitespace() }.lowercase()
        if (input.isBlank()) board = mutableListOf(6, 7)
        val inp = input.split("x")
        if (input.isNotBlank()) {
            if (!input.matches("\\d+x\\d+".toRegex())) {
                println("Invalid input")
                checkUp()
            } else {
                if (inp[0].toInt() !in 5..9) {
                    println("Board rows should be from 5 to 9")
                    checkUp()
                } else if (inp[1].toInt() !in 5..9) {
                    println("Board columns should be from 5 to 9")
                    checkUp()
                } else {
                    board[0] = inp[0].toInt()
                    board[1] = inp[1].toInt()
                }
            }
        }
        gameField = MutableList(board[0]) {MutableList(board[1]) { " " } }
    }

    private fun printGameField(field: MutableList<MutableList<String>>) {
        for (i in 1..board[1]) print(" $i")
        println()
        for (i in 0 until board[0]) {
            print("║${field[i].joinToString("║")}║\n")
        }
        println("╚═${"╩═".repeat(board[1] - 1)}╝")
    }

    private fun checkWin(): Boolean {
        var bool = false
        for (i in 0 until board[0]) {
            for (j in 0 until board[1]) {
                val player = gameField[i][j]
                if (player == " ") continue
                if (j + 3 < board[1] &&
                    player == gameField[i][j + 1] &&
                    player == gameField[i][j + 2] &&
                    player == gameField[i][j + 3]) bool = true // horizontal

                if (i + 3 < board[0]) {
                    if (player == gameField[i + 1][j] &&
                        player == gameField[i + 2][j] &&
                        player == gameField[i + 3][j]) bool = true // vertical

                    if (j + 3 < board[1] &&
                        player == gameField[i + 1][j + 1] &&
                        player == gameField[i + 2][j + 2] &&
                        player == gameField[i + 3][j + 3]) bool = true // diagonal down right

                    if (j - 3 >= 0 &&
                        player == gameField[i + 1][j - 1] &&
                        player == gameField[i + 2][j - 2] &&
                        player == gameField[i + 3][j - 3]) bool = true // diagonal down left
                }

            }
        }
        return bool
    }

    private fun checkDraw(): Boolean {
        return " " !in gameField[0]
    }
}

fun main() {
    ConnectFour().startGame()
}
