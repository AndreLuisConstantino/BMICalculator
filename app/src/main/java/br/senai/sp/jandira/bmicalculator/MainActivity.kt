package br.senai.sp.jandira.bmicalculator

import android.icu.text.ListFormatter.Width
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import br.senai.sp.jandira.bmicalculator.calcs.bmiCalculate
import br.senai.sp.jandira.bmicalculator.calcs.getBmiClassification
import br.senai.sp.jandira.bmicalculator.calcs.getBmiColorClassification
import br.senai.sp.jandira.bmicalculator.model.Client
import br.senai.sp.jandira.bmicalculator.model.Product
import br.senai.sp.jandira.bmicalculator.ui.theme.BMICalculatorTheme
import java.time.LocalDate
import kotlin.math.pow


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val p = Product()
        p.id = 100
        p.name = "Mouse"
        p.price = 230.0

        var x = p.addName()
        var y = p.listProducts()

        val c = Client(
            id = 100,
            name = "Pedro",
            birthDay = LocalDate.of(2005, 2, 2)
        )

        setContent {
            BMICalculatorTheme {
                CalculatorScreen()
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun CalculatorScreen() {
    //Criando a variável mutavel
    var weightState = rememberSaveable {
        mutableStateOf("")
    }

    var heightState = rememberSaveable {
        mutableStateOf("")
    }

    var bmiState = rememberSaveable {
        mutableStateOf("")
    }

    var bmiClassificationState = rememberSaveable {
        mutableStateOf("")
    }

    var bmiClassificationColor = rememberSaveable {
        mutableStateOf("")
    }

    //Criando variável de contexto
    var context = LocalContext.current


    Surface(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            //Header
            Column(
                modifier = Modifier
                    .background(color = Color.White)
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.bmi),
                    contentDescription = "Logo da aplicação",
                    modifier = Modifier.size(120.dp)
                )
                Text(
                    text = stringResource(id = R.string.title),
                    fontSize = 30.sp,
                    color = Color.Blue,
                    letterSpacing = 8.sp
                )
            }
            //FORM
            Column(
                modifier = Modifier
                    .background(color = Color.White)
                    .fillMaxWidth()
                    .padding(32.dp)

            ) {
                Text(
                    //Para procurar uma string dentro da pasta value
                    text = stringResource(id = R.string.weight_label),
                    modifier = Modifier.padding(
                        bottom = 8.dp,
                        top = 16.dp
                    )
                )
                OutlinedTextField(
                    //Para mudar o valor
                    value = weightState.value,
                    onValueChange = {
                        weightState.value = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Text(
                    text = stringResource(id = R.string.height_label),
                    modifier = Modifier.padding(
                        bottom = 8.dp,
                        top = 16.dp
                    )
                )
                OutlinedTextField(
                    value = heightState.value,
                    onValueChange = {
                        heightState.value = it
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                Spacer(
                    modifier = Modifier.height(32.dp)
                )

                Button(
                    onClick = {
                        var bmi = bmiCalculate(
                            weight = weightState.value.toDouble(),
                            height = heightState.value.toDouble()
                        )
                        bmiState.value = bmi.toString()
                        bmiClassificationState.value =
                            getBmiClassification(bmi,context)
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(Color(57, 218, 64, 255))
                )
                {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        text = stringResource(id = R.string.button_text),
                        textAlign = TextAlign.Center,
                        color = Color.White
                    )
                }
            }
            //FOOTER
            Column(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize(),
                    color = if(bmiState.value.isEmpty()) Color(31, 43, 145, 255)
                            else getBmiColorClassification(bmiState.value.toDouble()),
                    shape = RoundedCornerShape(
                        topStart = 18.dp,
                        topEnd = 18.dp
                    )
                )
                {
                    Column(
                        modifier = Modifier
                            .fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally


                    ) {
                        Text(
                            text = stringResource(id = R.string.your_score),
                            color = Color.White,
                            fontSize = 24.sp
                        )
                        Text(
                            text = String.format("%.2f",if(bmiState.value.isEmpty()) 0.0 else bmiState.value.toDouble()),
                            color = Color.White,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = bmiClassificationState.value,
                            color = Color.White,
                            fontSize = 18.sp
                        )

                        Row() {
                            Button(onClick = { /*TODO*/ }) {
                                Text(text = stringResource(id = R.string.reset))
                            }
                            Spacer(
                                modifier = Modifier.width(32.dp)
                            )
                            Button(onClick = { /*TODO*/ }) {
                                Text(text = stringResource(id = R.string.share))
                            }
                        }
                    }

                }
            }
        }
    }
}