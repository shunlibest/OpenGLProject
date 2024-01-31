package info.laht.threekt.scenes

import info.laht.threekt.maths.Color

sealed class Background

class ColorBackground(
    val color: Color
) : Background() {

    constructor(hex: Int) : this(Color(hex))

}