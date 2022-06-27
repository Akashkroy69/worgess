package com.example.worgess

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.properties.Delegates

class GameFragmentViewModel : ViewModel() {
    private val words = listOf("Ashish", "Pratik", "Aman", "Akash", "Nishu", "Abhilash")

    // listOf("Android", "Activity", "Fragment", "Akash", "Nishu", "Aman", "Ask", "Hi")
    private lateinit var secretWord: String
    private var correctGuess by Delegates.notNull<String>()

    //we are going to make each MutableLiveData property private, so it can't be
    //accessed and changed from other classes.
    private val _livesLeft = MutableLiveData<Int>()

    //At the same time we are providing a read only version of the same
    //property so that it can be accessed sometimes as per the need
    //This is called BACKING PROPERTY.
    val livesLeft: LiveData<Int> get() = _livesLeft

    private val _incorrectGuesses = MutableLiveData<String>()
    val incorrectGuesses: LiveData<String> get() = _incorrectGuesses

    private val _secretWordDisplay = MutableLiveData<String>()
    val secretWordDisplay: LiveData<String> get() = _secretWordDisplay

    private val _hint = MutableLiveData<String>()
    val hint: LiveData<String> get() = _hint

    private val _gameOver = MutableLiveData<Boolean>(false)
    val gameOver: LiveData<Boolean> get() = _gameOver

    init {
        setValuesForInitialSateForSecretWord()
    }

    fun setValuesForInitialSateForSecretWord() {
        secretWord = words.random().uppercase()
        _livesLeft.value = 3
        correctGuess = ""
        _incorrectGuesses.value = ""
        _secretWordDisplay.value = deriveSecretWordDisplay()
        _hint.value = when (secretWord) {
            "ASHISH" -> "Android Guru, a very close friend sky has, like a brother"
            "AKASH" -> "sky has no limit"
            "AMAN" -> "Its a name, the meaning of the name is peace"
            "NISHU" -> "Sky also has girlfriend, but who"
            "PRATIK" -> "Symbol of HAWAS, a very kind hearted person"
            "ABHILASH" -> "Sumi wala"
            else -> ""
        }
    }

    private fun deriveSecretWordDisplay(): String {
        var display = ""
        secretWord.forEach {
            display += checkCharacter(it.toString())
        }
        return display

    }

    private fun checkCharacter(charFromSecretWord: String) =
        when (correctGuess.contains(charFromSecretWord, ignoreCase = true)) {
            true -> charFromSecretWord
            false -> "_"
        }

    fun makeGuess(charFromInputAreaAsString: String) {
        if (secretWord.contains(charFromInputAreaAsString, ignoreCase = true)) {
            correctGuess += charFromInputAreaAsString
            _secretWordDisplay.value = deriveSecretWordDisplay()
        } else {

            //why can't we write livesLeft.value-- ? We can not write this
            // as value can be null also and 1 can't be reduced from the null.
            _livesLeft.value = livesLeft.value?.minus(1)
            _incorrectGuesses.value += charFromInputAreaAsString.uppercase() + " "
        }
        if (isLost() || isWon()) _gameOver.value = true
    }

    private fun isWon() = secretWord.equals(_secretWordDisplay.value, true)


    //here livesLeft.value ?: 0 returns 0 if the value is null
    private fun isLost() = livesLeft.value ?: 0 <= 0

    fun messageWinLoss(): String {
        val message: String = if (isWon()) {
            "Hurray, You Won!"
        } else {
            "Oh, You Lost!"
        }

        return "$message the secret word was ${secretWord.uppercase()}"
    }
    fun finishGame(){
        _gameOver.value = true
    }

    /*
    * his method gets called optionally just before the view model gets cleared away,
    *  and it gives you a chance to dispose of any resources:*/
    override fun onCleared() {
        super.onCleared()
        Log.i("GameViewModel", "cleared")
    }
}