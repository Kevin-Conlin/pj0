package CreationEngine

class newMage(override val name: String,
           override val charClass: String = "Mage")
           extends Character(name, charClass) {

  attrStrength = 3
  attrDexterity = 3
  attrMagic = 13
  attrSpeech = 4

}