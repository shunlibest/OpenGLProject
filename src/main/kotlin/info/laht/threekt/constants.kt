package info.laht.threekt

const val CullFaceNone = 0
const val CullFaceBack = 1
const val CullFaceFront = 2
const val CullFaceFrontBack = 3
const val FrontFaceDirectionCW = 0
const val FrontFaceDirectionCCW = 1
const val BasicShadowMap = 0
const val PCFShadowMap = 1
const val PCFSoftShadowMap = 2
const val FrontSide = 0
const val BackSide = 1
const val DoubleSide = 2
const val FlatShading = 1
const val SmoothShading = 2
const val NoColors = 0
const val FaceColors = 1
const val VertexColors = 2
const val NoBlending = 0
const val NormalBlending = 1
const val AdditiveBlending = 2
const val SubtractiveBlending = 3
const val MultiplyBlending = 4
const val CustomBlending = 5
const val AddEquation = 100
const val SubtractEquation = 101
const val ReverseSubtractEquation = 102
const val MinEquation = 103
const val MaxEquation = 104
const val ZeroFactor = 200
const val OneFactor = 201
const val SrcColorFactor = 202
const val OneMinusSrcColorFactor = 203
const val SrcAlphaFactor = 204
const val OneMinusSrcAlphaFactor = 205
const val DstAlphaFactor = 206
const val OneMinusDstAlphaFactor = 207
const val DstColorFactor = 208
const val OneMinusDstColorFactor = 209
const val SrcAlphaSaturateFactor = 210
const val NeverDepth = 0
const val AlwaysDepth = 1
const val LessDepth = 2
const val LessEqualDepth = 3
const val EqualDepth = 4
const val GreaterEqualDepth = 5
const val GreaterDepth = 6
const val NotEqualDepth = 7
const val MultiplyOperation = 0
const val MixOperation = 1
const val AddOperation = 2
const val NoToneMapping = 0
const val LinearToneMapping = 1
const val ReinhardToneMapping = 2
const val Uncharted2ToneMapping = 3
const val CineonToneMapping = 4
const val ACESFilmicToneMapping = 5

const val UVMapping = 300
const val CubeReflectionMapping = 301
const val CubeRefractionMapping = 302
const val EquirectangularReflectionMapping = 303
const val EquirectangularRefractionMapping = 304
const val SphericalReflectionMapping = 305
const val CubeUVReflectionMapping = 306
const val CubeUVRefractionMapping = 307
const val RepeatWrapping = 1000
const val ClampToEdgeWrapping = 1001
const val MirroredRepeatWrapping = 1002
const val NearestFilter = 1003
const val NearestMipMapNearestFilter = 1004
const val NearestMipMapLinearFilter = 1005
const val LinearFilter = 1006
const val LinearMipMapNearestFilter = 1007
const val LinearMipMapLinearFilter = 1008
const val UnsignedByteType = 1009
const val ByteType = 1010
const val ShortType = 1011
const val UnsignedShortType = 1012
const val IntType = 1013
const val UnsignedIntType = 1014
const val FloatType = 1015
const val HalfFloatType = 1016
const val UnsignedShort4444Type = 1017
const val UnsignedShort5551Type = 1018
const val UnsignedShort565Type = 1019
const val UnsignedInt248Type = 1020
const val AlphaFormat = 1021
const val RGBFormat = 1022
const val RGBAFormat = 1023
const val LuminanceFormat = 1024
const val LuminanceAlphaFormat = 1025
const val RGBEFormat = RGBAFormat
const val DepthFormat = 1026
const val DepthStencilFormat = 1027
const val RedFormat = 1028
const val RGB_S3TC_DXT1_Format = 33776
const val RGBA_S3TC_DXT1_Format = 33777
const val RGBA_S3TC_DXT3_Format = 33778
const val RGBA_S3TC_DXT5_Format = 33779
const val RGB_PVRTC_4BPPV1_Format = 35840
const val RGB_PVRTC_2BPPV1_Format = 35841
const val RGBA_PVRTC_4BPPV1_Format = 35842
const val RGBA_PVRTC_2BPPV1_Format = 35843
const val RGB_ETC1_Format = 36196
const val RGBA_ASTC_4x4_Format = 37808
const val RGBA_ASTC_5x4_Format = 37809
const val RGBA_ASTC_5x5_Format = 37810
const val RGBA_ASTC_6x5_Format = 37811
const val RGBA_ASTC_6x6_Format = 37812
const val RGBA_ASTC_8x5_Format = 37813
const val RGBA_ASTC_8x6_Format = 37814
const val RGBA_ASTC_8x8_Format = 37815
const val RGBA_ASTC_10x5_Format = 37816
const val RGBA_ASTC_10x6_Format = 37817
const val RGBA_ASTC_10x8_Format = 37818
const val RGBA_ASTC_10x10_Format = 37819
const val RGBA_ASTC_12x10_Format = 37820
const val RGBA_ASTC_12x12_Format = 37821
const val LoopOnce = 2200
const val LoopRepeat = 2201
const val LoopPingPong = 2202
const val InterpolateDiscrete = 2300
const val InterpolateLinear = 2301
const val InterpolateSmooth = 2302
const val ZeroCurvatureEnding = 2400
const val ZeroSlopeEnding = 2401
const val WrapAroundEnding = 2402
const val TrianglesDrawMode = 0
const val TriangleStripDrawMode = 1
const val TriangleFanDrawMode = 2
const val LinearEncoding = 3000
const val sRGBEncoding = 3001
const val GammaEncoding = 3007
const val RGBEEncoding = 3002
const val LogLuvEncoding = 3003
const val RGBM7Encoding = 3004
const val RGBM16Encoding = 3005
const val RGBDEncoding = 3006
const val BasicDepthPacking = 3200
const val RGBADepthPacking = 3201
const val TangentSpaceNormalMap = 0
const val ObjectSpaceNormalMap = 1