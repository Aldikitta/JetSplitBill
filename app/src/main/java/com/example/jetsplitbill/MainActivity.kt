package com.example.jetsplitbill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetsplitbill.components.InputField
import com.example.jetsplitbill.ui.theme.JetSplitBillTheme
import com.example.jetsplitbill.util.calculateTotalPerPerson
import com.example.jetsplitbill.util.calculateTotalTip
import com.example.jetsplitbill.widgets.FillIconButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetSplitBillTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        MainContent()
                    }
                }
            }
//            MyApp {
//                Column {
//                    BillPerson()
//                    MainContent()
//                }
//            }
        }
    }
}

//@Composable
//fun MyApp(content: @Composable () -> Unit) {
//    JetSplitBillTheme {
//        // A surface container using the 'background' color from the theme
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//            color = MaterialTheme.colorScheme.background
//        ) {
//            content()
//        }
//    }
//}

//@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BillPerson(totalPerPerson: Double = 133.0) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(150.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val total = "%.2f".format(totalPerPerson)
            Text(
                text = "Total Per Person",
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
            Text(
                text = "IDR $total",
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.headlineLarge
            )
        }
    }
}

@Composable
fun MainContent() {
    val tipAmountState = remember {
        mutableStateOf(0.0)
    }
    val totalPerPersonState = remember {
        mutableStateOf(0.0)
    }
    val splitByState = remember {
        mutableStateOf(1)
    }
//    val range = IntRange(start = 1, endInclusive = 100)
    BillForm(
        splitByState = splitByState,
        totalPerPersonState = totalPerPersonState,
        tipAmountState = tipAmountState,

    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun BillForm(
    modifier: Modifier = Modifier,
    range: IntRange = 1..100,
    splitByState: MutableState<Int>,
    tipAmountState: MutableState<Double>,
    totalPerPersonState: MutableState<Double>,
    onValChange: (String) -> Unit = {}
) {
    val totalBillState = remember {
        mutableStateOf("")
    }
    val validState = remember(totalBillState.value) {
        totalBillState.value.trim().isNotEmpty()
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val sliderPositionState = remember {
        mutableStateOf(0f)
    }

    val tipPercentage = (sliderPositionState.value * 100).toInt()


    BillPerson(totalPerPerson = totalPerPersonState.value)
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(2.dp),
//            .height(300.dp),
        elevation = CardDefaults.elevatedCardElevation(),
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.surface)

    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(14.dp)
        ) {
            InputField(
                valueState = totalBillState,
                labelId = "Enter Bill",
                enabled = true,
                isSingleLine = true,
                onAction = KeyboardActions {
                    if (!validState)
                        return@KeyboardActions
                    onValChange(totalBillState.value.trim())
                    keyboardController?.hide()
                },
                modifier = modifier.fillMaxWidth()
            )
            if (validState) {
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Split", style = MaterialTheme.typography.headlineSmall
                    )
                    Row(horizontalArrangement = Arrangement.SpaceBetween) {
                        FillIconButton(
                            modifier = modifier.size(30.dp),
                            onClick = {
                                splitByState.value =
                                    if (splitByState.value > 1) splitByState.value - 1
                                    else 1
                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.value,
                                    tipPercentage = tipPercentage
                                )
                            },
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove",
                            shape = RoundedCornerShape(10.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                        )
                        Text(
                            text = "${splitByState.value}",
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = modifier.padding(horizontal = 10.dp)
                        )
                        FillIconButton(
                            modifier = modifier.size(30.dp),
                            onClick = {
                                if (splitByState.value < range.last) {
                                    splitByState.value = splitByState.value + 1
                                }
                                totalPerPersonState.value = calculateTotalPerPerson(
                                    totalBill = totalBillState.value.toDouble(),
                                    splitBy = splitByState.value,
                                    tipPercentage = tipPercentage
                                )
                            },
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            shape = RoundedCornerShape(10.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
                        )
                    }

                }
                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 15.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = "Tax", style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = "$ ${tipAmountState.value}",
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                Text(
                    text = "$tipPercentage %",
                    modifier = modifier.align(alignment = Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.headlineSmall,
                )
                Slider(
                    value = sliderPositionState.value,
                    onValueChange = { newVal ->
                        sliderPositionState.value = newVal
                        tipAmountState.value = calculateTotalTip(
                            totalBill = totalBillState.value.toDouble(),
                            tipPercentage = tipPercentage
                        )
                        totalPerPersonState.value = calculateTotalPerPerson(
                            totalBill = totalBillState.value.toDouble(),
                            splitBy = splitByState.value,
                            tipPercentage = tipPercentage
                        )
                    },
                )
            } else {

            }

        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetSplitBillTheme {

    }
}