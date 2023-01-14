package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import java.math.RoundingMode
import java.text.DecimalFormat
import com.example.mycalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel: CalculatorViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //showOperation()
        initObservers()
        receiveOperation()
    }

    private fun initObservers() {
        viewModel.result.observe(this){ result->
            binding.tvShowResult.setText(result.toString())
        }

        viewModel.calculate.observe(this){ operation ->
            binding.editTextShowOperation.setText(operation)
        }
    }

    private fun subOperate(operation: String) {
        val indices = mutableListOf<Int>()
        val operationsSymbols = mutableListOf<Char>()
        val subsequencesOfOperations = mutableListOf<String>()
        var numberOfOperations = 0
        var auxNumberOfOperations = 0
        var auxNeg = 0


        for ((index, value) in operation.withIndex()) {
            if(index == 0 && value == '-'){
                    auxNeg = 1
            }

            else{
                if (!value.isDigit() && value != '.') {
                    numberOfOperations++
                    auxNumberOfOperations++
                    indices.add(index)
                    operationsSymbols.add(value)
                }
            }
        }

        if(numberOfOperations == 0 && operation.isNotEmpty()){
            subsequencesOfOperations.add(operation)
        }


        if (numberOfOperations != 0) {
            subsequencesOfOperations.add(operation.subSequence(0, indices[0]).toString())
            numberOfOperations--
        }

        var i = 0
        while (numberOfOperations > 0) {
            subsequencesOfOperations.add(
                operation.subSequence(indices[i] + 1, indices[i + 1]).toString()
            )
            i++
            numberOfOperations--
        }

        if (operation.isNotEmpty() && indices.isNotEmpty()) {
            subsequencesOfOperations.add(operation.subSequence(indices[i] + 1, operation.lastIndex + 1).toString())
        }

        if (subsequencesOfOperations.isNotEmpty()) {
            subsequencesOfOperations.forEach {
                if (it.compareTo("") == 0) {
                    subsequencesOfOperations.removeAt(subsequencesOfOperations.size-1)
                }
            }
        }

        operate (subsequencesOfOperations, operationsSymbols, auxNeg)
    }

    private fun operate(subsequencesOfOperations: MutableList<String>, operationsSymbols: MutableList<Char>, auxNeg: Int) {
        var iterations = operationsSymbols.size
        var result = 0.0f
        var i = 0
        var multiplicationAux = 1.0f
        var multiplicationAux2 = 1.0f
        var divisionAux = 1.0f
        var divisionAux2 = 1.0f
        var auxNeg2: Float
        var auxNeg3: Float

        if(auxNeg ==1){
            auxNeg3 = subsequencesOfOperations[0].toFloat()
            auxNeg2 = auxNeg3
        } else{
            auxNeg3 = subsequencesOfOperations[0].toFloat()
            auxNeg2 = auxNeg3
        }

        if(iterations == 0)
            result = subsequencesOfOperations[0].toFloat()

        while (iterations != 0) {
            if (i == 0) {
                when (operationsSymbols[i]){
                    '+' -> result = auxNeg2 + subsequencesOfOperations[1].toFloat()
                    '-' -> result = auxNeg2 - subsequencesOfOperations[1].toFloat()
                    'x' -> {
                        multiplicationAux = auxNeg2 * subsequencesOfOperations[1].toFloat()
                        result = multiplicationAux
                        multiplicationAux2 = multiplicationAux

                        divisionAux = multiplicationAux
                        divisionAux2 = multiplicationAux2
                    }
                    '/' -> {
                        divisionAux = auxNeg2 / subsequencesOfOperations[1].toFloat()
                        result = divisionAux
                        divisionAux2 = divisionAux

                        multiplicationAux = divisionAux
                        multiplicationAux2 = divisionAux2
                    }
                }

            } else {
                if (operationsSymbols[i].compareTo('+') == 0) {
                    result += subsequencesOfOperations[i + 1].toFloat()
                    multiplicationAux = 1.0f
                    multiplicationAux2 = 1.0f
                    divisionAux = 1.0f
                    divisionAux2 = 1.0f
                }

                if (operationsSymbols[i].compareTo('-') == 0) {
                    result -= subsequencesOfOperations[i + 1].toFloat()
                    multiplicationAux = -1.0f
                    multiplicationAux2 = -1.0f
                    divisionAux = -1.0f
                    divisionAux2 = -1.0f
                }

                if (operationsSymbols[i].compareTo('x') == 0) {
                        when(operationsSymbols[i-1]){
                            '+' -> {
                                multiplicationAux =
                                    subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()  - subsequencesOfOperations[i].toFloat()
                                result += multiplicationAux
                                multiplicationAux2 = subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()
                                multiplicationAux = subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()

                                divisionAux = multiplicationAux
                                divisionAux2 = multiplicationAux2
                            }
                            '-' -> {
                                multiplicationAux =
                                    subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()  - subsequencesOfOperations[i].toFloat()
                                result -= multiplicationAux
                                multiplicationAux2 = -subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()
                                multiplicationAux = -subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()

                                divisionAux = multiplicationAux
                                divisionAux2 = multiplicationAux2
                            }

                            '/' -> {
                                divisionAux *= subsequencesOfOperations[i + 1].toFloat()
                                result = result + divisionAux - divisionAux2
                                divisionAux2 = divisionAux


                                multiplicationAux = divisionAux
                                multiplicationAux2 = divisionAux2
                            }

                            'x' -> {
                                multiplicationAux *= subsequencesOfOperations[i + 1].toFloat()
                                result = result + multiplicationAux - multiplicationAux2
                                multiplicationAux2 = multiplicationAux


                                divisionAux = multiplicationAux
                                divisionAux2 = multiplicationAux2
                            }
                        }
                }

                if (operationsSymbols[i].compareTo('/') == 0) {
                    when(operationsSymbols[i-1]){
                        '+' -> {
                            divisionAux =
                                subsequencesOfOperations[i].toFloat() / subsequencesOfOperations[i + 1].toFloat()  - subsequencesOfOperations[i].toFloat()
                            result += divisionAux
                            divisionAux2 = subsequencesOfOperations[i].toFloat() / subsequencesOfOperations[i + 1].toFloat()
                            divisionAux = subsequencesOfOperations[i].toFloat() / subsequencesOfOperations[i + 1].toFloat()

                            multiplicationAux = divisionAux
                            multiplicationAux2 = divisionAux2
                        }
                        '-' -> {
                            divisionAux =
                                subsequencesOfOperations[i].toFloat() / subsequencesOfOperations[i + 1].toFloat()  - subsequencesOfOperations[i].toFloat()
                            result -= divisionAux
                            divisionAux2 = -subsequencesOfOperations[i].toFloat() / subsequencesOfOperations[i + 1].toFloat()
                            divisionAux = -subsequencesOfOperations[i].toFloat() / subsequencesOfOperations[i + 1].toFloat()

                            multiplicationAux = divisionAux
                            multiplicationAux2 = divisionAux2
                        }

                        '/' -> {
                            divisionAux /= subsequencesOfOperations[i + 1].toFloat()
                            result = result + divisionAux - divisionAux2
                            divisionAux2 = divisionAux


                            multiplicationAux = divisionAux
                            multiplicationAux2 = divisionAux2
                        }

                        'x' -> {
                            divisionAux /= subsequencesOfOperations[i + 1].toFloat()
                            result = result + divisionAux - divisionAux2
                            divisionAux2 = divisionAux


                            multiplicationAux = divisionAux
                            multiplicationAux2 = divisionAux2
                        }
                    }
                }
            }

            i++
            iterations--
        }


        val decimalFormat = DecimalFormat("#,###.####")
        decimalFormat.roundingMode = RoundingMode.DOWN
        val roundOff = decimalFormat.format(result)
        binding.tvShowResult.setText(roundOff.toString())
    }

    private fun showOperation() {
        var operation = ""
        with(binding) {
            editTextShowOperation.startNestedScroll(2)
            btn0.setOnClickListener {
                operation += "0"
                editTextShowOperation.setText(operation)
            }

            btn1.setOnClickListener {
                operation += "1"
                editTextShowOperation.setText(operation)
            }

            btn2.setOnClickListener {
                operation += "2"
                editTextShowOperation.setText(operation)
            }

            btn3.setOnClickListener {
                operation += "3"
                editTextShowOperation.setText(operation)
            }

            btn4.setOnClickListener {
                operation += "4"
                editTextShowOperation.setText(operation)
            }

            btn5.setOnClickListener {
                operation += "5"
                editTextShowOperation.setText(operation)
            }

            btn6.setOnClickListener {
                operation += "6"
                editTextShowOperation.setText(operation)
            }

            btn7.setOnClickListener {
                operation += "7"
                editTextShowOperation.setText(operation)
            }

            btn8.setOnClickListener {
                operation += "8"
                editTextShowOperation.setText(operation)
            }

            btn9.setOnClickListener {
                operation += "9"
                editTextShowOperation.setText(operation)
            }

            btnPlus.setOnClickListener {
                operation += "+"
                editTextShowOperation.setText(operation)
            }


            btnMultiplication.setOnClickListener {
                operation += "x"
                editTextShowOperation.setText(operation)
            }

            btnMinus.setOnClickListener {
                operation += "-"
                editTextShowOperation.setText(operation)
            }

            btnDivision.setOnClickListener {
                operation += "/"
                editTextShowOperation.setText(operation)
            }

            btnPoint.setOnClickListener {
                operation += "."
                editTextShowOperation.setText(operation)
            }

            btnPi.setOnClickListener {
                editTextShowOperation.setText(operation + "π")
                operation += "3.141592653589793"
            }

            btnClear.setOnClickListener {
                editTextShowOperation.setText("")
                tvShowResult.setText("")
                operation = ""
            }

            btnErase.setOnClickListener {
                val auxOperation: String
                if (operation.isNotEmpty()) {
                    auxOperation = operation.subSequence(0, operation.lastIndex).toString()
                    operation = auxOperation
                    editTextShowOperation.setText(operation)
                }
            }

            btnErase.setOnLongClickListener {
                editTextShowOperation.setText("")
                tvShowResult.setText("")
                operation = ""
                return@setOnLongClickListener true
            }

            btnEqual.setOnClickListener {
                subOperate(operation)
            }
        }
    }

    private fun receiveOperation() {
        var operation = ""
        with(binding) {

            btn0.setOnClickListener { operation += "0"
                viewModel.modifyOperation(operation)}

            btn1.setOnClickListener { operation += "1"
                viewModel.modifyOperation(operation)}

            btn2.setOnClickListener { operation += "2"
                viewModel.modifyOperation(operation)}

            btn3.setOnClickListener { operation += "3"
                viewModel.modifyOperation(operation)}

            btn4.setOnClickListener { operation += "4"
                viewModel.modifyOperation(operation)}

            btn5.setOnClickListener { operation += "5"
                viewModel.modifyOperation(operation)}

            btn6.setOnClickListener { operation += "6"
                viewModel.modifyOperation(operation)}

            btn7.setOnClickListener { operation += "7"
                viewModel.modifyOperation(operation)}

            btn8.setOnClickListener { operation += "8"
                viewModel.modifyOperation(operation)}

            btn9.setOnClickListener { operation += "9"
                viewModel.modifyOperation(operation)}

            btnPlus.setOnClickListener { operation += "+"
                viewModel.modifyOperation(operation)}

            btnMinus.setOnClickListener { operation += "-"
                viewModel.modifyOperation(operation)}

            btnMultiplication.setOnClickListener { operation += "x"
                viewModel.modifyOperation(operation)}

            btnDivision.setOnClickListener { operation += "/"
                viewModel.modifyOperation(operation)}

            btnPoint.setOnClickListener { operation += "."
                viewModel.modifyOperation(operation)}

            btnPi.setOnClickListener { operation += "3.141592653589793"}

            btnClear.setOnClickListener {
                editTextShowOperation.setText("")
                tvShowResult.setText("")
                operation = ""
            }

            btnErase.setOnClickListener {
                val auxOperation: String
                if (operation.isNotEmpty()) {
                    auxOperation = operation.subSequence(0, operation.lastIndex).toString()
                    operation = auxOperation
                    //editTextShowOperation.setText(operation)
                }
                viewModel.modifyOperation(operation)
            }

            btnErase.setOnLongClickListener {
                editTextShowOperation.setText("")
                tvShowResult.setText("")
                operation = ""
                return@setOnLongClickListener true
            }

            btnEqual.setOnClickListener {
                viewModel.subOperate(operation)
                //subOperate(operation)
            }
        }
    }
}
