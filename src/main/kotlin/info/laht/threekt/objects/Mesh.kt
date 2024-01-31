package info.laht.threekt.objects


import info.laht.threekt.TrianglesDrawMode
import info.laht.threekt.geometries.BaseGeometry
import info.laht.threekt.geometries.BoxBufferGeometry
import info.laht.threekt.materials.Material
import info.laht.threekt.materials.MeshBasicMaterial


open class Mesh(override var geometry: BaseGeometry, override var material: Material) : Object3D() {

    var drawMode = TrianglesDrawMode

}
