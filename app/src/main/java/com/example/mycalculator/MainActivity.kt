package com.example.mycalculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.Toast
import androidx.activity.viewModels
import com.example.mycalculator.databinding.ActivityMainBinding
import java.math.RoundingMode
import java.text.DecimalFormat

class MainActivity : AppCompatActivity() {
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
        var operation = ""
        with(binding) {

            btn0.setOnClickListener { operation += "0"
                viewModel.subOperate(operation)
                viewModel.modifyOperation(operation)}

            btn1.setOnClickListener { operation += "1"
                viewModel.subOperate(operation)
                viewModel.modifyOperation(operation)}

            btn2.setOnClickListener { operation += "2"
                viewModel.subOperate(operation)
                viewModel.modifyOperation(operation)}

            btn3.setOnClickListener { operation += "3"
                viewModel.subOperate(operation)
                viewModel.modifyOperation(operation)}

            btn4.setOnClickListener { operation += "4"
                viewModel.subOperate(operation)
                viewModel.modifyOperation(operation)}

            btn5.setOnClickListener { operation += "5"
                viewModel.subOperate(operation)
                viewModel.modifyOperation(operation)}

            btn6.setOnClickListener { operation += "6"
                viewModel.subOperate(operation)
                viewModel.modifyOperation(operation)}

            btn7.setOnClickListener { operation += "7"
                viewModel.subOperate(operation)
                viewModel.modifyOperation(operation)}

            btn8.setOnClickListener{ operation += "8"
                viewModel.subOperate(operation)
                viewModel.modifyOperation(operation)}

            btn9.setOnClickListener { operation += "9"
                viewModel.subOperate(operation)
                viewModel.modifyOperation(operation)}

            btnPlus.setOnClickListener { operation += "+"
                viewModel.modifyOperation(operation)}

            btnMinus.setOnClickListener { operation += "-"
                viewModel.modifyOperation(operation)}

            btnMultiplication.setOnClickListener { operation += "x"
                viewModel.modifyOperation(operation)}

            btnDivision.setOnClickListener { operation += "÷"
                viewModel.modifyOperation(operation)}

            btnPoint.setOnClickListener { operation += "."
                viewModel.modifyOperation(operation)}

            btnPercentage.setOnClickListener {

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
                    val lastIndex = operation.lastIndex
                    for ((index, value) in operation.withIndex()) {
                        if (index == lastIndex && (!value.isDigit() || value == '.')){
                            operation = operation.substring(0, operation.length-1)
                        }
                    }
                }
                viewModel.modifyOperation(operation)
                viewModel.subOperate(operation)
            }

            btnErase.setOnLongClickListener {
                operation = ""
                viewModel.modifyOperation(operation)
                viewModel.subOperate(operation)
                return@setOnLongClickListener true
            }

            btnEqual.setOnClickListener {

                operation = viewModel.setEquals()
                viewModel.subOperate(operation)
            }

            tvShowResult.inputType = InputType.TYPE_NULL

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
}
