package com.example.mycalculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.math.RoundingMode
import java.text.DecimalFormat

class CalculatorViewModel: ViewModel() {

    private var _result = MutableLiveData(0.0f)
    val result: LiveData<Float> = _result

    private var _calculate = MutableLiveData("")
    val calculate: LiveData<String> = _calculate

    fun modifyOperation(operation: String){
            _calculate.value = operation
    }

    fun subOperate(operation: String) {
        _calculate.value = operation
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
        //var _result = 0.0f
        var i = 0
        var multiplicationAux = 1.0f
        var multiplicationAux2 = 1.0f
        var divisionAux = 1.0f
        var divisionAux2 = 1.0f
        var auxGraL: Float
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
            _result.value = subsequencesOfOperations[0].toFloat()

        while (iterations != 0) {
            if (i == 0) {
                when (operationsSymbols[i]){
                    '+' -> _result.value = auxNeg2 + subsequencesOfOperations[1].toFloat()
                    '-' -> _result.value = auxNeg2 - subsequencesOfOperations[1].toFloat()
                    'x' -> {
                        multiplicationAux = auxNeg2 * subsequencesOfOperations[1].toFloat()
                        _result.value = multiplicationAux
                        multiplicationAux2 = multiplicationAux

                        divisionAux = multiplicationAux
                        divisionAux2 = multiplicationAux2
                    }
                    '/' -> {
                        divisionAux = auxNeg2 / subsequencesOfOperations[1].toFloat()
                        _result.value = divisionAux
                        divisionAux2 = divisionAux

                        multiplicationAux = divisionAux
                        multiplicationAux2 = divisionAux2
                    }
                }

            } else {
                if (operationsSymbols[i].compareTo('+') == 0) {
                    //_result.value += subsequencesOfOperations[i + 1].toFloat()
                    auxGraL = _result.value!!
                    auxGraL+= subsequencesOfOperations[i + 1].toFloat()
                    _result.value = auxGraL
                    multiplicationAux = 1.0f
                    multiplicationAux2 = 1.0f
                    divisionAux = 1.0f
                    divisionAux2 = 1.0f
                }

                if (operationsSymbols[i].compareTo('-') == 0) {
                    auxGraL = _result.value!!
                    auxGraL -= subsequencesOfOperations[i + 1].toFloat()
                    _result.value = auxGraL
                    //_result -= subsequencesOfOperations[i + 1].toFloat()
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
                            auxGraL = _result.value!!
                            auxGraL += multiplicationAux
                            _result.value = auxGraL
                            //_result += multiplicationAux
                            multiplicationAux2 = subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()
                            multiplicationAux = subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()

                            divisionAux = multiplicationAux
                            divisionAux2 = multiplicationAux2
                        }
                        '-' -> {
                            multiplicationAux =
                                subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()  - subsequencesOfOperations[i].toFloat()
                            auxGraL = _result.value!!
                            auxGraL -= multiplicationAux
                            _result.value = auxGraL
                            //_result -= multiplicationAux
                            multiplicationAux2 = -subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()
                            multiplicationAux = -subsequencesOfOperations[i].toFloat() * subsequencesOfOperations[i + 1].toFloat()

                            divisionAux = multiplicationAux
                            divisionAux2 = multiplicationAux2
                        }

                        '/' -> {
                            divisionAux *= subsequencesOfOperations[i + 1].toFloat()
                            auxGraL = _result.value!!
                            auxGraL = auxGraL + divisionAux - divisionAux2
                            _result.value = auxGraL
                            //_result = _result + divisionAux - divisionAux2
                            divisionAux2 = divisionAux


                            multiplicationAux = divisionAux
                            multiplicationAux2 = divisionAux2
                        }

                        'x' -> {
                            multiplicationAux *= subsequencesOfOperations[i + 1].toFloat()
                            auxGraL = _result.value!!
                            auxGraL = auxGraL + multiplicationAux - multiplicationAux2
                            _result.value = auxGraL
                            //_result = _result + multiplicationAux - multiplicationAux2
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
                            auxGraL = _result.value!!
                            auxGraL += divisionAux
                            _result.value = auxGraL
                            //_result += divisionAux
                            divisionAux2 = subsequencesOfOperations[i].toFloat() / subsequencesOfOperations[i + 1].toFloat()
                            divisionAux = subsequencesOfOperations[i].toFloat() / subsequencesOfOperations[i + 1].toFloat()

                            multiplicationAux = divisionAux
                            multiplicationAux2 = divisionAux2
                        }
                        '-' -> {
                            divisionAux =
                                subsequencesOfOperations[i].toFloat() / subsequencesOfOperations[i + 1].toFloat()  - subsequencesOfOperations[i].toFloat()
                            auxGraL = _result.value!!
                            auxGraL -= divisionAux
                            _result.value = auxGraL
                            //_result -= divisionAux
                            divisionAux2 = -subsequencesOfOperations[i].toFloat() / subsequencesOfOperations[i + 1].toFloat()
                            divisionAux = -subsequencesOfOperations[i].toFloat() / subsequencesOfOperations[i + 1].toFloat()

                            multiplicationAux = divisionAux
                            multiplicationAux2 = divisionAux2
                        }

                        '/' -> {
                            divisionAux /= subsequencesOfOperations[i + 1].toFloat()
                            auxGraL = _result.value!!
                            auxGraL = auxGraL + divisionAux - divisionAux2
                            _result.value = auxGraL
                            //_result = _result + divisionAux - divisionAux2
                            divisionAux2 = divisionAux


                            multiplicationAux = divisionAux
                            multiplicationAux2 = divisionAux2
                        }

                        'x' -> {
                            divisionAux /= subsequencesOfOperations[i + 1].toFloat()
                            auxGraL = _result.value!!
                            auxGraL = auxGraL + divisionAux - divisionAux2
                            _result.value = auxGraL
                            //_result = _result + divisionAux - divisionAux2
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
        //val roundOff = decimalFormat.format(_result)
        //binding.tvShowResult.setText(roundOff.toString())
    }
}