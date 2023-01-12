package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.mycalculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        showOperation()
    }

    private fun subOperate(operation: String) {
        val indices = mutableListOf<Int>()
        val operationsSymbols = mutableListOf<Char>()
        val subsequencesOfOperations = mutableListOf<String>()
        var numberOfOperations = 0
        var auxNumberOfOperations = 0

        for ((index, value) in operation.withIndex()) {
            if (!value.isDigit()) {
                numberOfOperations++
                auxNumberOfOperations++
                indices.add(index)
                operationsSymbols.add(value)
            }
        }

        if(numberOfOperations == 0 && operation.isNotEmpty())
            subsequencesOfOperations.add(operation)

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
        operate (subsequencesOfOperations, operationsSymbols)
    }

    private fun operate(subsequencesOfOperations: MutableList<String>, operationsSymbols: MutableList<Char>) {
        var iterations = operationsSymbols.size
        var result = 0.0f
        var i = 0
        var multiplicationAux = 1.0f
        var multiplicationAux2 = 1.0f

        while (iterations != 0) {
            if (i == 0) {
                when (operationsSymbols[i]){
                    '+' -> result = subsequencesOfOperations[i].toFloat() + subsequencesOfOperations[1].toFloat()
                    '-' -> result = subsequencesOfOperations[i].toFloat() - subsequencesOfOperations[1].toFloat()
                    'x' -> {
                        multiplicationAux = subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[1].toFloat()
                        result = multiplicationAux
                        multiplicationAux2 = multiplicationAux
                    }
                    '/' -> {
                        result = subsequencesOfOperations[i].toFloat() / subsequencesOfOperations[1].toFloat()
                    }
                }

            } else {
                if (operationsSymbols[i].compareTo('+') == 0) {
                    result += subsequencesOfOperations[i + 1].toFloat()
                    multiplicationAux = 1.0f
                    multiplicationAux2 = 1.0f
                }

                if (operationsSymbols[i].compareTo('-') == 0) {
                    result -= subsequencesOfOperations[i + 1].toFloat()
                    multiplicationAux = -1.0f
                    multiplicationAux2 = -1.0f
                }

                if (operationsSymbols[i].compareTo('x') == 0) {
                        when(operationsSymbols[i-1]){
                            '+' -> {
                                multiplicationAux =
                                    subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()  - subsequencesOfOperations[i].toFloat()
                                result += multiplicationAux
                                multiplicationAux2 = subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()
                                multiplicationAux = subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()
                            }
                            '-' -> {
                                multiplicationAux =
                                    subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()  - subsequencesOfOperations[i].toFloat()
                                result -= multiplicationAux
                                multiplicationAux2 = subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()
                                multiplicationAux = -subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()
                            }

                            '/' -> {}

                            'x' -> {
                                multiplicationAux *= subsequencesOfOperations[i + 1].toFloat()
                                result = result + multiplicationAux - multiplicationAux2
                                multiplicationAux2 = multiplicationAux
                            }
                        }
                }
            }

            i++
            iterations--
        }
        Toast.makeText(this, "aux1 = $multiplicationAux", Toast.LENGTH_SHORT).show()
        Toast.makeText(this, "aux2 = $multiplicationAux2", Toast.LENGTH_SHORT).show()
        binding.tvShowResult.text = result.toString()
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

            btnClear.setOnClickListener {
                editTextShowOperation.setText("")
                tvShowResult.text = ""
                operation = ""
            }

            btnErase.setOnClickListener {
                var auxOperation: String
                if (operation.isNotEmpty()) {
                    auxOperation = operation.subSequence(0, operation.lastIndex).toString()
                    operation = auxOperation
                    editTextShowOperation.setText(operation)
                }
            }

            btnEqual.setOnClickListener {
                subOperate(operation)
            }
        }
    }
}