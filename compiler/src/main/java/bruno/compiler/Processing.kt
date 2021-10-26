package bruno.compiler

import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.TypeElement
import javax.lang.model.util.ElementFilter
import javax.lang.model.util.Elements
import com.squareup.javapoet.AnnotationSpec
import com.squareup.javapoet.ClassName
import com.squareup.javapoet.TypeSpec

const val T = "\$T"
const val S = "\$S"

internal fun AnnotationMirror.getValue(propertyName: String): Any? {
  val typeElement = annotationType.asElement() as TypeElement
  val properties = ElementFilter.methodsIn(typeElement.enclosedElements).associateBy { it.simpleName.toString() }
  return elementValues[properties[propertyName]]?.value
    ?: error("No annotation value found named '$propertyName'.")
}

internal fun TypeSpec.Builder.addGeneratedAnnotation(
  elements: Elements,
  sourceVersion: SourceVersion
) = apply {
  generatedAnnotationSpec(
    elements,
    sourceVersion,
    BindingProcessor::class.java
  )?.let { generatedAnnotation ->
    addAnnotation(generatedAnnotation)
  }
}

private fun generatedAnnotationSpec(
  elements: Elements,
  sourceVersion: SourceVersion,
  processorClass: Class<*>
) = elements.getTypeElement(
  if (sourceVersion > SourceVersion.RELEASE_8) {
    "javax.annotation.processing.Generated"
  } else {
    "javax.annotation.Generated"
  }
)?.let {
  AnnotationSpec.builder(ClassName.get(it))
    .addMember("value", S, processorClass.canonicalName)
    .build()
}
