@file:Suppress("unused")

package com.significo.bugtracker

import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.heading
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import com.halilibo.richtext.commonmark.CommonmarkAstNodeParser
import com.halilibo.richtext.commonmark.MarkdownParseOptions
import com.halilibo.richtext.markdown.BasicMarkdown
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.material.RichText
import com.halilibo.richtext.ui.string.RichTextStringStyle
import com.significo.bugtracker.core.ui.R

private val fontFamily = FontFamily(
    Font(R.font.metropolis_regular, FontWeight.Normal),
    Font(R.font.metropolis_extralight, FontWeight.ExtraLight),
    Font(R.font.metropolis_light, FontWeight.Light),
    Font(R.font.metropolis_bold, FontWeight.Bold),
    Font(R.font.metropolis_medium, FontWeight.SemiBold)
)

private val bigNumberLight = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Light,
    fontSize = 40.sp,
    lineHeight = 41.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)
private val bigNumberMedium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 40.sp,
    lineHeight = 41.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)
private val h1Light = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Light,
    fontSize = 28.sp,
    lineHeight = 34.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineBreak = LineBreak.Heading.copy(strategy = LineBreak.Strategy.HighQuality)
)
private val h1Medium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 28.sp,
    lineHeight = 34.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineBreak = LineBreak.Heading.copy(strategy = LineBreak.Strategy.HighQuality)
)
private val h2Light = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Light,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineBreak = LineBreak.Heading.copy(strategy = LineBreak.Strategy.Simple)
)
private val h2Medium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 22.sp,
    lineHeight = 28.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineBreak = LineBreak.Heading.copy(strategy = LineBreak.Strategy.Simple)
)
private val h3Light = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Light,
    fontSize = 17.sp,
    lineHeight = 24.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineBreak = LineBreak.Heading.copy(strategy = LineBreak.Strategy.HighQuality)
)
private val h3Medium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 17.sp,
    lineHeight = 24.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineBreak = LineBreak.Heading.copy(strategy = LineBreak.Strategy.HighQuality)
)
private val copyLight = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Light,
    fontSize = 17.sp,
    lineHeight = 24.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineBreak = LineBreak.Paragraph,
    hyphens = Hyphens.Auto
)
private val copyMedium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 17.sp,
    lineHeight = 24.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineBreak = LineBreak.Paragraph,
    hyphens = Hyphens.Auto
)
private val textLight = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Light,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineBreak = LineBreak.Paragraph,
    hyphens = Hyphens.Auto
)
private val textMedium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 14.sp,
    lineHeight = 20.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    ),
    lineBreak = LineBreak.Paragraph,
    hyphens = Hyphens.Auto
)
private val labelLight = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Light,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)
private val labelMedium = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 12.sp,
    lineHeight = 16.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)
private val labelSmall = TextStyle(
    fontFamily = fontFamily,
    fontWeight = FontWeight.Medium,
    fontSize = 10.sp,
    lineHeight = 13.sp,
    lineHeightStyle = LineHeightStyle(
        alignment = LineHeightStyle.Alignment.Center,
        trim = LineHeightStyle.Trim.None
    )
)

internal val typography = Typography(
    displayLarge = h1Medium,
    displayMedium = h2Medium,
    displaySmall = h3Medium,
    headlineLarge = copyMedium,
    headlineMedium = textMedium,
    headlineSmall = labelMedium,
    titleLarge = copyMedium,
    titleMedium = copyMedium,
    titleSmall = copyLight,
    bodyLarge = textMedium,
    bodyMedium = textMedium,
    bodySmall = textLight,
    labelLarge = labelMedium,
    labelMedium = labelMedium,
    labelSmall = labelSmall

)

data class ExtendedTypography(
    val bigNumberLight: TextStyle,
    val bigNumberMedium: TextStyle,
    val h1Light: TextStyle,
    val h1Medium: TextStyle,
    val h2Light: TextStyle,
    val h2Medium: TextStyle,
    val h3Light: TextStyle,
    val h3Medium: TextStyle,
    val copyLight: TextStyle,
    val copyMedium: TextStyle,
    val textLight: TextStyle,
    val textMedium: TextStyle,
    val labelLight: TextStyle,
    val labelMedium: TextStyle,
    val labelSmall: TextStyle
)

internal val LocalExtendedTypography = staticCompositionLocalOf {
    ExtendedTypography(
        bigNumberLight = TextStyle.Default,
        bigNumberMedium = TextStyle.Default,
        h1Light = TextStyle.Default,
        h1Medium = TextStyle.Default,
        h2Light = TextStyle.Default,
        h2Medium = TextStyle.Default,
        h3Light = TextStyle.Default,
        h3Medium = TextStyle.Default,
        copyLight = TextStyle.Default,
        copyMedium = TextStyle.Default,
        textLight = TextStyle.Default,
        textMedium = TextStyle.Default,
        labelLight = TextStyle.Default,
        labelMedium = TextStyle.Default,
        labelSmall = TextStyle.Default
    )
}

val extendedTypography = ExtendedTypography(
    bigNumberLight = bigNumberLight,
    bigNumberMedium = bigNumberMedium,
    h1Light = h1Light,
    h1Medium = h1Medium,
    h2Light = h2Light,
    h2Medium = h2Medium,
    h3Light = h3Light,
    h3Medium = h3Medium,
    copyLight = copyLight,
    copyMedium = copyMedium,
    textLight = textLight,
    textMedium = textMedium,
    labelLight = labelLight,
    labelMedium = labelMedium,
    labelSmall = labelSmall
)

internal val LocalRichTextStyle = staticCompositionLocalOf {
    RichTextStyle(
        paragraphSpacing = 16.sp,
        headingStyle = { level, _ ->
            when (level) {
                0 -> bigNumberLight
                1 -> h1Light
                2 -> h2Light
                else -> h3Medium
            }
        },
        stringStyle = RichTextStringStyle(
            boldStyle = textLight.toSpanStyle().copy(
                fontFamily = fontFamily,
                fontWeight = FontWeight.Bold
            ),
            codeStyle = SpanStyle(
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Medium,
                background = Neutral0
            ),
            linkStyle = SpanStyle(textDecoration = TextDecoration.Underline)
        )
    )
}

@Composable
fun BigNumberLight(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.bigNumberLight)
    )
}

@Composable
fun BigNumberMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.bigNumberMedium)
    )
}

@Composable
fun BigNumberMedium(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.bigNumberMedium)
    )
}

@Composable
fun H1Light(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier.semantics { heading() },
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.h1Light)
    )
}

@Composable
fun H1Medium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier.semantics { heading() },
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.h1Medium)
    )
}

@Composable
fun H1Medium(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier.semantics { heading() },
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.h1Medium)
    )
}

@Composable
fun H2Light(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier.semantics { heading() },
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.h2Light)
    )
}

@Composable
fun H2Light(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier.semantics { heading() },
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.h2Light)
    )
}

@Composable
fun H2Medium(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier.semantics { heading() },
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.h2Medium)
    )
}

@Composable
fun H2Medium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier.semantics { heading() },
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.h2Medium)
    )
}

@Composable
fun H3Light(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier.semantics { heading() },
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.h3Light)
    )
}

@Composable
fun H3Medium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier.semantics { heading() },
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.h3Medium)
    )
}

@Composable
fun H3Medium(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier.semantics { heading() },
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.h3Medium)
    )
}

@Composable
fun CopyLight(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.copyLight)
    )
}

@Composable
fun CopyLight(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.copyLight)
    )
}

@Composable
fun CopyMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.copyMedium)
    )
}

@Composable
fun CopyMedium(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.copyMedium)
    )
}

@Composable
fun TextLight(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.textLight)
    )
}

@Composable
fun TextLight(
    text: AnnotatedString,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.textLight)
    )
}

@Composable
fun TextMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.textMedium)
    )
}

@Composable
fun LabelLight(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.labelLight)
    )
}

@Composable
fun LabelMedium(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.labelMedium)
    )
}

@Composable
fun LabelSmall(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = Color.Unspecified,
    textAlign: TextAlign? = null,
    overflow: TextOverflow = TextOverflow.Ellipsis,
    maxLines: Int = Int.MAX_VALUE,
    minLines: Int = 1
) {
    Text(
        text = text,
        modifier = modifier,
        color = color,
        textAlign = textAlign,
        overflow = overflow,
        maxLines = maxLines,
        minLines = minLines,
        style = LocalTextStyle.current.merge(AppTheme.typography.labelSmall)
    )
}

@Composable
fun AppRichText(
    text: String,
    modifier: Modifier = Modifier
) {
    val parser = remember { CommonmarkAstNodeParser(MarkdownParseOptions.Default) }
    val astNode = remember(parser, text) { parser.parse(text.trimIndent()) }

    RichText(
        modifier = modifier,
        style = LocalRichTextStyle.current
    ) {
        BasicMarkdown(astNode = astNode)
    }
}
