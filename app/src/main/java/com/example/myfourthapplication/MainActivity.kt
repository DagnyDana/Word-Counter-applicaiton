package com.example.myfourthapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorApp()
        }
    }
}

@Composable
fun CalculatorApp() {
    var displayText by remember { mutableStateOf("0") }
    var currentOperation by remember { mutableStateOf<String?>(null) }
    var operand1 by remember { mutableStateOf(0.0) }
    var operand2 by remember { mutableStateOf(0.0) }

    fun appendNumber(number: String) {
        displayText = if (displayText == "0") number else displayText + number
    }

    fun performOperation(operation: String) {
        operand1 = displayText.toDoubleOrNull() ?: 0.0
        currentOperation = operation
        displayText = "0"
    }

    fun calculateResult() {
        operand2 = displayText.toDoubleOrNull() ?: 0.0
        displayText = when (currentOperation) {
            "+" -> (operand1 + operand2).toString()
            "-" -> (operand1 - operand2).toString()
            "*" -> (operand1 * operand2).toString()
            "/" -> if (operand2 != 0.0) (operand1 / operand2).toString() else "Error"
            "√" -> if (operand1 >= 0) sqrt(operand1).toString() else "Error"
            else -> displayText
        }
        currentOperation = null
    }

    fun clearDisplay() {
        displayText = "0"
        operand1 = 0.0
        operand2 = 0.0
        currentOperation = null
    }

    fun backspace() {
        displayText = if (displayText.length > 1) {
            displayText.dropLast(1)
        } else {
            "0"
        }
    }

    fun toggleSign() {
        displayText = (displayText.toDoubleOrNull()?.times(-1)).toString()
    }

    CalculatorScreen(
        displayText = displayText,
        onNumberClick = { appendNumber(it) },
        onOperationClick = { performOperation(it) },
        onEqualsClick = { calculateResult() },
        onClearClick = { clearDisplay() },
        onBackClick = { backspace() },
        onSignChangeClick = { toggleSign() }
    )
}

@Composable
fun CalculatorScreen(
    displayText: String,
    onNumberClick: (String) -> Unit,
    onOperationClick: (String) -> Unit,
    onEqualsClick: () -> Unit,
    onClearClick: () -> Unit,
    onBackClick: () -> Unit,
    onSignChangeClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(Color(0xFF101820)),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.Black)
                .padding(24.dp),
            contentAlignment = Alignment.CenterEnd
        ) {
            Text(
                text = displayText,
                fontSize = 48.sp,
                color = Color.White,
                fontWeight = FontWeight.Bold
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            CalculatorButton(text = "C", color = Color.Red, onClick = onClearClick)
            CalculatorButton(text = "←", color = Color.Gray, onClick = onBackClick)
            CalculatorButton(text = "±", color = Color.Gray, onClick = onSignChangeClick)
            CalculatorButton(text = "√", color = Color.Yellow, onClick = { onOperationClick("√") })
        }

        val buttonRows = listOf(
            listOf("7", "8", "9", "/"),
            listOf("4", "5", "6", "*"),
            listOf("1", "2", "3", "-"),
            listOf("0", ".", "=", "+")
        )

        buttonRows.forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                row.forEach { label ->
                    val color = when (label) {
                        "+", "-", "*", "/" -> Color.Yellow
                        "=" -> Color.Green
                        else -> Color.DarkGray
                    }
                    CalculatorButton(
                        text = label,
                        color = color,
                        onClick = {
                            when (label) {
                                "=" -> onEqualsClick()
                                "+", "-", "*", "/" -> onOperationClick(label)
                                "." -> onNumberClick(".")
                                else -> onNumberClick(label)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(text: String, color: Color, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        shape = CircleShape,
        colors = ButtonDefaults.buttonColors(backgroundColor = color),
        modifier = Modifier
            .size(80.dp)
            .padding(4.dp)
    ) {
        Text(text = text, fontSize = 24.sp, color = Color.White)
    }
}
