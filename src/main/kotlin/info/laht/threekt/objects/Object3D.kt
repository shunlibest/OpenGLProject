package info.laht.threekt.objects

import info.laht.threekt.geometries.BaseGeometry
import info.laht.threekt.geometries.BoxBufferGeometry
import info.laht.threekt.materials.Material
import info.laht.threekt.materials.MeshBasicMaterial
import info.laht.threekt.shaders.ShaderLib
import info.laht.threekt.vao.VAO
import info.laht.threekt.vao.VaoUtils

open class Object3D {


    //    val shader = ShaderLib.cube
//
//
//    val shaderUniform = ShaderLib.uniform
    var name = ""


    open var geometry: BaseGeometry = BoxBufferGeometry()
    open var material: Material = MeshBasicMaterial()


    val children = mutableListOf<Object3D>()

    private var vao: VAO? = null

    fun activityVAO() {
        if (vao == null) {
            vao = VaoUtils.createVao()
        }
        VaoUtils.activateVao(vao!!)
    }

}