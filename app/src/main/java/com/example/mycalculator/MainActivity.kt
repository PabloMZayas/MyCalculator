package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.example.mycalculator.databinding.ActivityMainBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
    private var operation = ""
    var checkLast = 0
    private val viewModel: CalculatorViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initObservers()
        receiveOperation()
    }

    private fun initObservers() {
        viewModel.result.observe(this){ result->
            val decimalFormat = DecimalFormat("#,###.####")
            decimalFormat.roundingMode = RoundingMode.DOWN
            val roundOff = decimalFormat.format(result)
            binding.tvShowResult.setText(roundOff.toString())
        }

        viewModel.calculate.observe(this){ operation ->
            if(operation.length>11)
                binding.editTextShowOperation.textSize = 30f
            binding.editTextShowOperation.setText(operation)
        }
    }

    private fun receiveOperation() {
        with(binding) {

            btn0.setOnClickListener { callViewModel('0')}

            btn1.setOnClickListener { callViewModel('1')}

            btn2.setOnClickListener { callViewModel('2')}

            btn3.setOnClickListener { callViewModel('3')}

            btn4.setOnClickListener { callViewModel('4')}

            btn5.setOnClickListener { callViewModel('5')}

            btn6.setOnClickListener { callViewModel('6')}

            btn7.setOnClickListener { callViewModel('7')}

            btn8.setOnClickListener{ callViewModel('8')}

            btn9.setOnClickListener { callViewModel('9')}

            btnPlus.setOnClickListener { callViewModelWithSymbol('+')}

            btnMinus.setOnClickListener {
                val lastIndex = operation.lastIndex
                for ((index, value) in operation.withIndex()) {
                    if (index == lastIndex && (!value.isDigit() || value == '.') && value!= '÷' && value != 'x'){
                        operation = operation.substring(0, operation.length-1)
                        checkLast = 1
                        Toast.makeText(this@MainActivity, "checkLast = $checkLast", Toast.LENGTH_SHORT).show()
                    }
                }
                operation += "-"
                viewModel.modifyOperation(operation)}

            btnMultiplication.setOnClickListener {callViewModelWithSymbol('x')}

            btnDivision.setOnClickListener {callViewModelWithSymbol('÷')}


            btnPoint.setOnClickListener { operation += "."
                viewModel.modifyOperation(operation)}

            btnPercentage.setOnClickListener {
                operation+='%'
                viewModel.modifyOperation(operation)}

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
                    val lastIndex = operation.lastIndex
                    for ((index, value) in operation.withIndex()) {
                        if (index == lastIndex && (!value.isDigit() || value == '.')){
                            operation = operation.substring(0, operation.length-1)
                        }
                    }
                }
                viewModel.modifyOperation(operation)
                viewModel.subOperate(operation, checkLast)
            }

            btnErase.setOnLongClickListener {
                operation = ""
                viewModel.modifyOperation(operation)
                viewModel.subOperate(operation, checkLast)
                return@setOnLongClickListener true
            }

            btnEqual.setOnClickListener {
                operation = viewModel.setEquals()
                viewModel.subOperate(operation, checkLast)
            }

            editTextShowOperation.setOnClickListener {
                val position = editTextShowOperation.selectionEnd
                Toast.makeText(this@MainActivity, "position: $position", Toast.LENGTH_SHORT).show()
            }

            /*btnShrek.setOnClickListener {
                val snack = Snackbar.make(it,"Finlandia!",Snackbar.LENGTH_LONG)
                snack.show()
            }*/
        }
    }

    private fun callViewModelWithSymbol(c: Char) {
        val lastIndex = operation.lastIndex
        for ((index, value) in operation.withIndex()) {
            if (index == lastIndex && (!value.isDigit() || value == '.')){
                operation = operation.substring(0, operation.length-1)
            }
        }
        operation += c
        viewModel.modifyOperation(operation)
    }

    private fun callViewModel(c: Char) {
        operation+=c
        viewModel.subOperate(operation, checkLast)
        viewModel.modifyOperation(operation)
    }

}

