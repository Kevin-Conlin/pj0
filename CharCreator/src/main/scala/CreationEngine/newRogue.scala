package CreationEngine

class newRogue(override val name: String,
            override val charClass: String = "Rogue")
            extends Character(name, charClass) {

  attrStrength = 7
  attrDexterity = 10
  attrMagic = 0
  attrSpeech = 8

  override def spellOrAbility1() {
    println(s"Twin Dagger: \nCost: 20 Stamina \nThe player attacks with three strikes in rapid succession.\nEach attack deals ${(attrStrength * 5) + (attrDexterity * 2)/2.5} damage minus half the targets\nPhysical Defense.")
    println()
  }

}


