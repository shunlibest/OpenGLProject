package info.laht.threekt.geometries

import info.laht.threekt.renders.FloatBufferAttribute

class BoxBufferGeometry : BaseGeometry() {

    init {
        val vertices = floatArrayOf(
            -0.5f, -0.5f, 0.0f, 0.5f, -0.5f, 0.0f, 0.0f, 0.5f, 0.0f
        )
        addAttribute("aPos", FloatBufferAttribute(vertices, 3))
    }

}
