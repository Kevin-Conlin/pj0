package CreationEngine

class newMage(override val name: String,
           override val charClass: String = "Mage")
           extends Character(name, charClass) {

  attrStrength = 3
  attrDexterity = 3
  attrMagic = 13
  attrSpeech = 4

  override def spellOrAbility1() {
    println(s"Fireball: \nCost: 20 Magicka \nThe caster unleashes a ball of fire toward the targeted enemy\nthat deals ${50 * ((attrMagic * 7)/100)} damage minus half the targets\nMagicDefense.")
    println()
  }

}
