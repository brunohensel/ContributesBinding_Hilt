package bruno.compiler

import javax.annotation.processing.ProcessingEnvironment
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import javax.lang.model.type.DeclaredType
import javax.tools.Diagnostic
import com.google.auto.common.MoreElements
import com.squareup.javapoet.TypeName

class BindingStep(private val processingEnv: ProcessingEnvironment) {

  fun annotation(): String = ClassNames.INSTALL_BINDING.canonicalName()

  fun process(annotatedElements: Set<Element>) {
    val parsedElements = mutableSetOf<TypeElement>()
    annotatedElements.forEach { element ->
      val typeElement = MoreElements.asType(element)
      if (parsedElements.add(typeElement)) {
        parse(typeElement).let { binding ->

          val annotation: AnnotationMirror = typeElement.getAnnotationMirror()
          val boundType = if (typeElement.interfaces.size > 1) {
            val declaredBoundType = annotation.getValue("boundType") as DeclaredType
            if (TypeName.get(declaredBoundType) == TypeName.OBJECT) {
              processingEnv.messager.printMessage(
                Diagnostic.Kind.ERROR,
                "$typeElement implements multiple interfaces and no boundType was specified.",
                typeElement
              )
              return
            }
            declaredBoundType
          } else {
            typeElement.interfaces.first()
          }
          val boundTypeName = TypeName.get(boundType)
          BindingGenerator(processingEnv, binding, annotation, boundTypeName).generate()
        }
      }
    }
  }

  private fun TypeElement.getAnnotationMirror() = annotationMirrors.first {
    val annotationElement = it.annotationType.asElement() as TypeElement
    return@first annotationElement.qualifiedName.toString() == ClassNames.INSTALL_BINDING.canonicalName()
  }

  private fun parse(typeElement: TypeElement): BindingElements {
    return BindingElements(typeElement)
  }
}