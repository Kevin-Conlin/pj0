package CreationEngine

class newRogue(override val name: String,
            override val charClass: String = "Rogue")
            extends Character(name, charClass) {

  attrStrength = 7
  attrDexterity = 10
  attrMagic = 0
  attrSpeech = 8

}