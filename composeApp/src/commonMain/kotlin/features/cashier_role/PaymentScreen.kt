package features.cashier_role

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import ui.component.DefaultTextField
import ui.component.DisabledTextField
import ui.theme.dark
import ui.theme.primary
import ui.theme.primary_text
import ui.theme.red
import ui.theme.secondary_text
import ui.theme.stroke

@Composable
fun PaymentScreen() {
    var uangDiterima by remember { mutableStateOf("") }
    val radioOptions = listOf("Tunai", "Kredit")
    val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }

    Column(
        modifier = Modifier.fillMaxSize()
            .imePadding()
            .statusBarsPadding()
            .navigationBarsPadding()
    ) {
        Column(
            modifier = Modifier.weight(1f).fillMaxWidth().padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Pembayaran",
                style = MaterialTheme.typography.h4,
                color = dark,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Text(
                text = "Jenis Pembayaran",
                style = MaterialTheme.typography.h6,
                color = dark,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                radioOptions.forEach { text ->
                    Row(
                        modifier = Modifier
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = { onOptionSelected(text) },
                            ).padding(end = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = {
                                onOptionSelected(text)
                            }
                        )
                        Text(
                            text = text,
                            color = dark,
                        )
                    }
                }
            }
            if (selectedOption == "Kredit") {
                Text(
                    text = "Jatuh Tempo",
                    style = MaterialTheme.typography.h6,
                    color = dark,
                )
                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    label = { Text("dd/mm/yyyy", color = secondary_text) },
                    trailingIcon = {
                        IconButton(onClick = { }) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = "Date",
                                tint = stroke
                            )
                        }
                    },
                    shape = RoundedCornerShape(10.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = stroke,
                        unfocusedBorderColor = stroke,
                        cursorColor = primary_text,
                        focusedLabelColor = primary,
                        unfocusedLabelColor = secondary_text,
                        placeholderColor = secondary_text,
                        disabledPlaceholderColor = secondary_text,
                        leadingIconColor = secondary_text,
                        trailingIconColor = secondary_text,
                    ),
                    modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
                )
            }
            Text(
                text = "Uang Diterima",
                style = MaterialTheme.typography.h6,
                color = dark,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            DefaultTextField(
                value = uangDiterima,
                onValueChange = { uangDiterima = it },
                placehoder = "Nominal Uang",
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
            Text(
                text = "Kembalian",
                style = MaterialTheme.typography.h6,
                color = dark,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            DisabledTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
            )
        }
        Column {
            Divider(modifier = Modifier.fillMaxWidth().width(1.dp))
            Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Total Harga: ")
                    Text("Rp. 100.000")
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Diskon: ")
                    Text("Rp. 1.000")
                }
                Divider(modifier = Modifier.fillMaxWidth().width(1.dp).padding(vertical = 10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Total Tagihan", color = dark,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Rp. 613.000", color = dark,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    OutlinedButton(
                        onClick = {},
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = red),
                        border = BorderStroke(
                            width = 1.dp,
                            color = red
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Batal",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {},
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Text(
                            "Pembayaran",
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}