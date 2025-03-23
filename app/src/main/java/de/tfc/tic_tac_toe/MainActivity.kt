package de.tfc.tic_tac_toe


import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView


class MainActivity : AppCompatActivity() {

    private lateinit var buttons: Array<Array<Button>>
    private var player1Turn = true
    private var roundCount = 0
    private var currentToast: Toast? = null // Declare a variable for the current Toast

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        showTurnToast()

        buttons = Array(3) { Array(3) { Button(this) } }

        // Directly reference buttons by their IDs
        buttons[0][0] = findViewById(R.id.button1)
        buttons[0][1] = findViewById(R.id.button2)
        buttons[0][2] = findViewById(R.id.button3)
        buttons[1][0] = findViewById(R.id.button4)
        buttons[1][1] = findViewById(R.id.button5)
        buttons[1][2] = findViewById(R.id.button6)
        buttons[2][0] = findViewById(R.id.button7)
        buttons[2][1] = findViewById(R.id.button8)
        buttons[2][2] = findViewById(R.id.button9)

        // Set click listeners for each button
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                buttons[i][j].setOnClickListener(ButtonClickListener())
            }
        }
    }

    private inner class ButtonClickListener : View.OnClickListener {
        override fun onClick(v: View) {

            if ((v as Button).text.toString() != "") {
                return
            }

            v.text = if (player1Turn) "X" else "O"
            roundCount++

            if (checkForWin()) {
                if (player1Turn) {
                    showWinnerDialog("X wins!")
                } else {
                    showWinnerDialog("O wins!")
                }

            } else if (roundCount == 9) {
                showWinnerDialog("It's a draw!")

            } else {
                player1Turn = !player1Turn
                showTurnToast()
            }

        }

    }
    private fun showTurnToast() {
        // Cancel the existing Toast if it's already shown
        currentToast?.cancel()

        // Create a new Toast message
        val message = if (player1Turn) "X turn" else "O turn"
        currentToast = Toast.makeText(this, message, Toast.LENGTH_SHORT)
        currentToast?.show()
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

    private fun resetGame() {
        roundCount = 0
        player1Turn = true
        showTurnToast()
        for (i in 0 until 3) {
            for (j in 0 until 3) {
                buttons[i][j].text = ""
            }
        }
    }

    private fun showWinnerDialog(message: String) {
        // Inflate the custom dialog layout
        val dialogView = layoutInflater.inflate(R.layout.dialog_winner, null)
        val winnerMessageTextView: TextView = dialogView.findViewById(R.id.winnerMessage)
        val resetButton: Button = dialogView.findViewById(R.id.resetButton)

        // Set the winner message
        winnerMessageTextView.text = message

        // Create the dialog
        val builder = AlertDialog.Builder(this)
        builder.setView(dialogView)
        val dialog = builder.create()
        // Set the dialog to be non-cancelable
        dialog.setCancelable(false)
        // Set the reset button click listener
        resetButton.setOnClickListener {
            resetGame()
            dialog.dismiss()
        }

        // Show the dialog
        dialog.show()
    }
}