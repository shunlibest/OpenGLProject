package info.laht.threekt.geometries

import info.laht.threekt.objects.Object3D
import info.laht.threekt.renders.GLAttributes
import org.lwjgl.opengl.GL15

class Geometries internal constructor(
    private val attributes: GLAttributes
) {

    private val updateList = mutableMapOf<Int, Int>()
    private val geometries = mutableMapOf<Int, BaseGeometry>()


    fun get(`object`: Object3D): BaseGeometry {
        val geometry = `object`.geometry
        val buffergeometry = geometries[geometry.id]
        if (buffergeometry == null) {
            geometries[geometry.id] = geometry
        }
        return geometry
    }

    fun update(geometry: BaseGeometry) {
        val geometryAttributes = geometry.attributes
        for ((name, attr) in geometryAttributes) {
            attributes.update(attr, GL15.GL_ARRAY_BUFFER)
        }
    }


}