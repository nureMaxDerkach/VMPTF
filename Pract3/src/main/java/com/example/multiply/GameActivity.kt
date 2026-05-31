package com.example.multiply

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class GameActivity : AppCompatActivity() {

    data class WordEntry(val word: String, val category: String, val hint: String)

    private val wordList = listOf(
        WordEntry("KOTLIN",   "Мова програмування", "Мова для Android"),
        WordEntry("ANDROID",  "Платформа",          "Мобільна ОС від Google"),
        WordEntry("ПРОГРАМА", "IT",                  "Набір інструкцій для ЕОМ"),
        WordEntry("ЗМІННА",   "Програмування",       "Зберігає значення в пам'яті"),
        WordEntry("ФУНКЦІЯ",  "Програмування",       "Блок коду з іменем"),
        WordEntry("КЛАС",     "ООП",                 "Шаблон для створення об'єктів"),
        WordEntry("ЕКРАН",    "Android UI",          "Відображає інтерфейс"),
        WordEntry("КНОПКА",   "Android UI",          "Елемент для натискання"),
    )

    private lateinit var currentEntry: WordEntry
    private var guessedLetters = mutableSetOf<Char>()
    private var attemptsLeft = 6

    private lateinit var tvWord: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvAttemptsLeft: TextView
    private lateinit var tvUsedLetters: TextView
    private lateinit var tvResult: TextView
    private lateinit var etLetter: EditText
    private lateinit var btnGuess: Button
    private lateinit var btnNewGame: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_game)

        tvWord         = findViewById(R.id.tvWord)
        tvCategory     = findViewById(R.id.tvCategory)
        tvAttemptsLeft = findViewById(R.id.tvAttemptsLeft)
        tvUsedLetters  = findViewById(R.id.tvUsedLetters)
        tvResult       = findViewById(R.id.tvResult)
        etLetter       = findViewById(R.id.etLetter)
        btnGuess       = findViewById(R.id.btnGuess)
        btnNewGame     = findViewById(R.id.btnNewGame)

        btnGuess.setOnClickListener { onGuess() }
        btnNewGame.setOnClickListener { startGame() }

        startGame()
    }

    private fun startGame() {
        currentEntry   = wordList.random()
        guessedLetters = mutableSetOf()
        attemptsLeft   = 6
        tvResult.text  = ""
        btnGuess.isEnabled = true
        etLetter.text.clear()
        updateUI()
    }

    private fun onGuess() {
        val input = etLetter.text.toString().uppercase()
        etLetter.text.clear()

        if (input.isEmpty()) {
            Toast.makeText(this, "Введіть букву!", Toast.LENGTH_SHORT).show()
            return
        }

        val letter = input[0]

        if (letter in guessedLetters) {
            Toast.makeText(this, "Ця буква вже була!", Toast.LENGTH_SHORT).show()
            return
        }

        guessedLetters.add(letter)

        if (letter !in currentEntry.word) {
            attemptsLeft--
        }

        updateUI()
        checkGameOver()
    }

    private fun buildWordDisplay(): String {
        return currentEntry.word.map { ch ->
            if (ch in guessedLetters) ch else '_'
        }.joinToString(" ")
    }

    private fun updateUI() {
        tvWord.text = buildWordDisplay()
        tvCategory.text = "Категорія: ${currentEntry.category} | Підказка: ${currentEntry.hint}"
        tvAttemptsLeft.text = "Спроб залишилось: $attemptsLeft"
        tvUsedLetters.text = "Введені букви: ${guessedLetters.sorted().joinToString(", ")}"
    }

    private fun checkGameOver() {
        val allGuessed = currentEntry.word.all { it in guessedLetters }
        when {
            allGuessed -> {
                tvResult.text = "🎉 Вгадав! Слово: ${currentEntry.word}"
                btnGuess.isEnabled = false
            }
            attemptsLeft == 0 -> {
                tvResult.text = "😢 Програш! Слово було: ${currentEntry.word}"
                btnGuess.isEnabled = false
                tvWord.text = currentEntry.word.map { it }.joinToString(" ")
            }
        }
    }
}
