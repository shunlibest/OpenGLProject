package info.laht.threekt.materials

import info.laht.threekt.maths.Color
import info.laht.threekt.renders.GLProgram
import info.laht.threekt.renders.Uniform

open class Material {
    var name = ""
    internal var type = javaClass.simpleName

    internal open val uniforms: MutableMap<String, Uniform> = mutableMapOf()

    internal open var vertexShader: String = ""
    internal open var fragmentShader: String = ""

    internal var program: GLProgram? = null

}


interface MaterialWithColor {
    val color: Color
    var needUpdateColor: Boolean
}