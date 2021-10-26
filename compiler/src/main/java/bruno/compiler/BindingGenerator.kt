package bruno.compiler

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.type.DeclaredType
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier
import com.squareup.javapoet.JavaFile
import com.squareup.javapoet.MethodSpec

class BindingGenerator(
  private val processingEnv: ProcessingEnvironment,
  private val injectedBind: BindingElements,
  private val annotation: AnnotationMirror,
  private val boundTypeName: TypeName
) {
  fun generate(){
    val component = annotation.getValue("component") as DeclaredType

    val hiltModuleTypeSpec = TypeSpec.interfaceBuilder(injectedBind.moduleClassName)
      .addOriginatingElement(injectedBind.typeElement)
      .addGeneratedAnnotation(processingEnv.elementUtils, processingEnv.sourceVersion)
      .addAnnotation(ClassNames.MODULE)
      .addAnnotation(
        AnnotationSpec.builder(ClassNames.INSTALL_IN)
          .addMember("value", "$T.class", TypeName.get(component))
          .build()
      )
      .addAnnotation(
        AnnotationSpec.builder(ClassNames.ORIGINATING_ELEMENT)
          .addMember(
            "topLevelClass",
            "$T.class",
            injectedBind.className.topLevelClassName()
          )
          .build()
      )
      .addModifiers(Modifier.PUBLIC)
      .addMethod(
        MethodSpec.methodBuilder("bind")
          .addAnnotation(ClassNames.BINDS)
          .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
          .returns(boundTypeName)
          .addParameter(injectedBind.className, "impl")
          .build()
      )
      .build()
    JavaFile.builder(injectedBind.moduleClassName.packageName(), hiltModuleTypeSpec)
      .build()
      .writeTo(processingEnv.filer)
  }
}