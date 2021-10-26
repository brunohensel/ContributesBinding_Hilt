package bruno.compiler

import com.squareup.javapoet.ClassName

object ClassNames {
  val BINDS = ClassName.get("dagger", "Binds")
  val INSTALL_BINDING = ClassName.get("bruno.hilt", "ContributesBinding")
  val INSTALL_IN = ClassName.get("dagger.hilt", "InstallIn")
  val MODULE = ClassName.get("dagger", "Module")
  val ORIGINATING_ELEMENT = ClassName.get("dagger.hilt.codegen", "OriginatingElement")
}