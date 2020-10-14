package CreationEngine

import java.io.File
import CreationEngine.Engine.{collection, exportCharacter, menuOrExit}
import scala.io.StdIn

class Character(val name: String = "", val charClass: String = "") {

  var attrStrength = 0
  var attrDexterity = 0
  var attrMagic = 0
  var attrSpeech = 0

  def charSheet(): Unit = {
    var statHealth = (50 + (attrStrength) * 10 + (attrDexterity) * 3)
    var statStamina = (attrDexterity * 12 + attrStrength * 5)
    var statMagicka = (attrMagic * 10)
    var statPhysicalAttack = ((attrStrength * 5) + (attrDexterity * 2))
    var statPhysicalDefense = ((attrStrength * 3) + (attrDexterity * 3))
    var statMagicAttack = (attrMagic * 7)
    var statMagicDefense = ((attrMagic * 5) + (statPhysicalDefense) - 5)
    var statCriticalChance = (attrDexterity * 3)
    var statCriticalDam = (attrDexterity * 5)
    println()
    println("Basic Info")
    println("----------")
    println(s"Character Name: ${name} | Character's Class: ${charClass}")
    println()
    println("Attributes")
    println("----------")
    println(s"Strength: ${attrStrength} | Dexterity: ${attrDexterity} | Magic: ${attrMagic}")
    println()
    println("Primary Stats")
    println("-------------")
    println(s"Health: ${statHealth} | Stamina: ${statStamina} | Magicka: ${statMagicka}")
    println()
    println("Combat Stats")
    println("------------")
    println(s"Physical Attack: ${statPhysicalAttack} | Physical Defense: ${statPhysicalDefense}")
    println(s"Magic Attack: ${statMagicAttack} | Magic Defense: ${statMagicDefense}")
    println(s"Critical Chance: ${statCriticalChance}% | Critical Damage: +${statCriticalDam}%")
    println()
    /*println("Spells/Abilities")
    println("----------------")
    println()*/
    }

  def LevelUp(x: Int): Unit = {
    var statHealth = (50 + (attrStrength) * 10 + (attrDexterity) * 5)
    var statStamina = (attrDexterity * 12 + attrStrength * 5)
    var statMagicka = (attrMagic * 10)
    var statPhysicalAttack = ((attrStrength * 5) + (attrDexterity * 2))
    var statPhysicalDefense = ((attrStrength * 3) + (attrDexterity * 3))
    var statMagicAttack = (attrMagic * 7)
    var statMagicDefense = ((attrMagic * 5) + (statPhysicalDefense) - 5)
    var statCriticalChance = (attrDexterity * 5)
    var statCriticalDam = (attrDexterity * 3)
    var points = x
    while (points > 0) {
      println(s"You have ${points} skill points to spend!")
      println("Enter 1 to spend a pont on Strength (Major Stats: Health, Physical Attack | Minor Stats: Physical & Magic Defense)")
      println("Enter 2 to spend a point on Dexterity (Major Stats: Stamina | Minor Stats: All non-magic stats)")
      println("Enter 3 to spend a point on Magic (Major Stats: Magicka, Magic Attack & Defense | Minor Stats: None)")
      StdIn.readLine() match {
        case "1" => {
          attrStrength += 1
          statHealth = (50 + (attrStrength) * 10 + (attrDexterity) * 5)
          statStamina = (attrDexterity * 12 + attrStrength * 5)
          statPhysicalAttack = ((attrStrength * 5) + (attrDexterity * 2))
          statPhysicalDefense = ((attrStrength * 3) + (attrDexterity * 3))
          println(s"Strength is now: ${attrStrength}")
          points = (points - 1)
        }
        case "2" => {
          attrDexterity += 1
          statHealth = (50 + (attrStrength) * 10 + (attrDexterity) * 5)
          statStamina = (attrDexterity * 12 + attrStrength * 5)
          statPhysicalAttack = ((attrStrength * 5) + (attrDexterity * 2))
          statPhysicalDefense = ((attrStrength * 3) + (attrDexterity * 3))
          statCriticalChance = (attrDexterity * 5)
          statCriticalDam = (attrDexterity * 3)
          println(s"Dexterity is now: ${attrDexterity}")
          points = (points - 1)
        }
        case "3" => {
          attrMagic += 1
          statMagicka = (attrMagic * 10)
          statMagicAttack = (attrMagic * 7)
          statMagicDefense = ((attrMagic * 5) + (statPhysicalDefense) - 5)
          println(s"Magic is now: ${attrMagic}")
          points = (points - 1)
        }
        case e => {
          println("Invalid input.")
          LevelUp(points)
        }
      }
    }
    charSheet()
    //spellOrAbility1()
    Save()
  }

  def printToFile(f: java.io.File)(op: java.io.PrintWriter => Unit) {
    val p = new java.io.PrintWriter(f)
    try {
      op(p)
    } finally {
      p.close()
    }
  }

  def Save(): Unit = {
    println("Would you like to save your character? (y or n)\n*WARNING* This will overwrite any local saves with the same character name.")
    StdIn.readLine() match {
      case "y" => {
        val header = List[String]("Name", ",", "Class", ",", "Strength", ",", "Dexterity", ",", "Magic", ",", "Speech\n")
        val list = List[String](name, ",", charClass, ",", attrStrength.toString, ",", attrDexterity.toString, ",", attrMagic.toString, ",", attrSpeech.toString)
        printToFile(new File(s"${name.split(" ").map(_.trim).mkString("").toLowerCase}.csv")) {
          p =>
            header.foreach(p.print)
            list.foreach(p.print)
        }
        exportSave
      }
      case "n" => {
        println("Character not saved.")
        println()
        menuOrExit()
      }
      case e => {
        println("Invalid input.")
        Save()
      }
    }
  }

  def exportSave(): Unit = {
    println("Export character to Mongo Database? (y or n)")
    StdIn.readLine()
    match {
      case "y" => {
        exportCharacter(collection.insertOne(Import(name, charClass, attrStrength.toString, attrDexterity.toString, attrMagic.toString, attrSpeech.toString)))
        println("Your character has been saved!")
        menuOrExit()
      }
      case "n" => menuOrExit
      case e => println("Invalid input.")
        exportSave
    }
  }

  def spellOrAbility1(): Unit = {
  println()
  }

}