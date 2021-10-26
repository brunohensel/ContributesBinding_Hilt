package bruno.compiler

import javax.lang.model.element.TypeElement
import com.google.auto.common.MoreElements
import com.squareup.javapoet.ClassName

@Suppress("UnstableApiUsage")
data class BindingElements(val typeElement: TypeElement) {
  val className: ClassName = ClassName.get(typeElement)

  val moduleClassName: ClassName = ClassName.get(
    MoreElements.getPackage(typeElement).qualifiedName.toString(),
    "${className.simpleNames().joinToString("_")}_HiltBindingModule"
  )
}