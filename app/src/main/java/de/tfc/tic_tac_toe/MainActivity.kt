package de.tfc.tic_tac_toe

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class MainActivity : AppCompatActivity() {

    private lateinit var buttons: Array<Array<Button>>
    private var player1Turn = true
    private var roundCount = 0

    private var playerXScore = 0
    private var playerOScore = 0

    private lateinit var textPlayerXScore: TextView
    private lateinit var textPlayerOScore: TextView
    private lateinit var textTurn: TextView
    private lateinit var btnReset: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        textPlayerXScore = findViewById(R.id.textPlayerXScore)
        textPlayerOScore = findViewById(R.id.textPlayerOScore)
        textTurn = findViewById(R.id.textTurn)
        btnReset = findViewById(R.id.btnReset)

        buttons = Array(3) { Array(3) { Button(this) } }

        buttons[0][0] = findViewById(R.id.button1)
        buttons[0][1] = findViewById(R.id.button2)
        buttons[0][2] = findViewById(R.id.button3)
        buttons[1][0] = findViewById(R.id.button4)
        buttons[1][1] = findViewById(R.id.button5)
        buttons[1][2] = findViewById(R.id.button6)
        buttons[2][0] = findViewById(R.id.button7)
        buttons[2][1] = findViewById(R.id.button8)
        buttons[2][2] = findViewById(R.id.button9)

        for (i in 0 until 3) {
            for (j in 0 until 3) {
                buttons[i][j].setOnClickListener(ButtonClickListener())
            }
        }

        btnReset.setOnClickListener {
            resetAll()
        }

        updateTurnUI()
    }

    private inner class ButtonClickListener : View.OnClickListener {
        override fun onClick(v: View) {
            val button = v as Button
            if (button.text.toString() != "") {
                return
            }

            if (player1Turn) {
                button.text = "X"
                button.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.player_x))
            } else {
                button.text = "O"
                button.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.player_o))
            }

            roundCount++

            if (checkForWin()) {
                if (player1Turn) {
                    playerXScore++
                    textPlayerXScore.text = playerXScore.toString()
                    showWinnerDialog("Player X Wins!")
                } else {
                    playerOScore++
                    textPlayerOScore.text = playerOScore.toString()
                    showWinnerDialog("Player O Wins!")
                }
            } else if (roundCount == 9) {
                showWinnerDialog("It's a Draw!")
            } else {
                player1Turn = !player1Turn
                updateTurnUI()
            }
        }
    }

    private fun updateTurnUI() {
        if (player1Turn) {
            textTurn.text = "Player X's Turn"
            textTurn.setTextColor(ContextCompat.getColor(this, R.color.player_x))
        } else {
            textTurn.text = "Player O's Turn"
            textTurn.setTextColor(ContextCompat.getColor(this, R.color.player_o))
        }
    }

    private fun checkForWin(): Boolean {
        val field = Array(3) { arrayOfNulls<String>(3) }
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                field[i][j] = buttons[i][j].text.toString()
            }
        }

        // Check rows and columns
        for (i in 0 until 3) {
            if (field[i][0] == field[i][1] && field[i][0] == field[i][2] && !field[i][0].isNullOrEmpty()) {
                return true
            }
            if (field[0][i] == field[1][i] && field[0][i] == field[2][i] && !field[0][i].isNullOrEmpty()) {
                return true
            }
        }

        // Check diagonals
        if (field[0][0] == field[1][1] && field[0][0] == field[2][2] && !field[0][0].isNullOrEmpty()) {
            return true
        }
        if (field[0][2] == field[1][1] && field[0][2] == field[2][0] && !field[0][2].isNullOrEmpty()) {
            return true
        }

        return false
    }

    private fun resetBoard() {
        roundCount = 0
        player1Turn = true
        updateTurnUI()
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                buttons[i][j].text = ""
            }
        }
    }

    private fun resetAll() {
        playerXScore = 0
        playerOScore = 0
        textPlayerXScore.text = "0"
        textPlayerOScore.text = "0"
        resetBoard()
    }

    private fun showWinnerDialog(message: String) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_winner, null)
        val winnerMessageTextView: TextView = dialogView.findViewById(R.id.winnerMessage)
        val resetButton: Button = dialogView.findViewById(R.id.resetButton)

        winnerMessageTextView.text = message

        val builder = MaterialAlertDialogBuilder(this)
        builder.setView(dialogView)
        val dialog = builder.create()
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.setCancelable(false)

        resetButton.setOnClickListener {
            resetBoard()
            dialog.dismiss()
        }

        dialog.show()
    }
}