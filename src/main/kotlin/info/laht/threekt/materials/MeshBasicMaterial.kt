package info.laht.threekt.materials


import info.laht.threekt.maths.Color
import info.laht.threekt.renderers.shaders.ShaderChunk

class MeshBasicMaterial : Material(), MaterialWithColor {

    override var color = Color.fromHex(0xffffff)
    override var needUpdateColor = true

    init {
        vertexShader = ShaderChunk.cube_vert
        fragmentShader = ShaderChunk.cube_frag
    }

}


