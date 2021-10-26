package bruno.compiler

import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Element
import javax.lang.model.element.TypeElement
import com.google.auto.service.AutoService
import net.ltgt.gradle.incap.IncrementalAnnotationProcessor
import net.ltgt.gradle.incap.IncrementalAnnotationProcessorType.ISOLATING

@AutoService(Processor::class)
@IncrementalAnnotationProcessor(ISOLATING)
class BindingProcessor : AbstractProcessor() {

  override fun getSupportedAnnotationTypes() = setOf(ClassNames.INSTALL_BINDING.canonicalName())

  override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

  override fun process(annotations: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
    with(BindingStep(processingEnv)) {
      annotations.firstOrNull { it.qualifiedName.contentEquals(this.annotation()) }?.let { element ->
        this.process(roundEnv.getElementsAnnotatedWith(element))
      }
    }
    return false
  }
}