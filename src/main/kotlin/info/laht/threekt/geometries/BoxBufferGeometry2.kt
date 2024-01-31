package info.laht.threekt.geometries

import info.laht.threekt.renders.FloatBufferAttribute

class BoxBufferGeometry2 : BaseGeometry() {

    init {
        val vertices = floatArrayOf( // 第一个三角形
            0.5f, 0.5f, 0.0f,  // 右上角
            0.5f, -0.5f, 0.0f,  // 右下角
            -0.5f, 0.5f, 0.0f,  // 左上角
            // 第二个三角形
            0.5f, -0.5f, 0.0f,  // 右下角
            -0.5f, -0.5f, 0.0f,  // 左下角
            -0.5f, 0.5f, 0.0f // 左上角
        ).map { it + 0.5f }.toFloatArray()

        addAttribute("aPos", FloatBufferAttribute(vertices, 3))
    }

}
