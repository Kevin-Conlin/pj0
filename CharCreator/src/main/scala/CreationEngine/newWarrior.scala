package CreationEngine

class newWarrior(override val name: String,
              override val charClass: String = "Warrior")
              extends Character(name, charClass) {

  attrStrength = 13
  attrDexterity = 6
  attrMagic = 0
  attrSpeech = 4

}