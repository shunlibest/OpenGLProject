package info.laht.threekt.renders

import info.laht.threekt.Main.vertices
import info.laht.threekt.TriangleFanDrawMode
import info.laht.threekt.TriangleStripDrawMode
import info.laht.threekt.TrianglesDrawMode
import info.laht.threekt.cameras.Camera
import info.laht.threekt.geometries.BaseGeometry
import info.laht.threekt.geometries.Geometries
import info.laht.threekt.materials.Material
import info.laht.threekt.materials.MaterialWithColor
import info.laht.threekt.objects.Mesh
import info.laht.threekt.objects.Object3D
import info.laht.threekt.scenes.Scene
import info.laht.threekt.shaders.ShaderLib
import org.lwjgl.opengl.GL11
import org.lwjgl.opengl.GL15
import org.lwjgl.opengl.GL20

class GLRenderer {
    private val attributes = GLAttributes()

    private val geometries = Geometries(attributes)


    public var currentProgramId: Int = -1
    private var currentRenderList: MutableList<Object3D>? = null

    //    private val geometries = GLGeometries(attributes, info)
    private val state = GLState()

    fun render(scene: Scene, camera: Camera) {
        currentRenderList = ArrayList()
        projectObject(scene)
        currentRenderList!!.forEach {
            renderObject(it, camera)
        }
    }


    private fun projectObject(`object`: Object3D) {
        if (`object` is Mesh) {
            currentRenderList!!.add(`object`)
        }
        `object`.children.forEach { child ->
            projectObject(child)
        }

    }


    private fun renderObject(`object`: Object3D, camera: Camera) {

//        `object`.onBeforeRender?.invoke(this, scene, camera, geometry, material, group);
        //currentRenderState = renderStates.get(scene, camera)

//        `object`.modelViewMatrix.multiplyMatrices(camera.matrixWorldInverse, `object`.matrixWorld)
//        `object`.normalMatrix.getNormalMatrix(`object`.modelViewMatrix)

        // TODO object.isImmediateRenderObject

//        renderBufferDirect(camera, scene.fog, geometry, material, `object`, group)
        _render(`object`, camera)
//        `object`.onAfterRender?.invoke(this, scene, camera, geometry, material, group);
//        currentRenderState = renderStates.get(scene, camera)

    }


    private fun _render(`object`: Object3D, camera: Camera) {
        `object`.activityVAO()
        setProgram(`object`, camera)

        currentProgramId = `object`.material.program?.program!!

        geometries.update(`object`.geometry)
        setupVertexAttributes(`object`.material.program!!, `object`.geometry)
        drawOperate(`object`)
        GLUtils.checkError()
    }

    private fun drawOperate(`object`: Object3D) {

        //  执行渲染语句
        val dataCount = `object`.geometry.attributes.position!!.count

        var rendererMode = -1
        if (`object` is Mesh) {
            when (`object`.drawMode) {
                TrianglesDrawMode -> rendererMode = GL11.GL_TRIANGLES
                TriangleStripDrawMode -> rendererMode = GL11.GL_TRIANGLE_STRIP
                TriangleFanDrawMode -> rendererMode = GL11.GL_TRIANGLE_FAN
            }
        }

        GL11.glDrawArrays(rendererMode, 0, dataCount)

    }


    private fun setupVertexAttributes(program: GLProgram, geometry: BaseGeometry) {
        if (!geometry.needUpdateVertexAttribPointer) {
            return
        }
        geometry.needUpdateVertexAttribPointer = false
        val programAttributes = program.getAttributes()

        val geometryAttributes = geometry.attributes


        for (name in programAttributes.keys) {
            val programAttributeIndex = programAttributes[name] ?: error("")
            assert(programAttributeIndex >= 0)

            val geometryAttribute = geometryAttributes[name]
            if (geometryAttribute != null) {
                val size = geometryAttribute.itemSize
                val attribute = attributes.get(geometryAttribute)
                val buffer = attribute.buffer
                val type = attribute.type
                state.enableAttribute(programAttributeIndex)
                GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer)
                GL20.glVertexAttribPointer(programAttributeIndex, size, type, false, 0, 0)
            }

        }

    }


    private fun setProgram(`object`: Object3D, camera: Camera) {
        initMaterial(`object`)
        val program = `object`.material.program!!
        val material = `object`.material

        state.useProgram(program.program)
        val p_uniforms = program.getUniforms()
        if (material is MaterialWithColor && material.needUpdateColor) {
            p_uniforms.setValue("ourColor", material.color)
            material.needUpdateColor = false
        }
    }

    private fun initMaterial(`object`: Object3D) {
        val material = `object`.material
        if (material.program != null) return

        val shader = ShaderLib.Shader(
            name = material.type,
            uniforms = material.uniforms,
            vertexShader = material.vertexShader,
            fragmentShader = material.fragmentShader
        )
        val glProgram = GLProgram("cacheCode", shader)
        material.program = glProgram
    }

}