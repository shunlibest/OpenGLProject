package info.laht.threekt.renderers.shaders

internal object ShaderChunk {


    val cube_vert: String
            by lazy {
                load("shader/base.vert")
            }

    val cube_frag: String
            by lazy {
                load("shader/base.frag")
            }

    val uniform_vert: String
            by lazy {
                load("shader/uniform.vert")
            }
    val uniform_frag: String
            by lazy {
                load("shader/uniform.frag")
            }

    operator fun get(name: String): String? {

        return try {

            val method = ShaderChunk::class.java.getDeclaredMethod("get${name.capitalize()}")
            method.invoke(this) as String

        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }

    }

    private fun load(name: String): String {

        val path = name

        return ShaderChunk::class.java.classLoader.getResourceAsStream(path)
            .bufferedReader().use {
                it.readText()
            }

    }

}
