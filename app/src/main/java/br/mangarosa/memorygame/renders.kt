package br.mangarosa.memorygame

fun String.bold() = "${ASCII_STYLES["bold"]}$this${ASCII_STYLES["reset"]}"
fun String.setColor(color: String) = "${ASCII_COLORS[color]}$this${ASCII_COLORS["reset"]}"
fun String.setBgColor(color: String) = "${ASCII_BG_COLORS[color]}$this${ASCII_BG_COLORS["reset"]}"

fun String.center(widthSize: Int = DEFAULT_TERMINAL_WIDTH_SIZE, paddingStr: String = " "): String {
    val paddingNumber = (widthSize - this.length)
    val paddingNumberForEachSide = (paddingNumber / 2).toInt()
    val halfPadding = paddingStr.repeat(paddingNumberForEachSide)
    var newString = halfPadding + this + halfPadding
    newString += paddingStr.repeat(widthSize - newString.length)
    return newString
}

fun Int.isPair() = this % 2 == 0
fun Int.half() = this / 2