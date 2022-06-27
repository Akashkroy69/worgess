package com.example.worgess

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.worgess.databinding.FragmentResultBinding


class ResultFragment : Fragment() {
    private var _binding: FragmentResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var resultFragmentViewModel: ResultFragmentViewModel
    private lateinit var viewModelFactoryResultFragment: ViewModelFactoryResultFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        //what .apply{} is doing here?
        // It is actually applying he code to the inflated layout file.
        /* _binding = FragmentResultBinding.inflate(inflater, container, false).apply {
             composeView.setContent {
                 //  Text(text = "Hey, I am jetpack compose.")
                 MaterialTheme {
                     Surface {
                         view?.let { SetContentForResultFragment(it, resultFragmentViewModel) }
                     }
                 }
             }
         }*/
        val resultTextMessage =
            ResultFragmentArgs.fromBundle(requireArguments()).resultStringMessage

        viewModelFactoryResultFragment = ViewModelFactoryResultFragment(resultTextMessage)
        resultFragmentViewModel = ViewModelProvider(
            this,
            viewModelFactoryResultFragment
        ).get(ResultFragmentViewModel::class.java)
        //val view = _binding!!.root


        //What the following code is doing?
        //So in case of fragment, onCreateView() has to return a view that is needed by the FragmentContainerView and the MainActivity.
        //we have learnt about them in the fragment chapter. Here when we are using composables for UI components instead of a layout file.
        //there is no concept like inflation in jetpack compose. But as we are integrating composables with layout format. so we will have to return
        //a view any how. The ComposeView does exactly the same thing.
        val composeView = ComposeView(requireContext()).apply {
            this.setContent {
                MaterialTheme {
                    Surface {
                        SetContentForResultFragment(
                            view = this,
                            viewModel = resultFragmentViewModel
                        )

                    }
                }
            }
        }


        // binding.resultViewModel = resultFragmentViewModel
        //binding.resultScreenTextId.text = resultFragmentViewModel.finalResultDisplay

        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_result, container, false)

        /*  binding.newGameButton.setOnClickListener {
              view.findNavController().navigate(R.id.action_resultFragment_to_gameHome)
          }*/

        return composeView
    }
}

@Composable
fun SetContentForResultFragment(view: View, viewModel: ResultFragmentViewModel) {
    //Text(text = "Hey, I am Jetpack compose")
    Column(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ResultText(finalResultText = viewModel.finalResultDisplay)
        NewGameButton {
            view.findNavController().navigate(R.id.action_resultFragment_to_gameHome)
        }
    }

}

@Composable
fun NewGameButton(clicked: () -> Unit) {
    Button(onClick = clicked) {
        Text(text = "Start a new game!")
    }
}

@Composable
fun ResultText(finalResultText: String) {
    Text(
        text = finalResultText,
        textAlign = TextAlign.Center,
        fontSize = 28.sp
    )
}
