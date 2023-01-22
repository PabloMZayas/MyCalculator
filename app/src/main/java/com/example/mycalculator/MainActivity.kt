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
    private  var falseOperation = ""
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
            var operationFormat = operation.toString()
            val decimalFormat = DecimalFormat("#,###.####")
            //val roundOff = decimalFormat.format(operationFormat)
            if(operation.length>11)
                binding.editTextShowOperation.textSize = 30f
            binding.editTextShowOperation.setText(operationFormat)
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

            btnMinus.setOnClickListener { callViewModelWithSymbol('-')}

            btnMultiplication.setOnClickListener {callViewModelWithSymbol('x')}

            btnDivision.setOnClickListener {callViewModelWithSymbol('÷')}


            btnPoint.setOnClickListener {
                operation += "."
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
                val auxOperation2: String
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

                if (falseOperation.isNotEmpty()) {
                    auxOperation2 = falseOperation.subSequence(0, falseOperation.lastIndex).toString()
                    falseOperation = auxOperation2
                    val lastIndex = falseOperation.lastIndex
                    for ((index, value) in falseOperation.withIndex()) {
                        if (index == lastIndex && (!value.isDigit() || value == '.')){
                            falseOperation = falseOperation.substring(0, falseOperation.length-1)
                        }
                    }
                }
                viewModel.modifyOperation(operation)
                viewModel.subOperate(operation)
            }

            btnErase.setOnLongClickListener {
                checkLast = 0
                operation = ""
                viewModel.modifyOperation(operation)
                viewModel.subOperate(operation)
                return@setOnLongClickListener true
            }

            btnEqual.setOnClickListener {
                operation = viewModel.setEquals()
                viewModel.subOperate(operation)
                viewModel.modifyOperation(operation)
            }

            editTextShowOperation.setOnClickListener {
                val position = editTextShowOperation.selectionEnd
                Toast.makeText(this@MainActivity, "position: $position", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun callViewModelWithSymbol(c: Char) {
        //checkLast = 0
        val lastIndex = operation.lastIndex
        for ((index, value) in operation.withIndex()) {
            if (index == lastIndex && (!value.isDigit() || value == '.')){
                //falseOperation = falseOperation.substring(0, falseOperation.length-1)
                operation = operation.substring(0, operation.length-1)
            }
        }
        operation += c
        viewModel.modifyOperation(operation)
    }

    private fun callViewModel(c: Char) {
        //checkLast = 0
        operation+=c
        viewModel.subOperate(operation)
        viewModel.modifyOperation(operation)
    }
}

