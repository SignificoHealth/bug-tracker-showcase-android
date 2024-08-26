package com.significo.bugtracker.components

import android.content.res.Resources
import android.graphics.Typeface
import android.text.Spanned
import android.text.SpannedString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.text.style.UnderlineSpan
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.text.HtmlCompat

@Composable
fun annotatedStringResource(
    @StringRes id: Int,
    vararg formatArgs: Any
): AnnotatedString {
    val resources = LocalContext.current.resources
    return remember(id) {
        val text = resources.getText(id, *formatArgs)
        spannableStringToAnnotatedString(text)
    }
}

private fun spannableStringToAnnotatedString(text: CharSequence): AnnotatedString = if (text is Spanned) {
    val spanStyles = mutableListOf<AnnotatedString.Range<SpanStyle>>()
    spanStyles.addAll(
        text.getSpans(0, text.length, UnderlineSpan::class.java).map {
            AnnotatedString.Range(
                SpanStyle(textDecoration = TextDecoration.Underline),
                text.getSpanStart(it),
                text.getSpanEnd(it)
            )
        }
    )
    spanStyles.addAll(
        text.getSpans(0, text.length, StyleSpan::class.java).map {
            val style = when (it.style) {
                Typeface.BOLD -> SpanStyle(fontWeight = FontWeight.Bold)
                Typeface.ITALIC -> SpanStyle(fontStyle = FontStyle.Italic)
                Typeface.BOLD_ITALIC -> SpanStyle(
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic
                )

                else -> SpanStyle()
            }
            AnnotatedString.Range(
                style,
                text.getSpanStart(it),
                text.getSpanEnd(it)
            )
        }
    )
    spanStyles.addAll(
        text.getSpans(0, text.length, ForegroundColorSpan::class.java).map {
            val colorInt = it.foregroundColor
            val color = Color(colorInt)
            AnnotatedString.Range(
                SpanStyle(color = color),
                text.getSpanStart(it),
                text.getSpanEnd(it)
            )
        }
    )
    AnnotatedString(text.toString(), spanStyles = spanStyles)
} else {
    AnnotatedString(text.toString())
}

private fun Spanned.toHtmlWithoutParagraphs(): String = HtmlCompat.toHtml(
    this,
    HtmlCompat.TO_HTML_PARAGRAPH_LINES_CONSECUTIVE
)
    .substringAfter("<p dir=\"ltr\">").substringBeforeLast("</p>")

private fun Resources.getText(
    @StringRes id: Int,
    vararg args: Any
): CharSequence {
    val escapedArgs = args.map {
        if (it is Spanned) it.toHtmlWithoutParagraphs() else it
    }.toTypedArray()
    val resource = SpannedString(getText(id))
    val htmlResource = resource.toHtmlWithoutParagraphs()
    val formattedHtml = String.format(htmlResource, *escapedArgs)
    return HtmlCompat.fromHtml(formattedHtml, HtmlCompat.FROM_HTML_MODE_LEGACY)
}
