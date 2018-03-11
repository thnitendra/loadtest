package simulations.examples

import io.gatling.core.Predef._

/**
 * Advanced Example using ChaingBuilder for modular setup
 */
class ChainBuilderSimulation extends Simulation {

    val chain = exec(Actions.getGoogle())
    val advancedExample = scenario("AdvancedExample").exec(chain)

    setUp(advancedExample
      .inject(rampUsers(Integer.getInteger("users", 1)) over 5)
    )
}