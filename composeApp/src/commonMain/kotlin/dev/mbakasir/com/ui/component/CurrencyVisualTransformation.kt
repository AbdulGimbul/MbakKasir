package dev.mbakasir.com.ui.component

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class CurrencyVisualTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val originalText = text.text
        val digitsOnly = originalText.filter { it.isDigit() }
        
        if (digitsOnly.isEmpty()) {
            return TransformedText(
                text = AnnotatedString("Rp 0"),
                offsetMapping = object : OffsetMapping {
                    override fun originalToTransformed(offset: Int): Int = 4
                    override fun transformedToOriginal(offset: Int): Int = 0
                }
            )
        }
        
        val symbols = DecimalFormatSymbols(Locale("in", "ID"))
        symbols.groupingSeparator = '.'
        val formatter = DecimalFormat("#,###", symbols)
        val formatted = "Rp " + formatter.format(digitsOnly.toLong())
        
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                if (offset == 0) return 3
                val digitsBefore = originalText.take(offset).count { it.isDigit() }
                if (digitsBefore == 0) return 3
                
                val formattedDigits = formatted.filter { it.isDigit() }
                var transformedOffset = 3
                var digitsFound = 0
                
                for (i in 3 until formatted.length) {
                    if (formatted[i].isDigit()) {
                        digitsFound++
                        if (digitsFound == digitsBefore) {
                            return i + 1
                        }
                    }
                    transformedOffset = i + 1
                }
                return transformedOffset
            }
            
            override fun transformedToOriginal(offset: Int): Int {
                if (offset <= 3) return 0
                val digitsBeforeOffset = formatted.substring(0, offset).count { it.isDigit() }
                var originalOffset = 0
                var digitsFound = 0
                
                for (char in originalText) {
                    if (char.isDigit()) {
                        digitsFound++
                        if (digitsFound == digitsBeforeOffset) {
                            return originalOffset + 1
                        }
                    }
                    originalOffset++
                }
                return originalText.length
            }
        }
        
        return TransformedText(
            text = AnnotatedString(formatted),
            offsetMapping = offsetMapping
        )
    }
}

fun formatCurrencyInput(input: String): String {
    return input.filter { it.isDigit() }
}
