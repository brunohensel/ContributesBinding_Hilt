package bruno.hiltextension

import javax.inject.Inject
import bruno.hilt.ContributesBinding
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.scopes.ActivityScoped

@ContributesBinding(component = ActivityComponent::class)
@ActivityScoped
class GreetingImpl @Inject constructor() : Greeting {

  override fun greetingsFrom(yourName: String) {
    println("$yourName says hello to the whole World")
  }
}