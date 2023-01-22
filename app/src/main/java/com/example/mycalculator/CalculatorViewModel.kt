package com.example.mycalculator

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CalculatorViewModel: ViewModel() {

    private var _result = MutableLiveData(0.0)
    val result: LiveData<Double> = _result

    private var _calculate = MutableLiveData("")
    val calculate: LiveData<String> = _calculate

    fun modifyOperation(falseOperation: String){
            _calculate.value = falseOperation
    }

    fun setEquals(): String{
        var change = _result.value.toString()
        _calculate.value = change
        return  change
    }

    fun subOperate(operation: String) {
        if(operation == ""){
            _result.value = 0.0
        } else {

            _calculate.value = operation
            val indices = mutableListOf<Int>()
            val operationsSymbols = mutableListOf<Char>()
            val subsequencesOfOperations = mutableListOf<String>()
            var numberOfOperations = 0
            var auxNumberOfOperations = 0
            var auxNeg = 0

            //return str.substring(0, str.length - n)

            for ((index, value) in operation.withIndex()) {
                //auxNeg permite saber si la operacion inicia con num negativo
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
    }

    private fun operate(subsequencesOfOperations: MutableList<String>, operationsSymbols: MutableList<Char>, auxNeg: Int) {
        var iterations = operationsSymbols.size
        //var _result = 0.0f
        var i = 0
        var multiplicationAux = 1.0
        var multiplicationAux2 = 1.0
        var divisionAux = 1.0
        var divisionAux2 = 1.0
        var auxGraL: Double
        val auxNeg2: Double
        val auxNeg3: Double

        if(auxNeg ==1){
            auxNeg3 = subsequencesOfOperations[0].toDouble()
            auxNeg2 = auxNeg3
        } else{
            auxNeg3 = subsequencesOfOperations[0].toDouble()
            auxNeg2 = auxNeg3
        }

        if(iterations == 0)
            _result.value = subsequencesOfOperations[0].toDouble()

        while (iterations != 0) {
            if (i == 0) {
                when (operationsSymbols[i]){
                    '+' -> _result.value = auxNeg2 + subsequencesOfOperations[1].toDouble()
                    '-' -> _result.value = auxNeg2 - subsequencesOfOperations[1].toDouble()
                    'x' -> {
                        multiplicationAux = auxNeg2 * subsequencesOfOperations[1].toDouble()
                        _result.value = multiplicationAux
                        multiplicationAux2 = multiplicationAux

                        divisionAux = multiplicationAux
                        divisionAux2 = multiplicationAux2
                    }
                    '÷' -> {
                        divisionAux = auxNeg2 / subsequencesOfOperations[1].toDouble()
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
                    auxGraL+= subsequencesOfOperations[i + 1].toDouble()
                    _result.value = auxGraL
                    multiplicationAux = 1.0
                    multiplicationAux2 = 1.0
                    divisionAux = 1.0
                    divisionAux2 = 1.0
                }

                if (operationsSymbols[i].compareTo('-') == 0) {

                    auxGraL = _result.value!!
                    auxGraL -= subsequencesOfOperations[i + 1].toDouble()
                    _result.value = auxGraL
                    //_result -= subsequencesOfOperations[i + 1].toFloat()
                    multiplicationAux = -1.0
                    multiplicationAux2 = -1.0
                    divisionAux = -1.0
                    divisionAux2 = -1.0
                }

                if (operationsSymbols[i].compareTo('x') == 0) {
                    when(operationsSymbols[i-1]){
                        '+' -> {
                            multiplicationAux =
                                subsequencesOfOperations[i].toDouble() * subsequencesOfOperations[i + 1].toDouble()  - subsequencesOfOperations[i].toDouble()
                            auxGraL = _result.value!!
                            auxGraL += multiplicationAux
                            _result.value = auxGraL
                            //_result += multiplicationAux
                            multiplicationAux2 = subsequencesOfOperations[i].toDouble() * subsequencesOfOperations[i + 1].toDouble()
                            multiplicationAux = subsequencesOfOperations[i].toDouble() * subsequencesOfOperations[i + 1].toDouble()

                            divisionAux = multiplicationAux
                            divisionAux2 = multiplicationAux2
                        }
                        '-' -> {
                            multiplicationAux =
                                subsequencesOfOperations[i].toDouble() * subsequencesOfOperations[i + 1].toDouble()  - subsequencesOfOperations[i].toDouble()
                            auxGraL = _result.value!!
                            auxGraL -= multiplicationAux
                            _result.value = auxGraL
                            //_result -= multiplicationAux
                            multiplicationAux2 = -subsequencesOfOperations[i].toDouble() * subsequencesOfOperations[i + 1].toDouble()
                            multiplicationAux = -subsequencesOfOperations[i].toDouble() * subsequencesOfOperations[i + 1].toDouble()

                            divisionAux = multiplicationAux
                            divisionAux2 = multiplicationAux2
                        }

                        '÷' -> {
                            divisionAux *= subsequencesOfOperations[i + 1].toDouble()
                            auxGraL = _result.value!!
                            auxGraL = auxGraL + divisionAux - divisionAux2
                            _result.value = auxGraL
                            //_result = _result + divisionAux - divisionAux2
                            divisionAux2 = divisionAux


                            multiplicationAux = divisionAux
                            multiplicationAux2 = divisionAux2
                        }

                        'x' -> {
                            multiplicationAux *= subsequencesOfOperations[i + 1].toDouble()
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

                if (operationsSymbols[i].compareTo('÷') == 0) {
                    when(operationsSymbols[i-1]){
                        '+' -> {
                            divisionAux =
                                subsequencesOfOperations[i].toDouble() / subsequencesOfOperations[i + 1].toDouble()  - subsequencesOfOperations[i].toDouble()
                            auxGraL = _result.value!!
                            auxGraL += divisionAux
                            _result.value = auxGraL
                            //_result += divisionAux
                            divisionAux2 = subsequencesOfOperations[i].toDouble() / subsequencesOfOperations[i + 1].toDouble()
                            divisionAux = subsequencesOfOperations[i].toDouble() / subsequencesOfOperations[i + 1].toDouble()

                            multiplicationAux = divisionAux
                            multiplicationAux2 = divisionAux2
                        }
                        '-' -> {
                            divisionAux =
                                subsequencesOfOperations[i].toDouble() / subsequencesOfOperations[i + 1].toDouble()  - subsequencesOfOperations[i].toDouble()
                            auxGraL = _result.value!!
                            auxGraL -= divisionAux
                            _result.value = auxGraL
                            //_result -= divisionAux
                            divisionAux2 = -subsequencesOfOperations[i].toDouble() / subsequencesOfOperations[i + 1].toDouble()
                            divisionAux = -subsequencesOfOperations[i].toDouble() / subsequencesOfOperations[i + 1].toDouble()

                            multiplicationAux = divisionAux
                            multiplicationAux2 = divisionAux2
                        }

                        '÷' -> {
                            divisionAux /= subsequencesOfOperations[i + 1].toDouble()
                            auxGraL = _result.value!!
                            auxGraL = auxGraL + divisionAux - divisionAux2
                            _result.value = auxGraL
                            //_result = _result + divisionAux - divisionAux2
                            divisionAux2 = divisionAux


                            multiplicationAux = divisionAux
                            multiplicationAux2 = divisionAux2
                        }

                        'x' -> {
                            divisionAux /= subsequencesOfOperations[i + 1].toDouble()
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
    }
}
