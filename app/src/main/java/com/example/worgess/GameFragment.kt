package com.example.worgess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.worgess.databinding.FragmentGameBinding


class GameFragment : Fragment() {

    //This is used when we use the xml layout file and data binding
    /* private var _binding: FragmentGameBinding? = null
     private val binding get() = _binding!!*/


    private lateinit var gameViewModel: GameFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //This is used when we use the xml layout file and data binding and compose view
        //setting up view binding class instance and view hierarchy for Fragment_Game layout file
        /*_binding = FragmentGameBinding.inflate(inflater, container, false).apply {
            composeViewGameF.setContent {
                MaterialTheme {
                    Surface {
                        view?.let { SetContentGameFragment(gameViewModel) }

                    }
                }
            }
        }*/
        //val view = _binding!!.root

        val composeViewForView = ComposeView(requireContext()).apply {
            setContent {
                SetContentGameFragment(gameViewModel = gameViewModel)
            }
        }

        //creating an instance of ViewModel class that survives the configuration changes.
        gameViewModel = ViewModelProvider(this).get(GameFragmentViewModel::class.java)

        //providing reference of the gameViewModel instance to the data binding variable for the GameFragmentViewModel
        //binding.gameFragmentViewModelDataBindingVariable = gameViewModel
        //if we want our views with data binding variables to respond on changes in properties value same as observe method
        //then we will need to assign the viewLifeCycleOwner with the view hierarchy like following:
        //binding.lifecycleOwner = viewLifecycleOwner


        //consists all the code for keeping the eye on the changes happening in the ViewModel class
        // and it helps the fragment change its UI component automatically.
        //observeTheLiveDataPropertiesFromGameViewModel()


        gameViewModel.gameOver.observe(viewLifecycleOwner, Observer { gameOver ->
            if (gameOver) {
                val actionWithMessageSafeArgs =
                    GameFragmentDirections.actionGameFragmentToResultFragment(gameViewModel.messageWinLoss())
                //from where this view is coming?
                //This view is from fragment's getView
                view?.findNavController()?.navigate((actionWithMessageSafeArgs))
            }

        })
        //This is used when we use the xml layout file and data binding
       /* binding.guessButt.setOnClickListener {
            gameViewModel.makeGuess(binding.idForInputArea.text.toString())
            binding.idForInputArea.text = null

        }

        //resets for a new game, a new word.
        _binding!!.resetFABid.setOnClickListener {
            gameViewModel.setValuesForInitialSateForSecretWord()
        }
*/

        return composeViewForView
    }

    private fun observeTheLiveDataPropertiesFromGameViewModel() {
        //viewLifeCycleOwner: Observer class is attached with a Lifecycle owner.
        //a viewLifeCycleOwner is generated when a view hierarchy is created and
        //exists until the view hierarchy gets destroyed.
        gameViewModel.livesLeft.observe(viewLifecycleOwner, Observer { newValueForLivesLeft ->
            //binding.textIDForLiveLeft.text =
                String.format(resources.getString(R.string.lives_left), newValueForLivesLeft)

        })

        gameViewModel.hint.observe(viewLifecycleOwner, Observer { hint ->
           // binding.hintTextId.text = String.format(resources.getString(R.string.hint_text), hint)
        })


        gameViewModel.incorrectGuesses.observe(
            viewLifecycleOwner,
            Observer { newAdditionToIncorrectChar ->
                /*binding.textIDForIncorrectGuess.text = String.format(
                    resources.getString(R.string.incorrect_guesses),
                    newAdditionToIncorrectChar
                )*/
            })

        gameViewModel.secretWordDisplay.observe(
            viewLifecycleOwner,
            Observer { secretWordDisplay ->
                //binding.resultTextId.text = secretWordDisplay
            })

        gameViewModel.gameOver.observe(viewLifecycleOwner, Observer { gameOver ->
            if (gameOver) {
                val actionWithMessageSafeArgs =
                    GameFragmentDirections.actionGameFragmentToResultFragment(gameViewModel.messageWinLoss())
                view?.findNavController()?.navigate((actionWithMessageSafeArgs))
            }

        })
    }

    //This is used when we use the xml layout file and data binding
    /*override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }*/


}

@Composable
fun SetContentGameFragment(gameViewModel: GameFragmentViewModel) {
    Text(text = "-------------")
    val guessedChar = rememberSaveable {
        mutableStateOf("")
    }
    val gameOver = gameViewModel.gameOver.observeAsState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SecretWordText(gameViewModel)
        Spacer(modifier = Modifier.padding(16.dp))
        TextForLivesLeft(gameViewModel)
        Spacer(modifier = Modifier.padding(16.dp))
        TextForIncorrectGuesses(gameViewModel)
        Spacer(modifier = Modifier.padding(16.dp))
        InputFieldForChar(charInput = guessedChar.value) {
            //this is lambda which is being passed for the TextField. 'it' is the string value that is being
            //picked by onValueChange property and will put in guessedChar.value.
            guessedChar.value = it
        }
        Spacer(modifier = Modifier.padding(16.dp))
        GuessButton {
            gameViewModel.makeGuess(guessedChar.value.uppercase())
            guessedChar.value = ""
        }
        FinishGameButton {
            gameViewModel.finishGame()
            //view.findNavController().navigate(R.id.action_gameFragment_to_resultFragment)
        }
        HintText(gameViewModel)
        Spacer(modifier = Modifier.padding(16.dp))
    }
}

@Composable
fun TextForIncorrectGuesses(gameViewModel: GameFragmentViewModel) {
    //observeAsState() is used for LiveData type properties.
    //Here incorrectGuesses is a property in GameFragmentViewModel whose type is LiveData.
    //we already know that composable functions are redrawn when the value for the states they depend on changes.
    //observeAsSate() helps us in that. If we call observeAsState() on the incorrectGuesses property of GameFragmentViewModel
    //then it makes an object of state whose values can be observed. Now if the value of the state changes then the composable function
    //that depends on this will get redrawn.
    val incorrectGuesses = gameViewModel.incorrectGuesses.observeAsState()
    Text(text = "Incorrect Guesses: ${incorrectGuesses.value}")
}

@Composable
fun SecretWordText(gameViewModel: GameFragmentViewModel) {
    val secretWordDisplay = gameViewModel.secretWordDisplay.observeAsState()
    Text(
        text = "${secretWordDisplay.value}",
        // modifier = Modifier.size(20.dp)
    )
}

@Composable
fun TextForLivesLeft(gameViewModel: GameFragmentViewModel) {
    val livesLeft = gameViewModel.livesLeft.observeAsState()
    Text(text = "Lives Left: ${livesLeft.value}")
}


@Composable
fun InputFieldForChar(charInput: String, modificationInChar: (String) -> Unit) {
    TextField(
        value = charInput,
        onValueChange = modificationInChar,
        label = { Text(text = "Guess a character") },
        // modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun GuessButton(guess: () -> Unit) {
    Button(onClick = guess) {
        Text(text = "Guess!")
    }
}

@Composable
fun FinishGameButton(finishAction: () -> Unit) {
    Button(onClick = finishAction) {
        Text(text = "Finish Game!")
    }
}

@Composable
fun HintText(gameViewModel: GameFragmentViewModel) {
    val hint = gameViewModel.hint.observeAsState()
    Text(text = "Hint : ${hint.value}")
}