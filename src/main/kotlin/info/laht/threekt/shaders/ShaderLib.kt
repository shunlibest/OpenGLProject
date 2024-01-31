package info.laht.threekt.shaders

import info.laht.threekt.renderers.shaders.ShaderChunk
import info.laht.threekt.renders.Uniform

object ShaderLib {

    val cube = Shader(
            mutableMapOf(
                    "tCube" to Uniform(null),
                    "tFlip" to Uniform(-1),
                    "opacity" to Uniform(1f)
            ),
            ShaderChunk.cube_vert,
            ShaderChunk.cube_frag
    )


    val uniform = Shader(
        mutableMapOf(
            "tCube" to Uniform(null),
            "tFlip" to Uniform(-1),
            "opacity" to Uniform(1f)
        ),
        ShaderChunk.uniform_vert,
        ShaderChunk.uniform_frag
    )

    class Shader(
            val name: String,
            val uniforms: MutableMap<String, Uniform>,
            val vertexShader: String,
            val fragmentShader: String
    ) {

        constructor (uniforms: MutableMap<String, Uniform>,
                     vertexShader: String,
                     fragmentShader: String) : this("", uniforms, vertexShader, fragmentShader)

    }

}
