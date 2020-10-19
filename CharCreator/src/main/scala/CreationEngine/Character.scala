package CreationEngine

import java.io.File

import CreationEngine.Engine.{collection, getCharacter, menuOrExit, newOrImport, transformCharacter}
import org.mongodb.scala.model.{Filters, UpdateOptions}
import org.mongodb.scala.model.Filters.equal
import org.mongodb.scala.model.Updates._

import scala.Console.println
import scala.io.StdIn

class Character(val name: String = "", val charClass: String = "") {
  val upsertTrue = (new UpdateOptions().upsert(true))

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
    println("============================================")
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
    println("Spells/Abilities")
    println("----------------")
    println()
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
      println()
      println(s"You have ${points} skill points to spend!")
      println("-----------------------------------------")
      println("Enter 1 to spend a pont on Strength (Major Stats: Health, Physical Attack | Minor Stats: Physical & Magic Defense)")
      println("Enter 2 to spend a point on Dexterity (Major Stats: Stamina | Minor Stats: All non-magic stats)")
      println("Enter 3 to spend a point on Magic (Major Stats: Magicka, Magic Attack & Defense | Minor Stats: None)")
      println()
      StdIn.readLine() match {
        case "1" => {
          attrStrength += 1
          statHealth = (50 + (attrStrength) * 10 + (attrDexterity) * 5)
          statStamina = (attrDexterity * 12 + attrStrength * 5)
          statPhysicalAttack = ((attrStrength * 5) + (attrDexterity * 2))
          statPhysicalDefense = ((attrStrength * 3) + (attrDexterity * 3))
          println("********************")
          println(s"Strength is now: ${attrStrength}")
          println("********************")
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
          println("*********************")
          println(s"Dexterity is now: ${attrDexterity}")
          println("*********************")
          points = (points - 1)
        }
        case "3" => {
          attrMagic += 1
          statMagicka = (attrMagic * 10)
          statMagicAttack = (attrMagic * 7)
          statMagicDefense = ((attrMagic * 5) + (statPhysicalDefense) - 5)
          println("*****************")
          println(s"Magic is now: ${attrMagic}")
          println("*****************")
          points = (points - 1)
        }
        case e => {
          println()
          println("Invalid input.")
          println()
          LevelUp(points)
        }
      }
    }
    charSheet()
    if (charClass == "Mage") fireball();
    else if (charClass == "Warrior") cleave();
    else twinDaggers()
    println("============================================")
    println()
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
    println()
    StdIn.readLine() match {
      case "y" => {
        val header = List[String]("Name", ",", "Class", ",", "Strength", ",", "Dexterity", ",", "Magic", ",", "Speech\n")
        val list = List[String](name, ",", charClass, ",", attrStrength.toString, ",", attrDexterity.toString, ",", attrMagic.toString, ",", attrSpeech.toString)
        printToFile(new File(s"${name.split(" ").map(_.trim).mkString("").toLowerCase}.csv")) {
          p =>
            header.foreach(p.print)
            list.foreach(p.print)
        }
        println()
        println("Your character has been saved!")
        println()
      }
      case "n" => {
        println()
        println("Character not saved.")
        println()
      }
      case e => {
        println()
        println("Invalid input.")
        println()
        Save()
      }
    }
    exportSave()
  }

  def exportSave(): Unit = {
    println("Export character to Mongo Database? (y or n)\n*WARNING* This will overwrite any character's stored in the database with the same name.")
    StdIn.readLine() match {
            case "y" => {
            getCharacter(collection.updateMany(equal("Name", name), combine(set("Class", charClass), set("Strength", attrStrength.toString), set("Dexterity", attrDexterity.toString), set("Magic", attrMagic.toString), set("Speech", attrSpeech.toString)), upsertTrue))
            println()
              println("Your character has been exported!")
            println()
            menuOrExit
            }
            case "n" => {
              println()
            println ("Character not exported.")
              println()
            newOrImport
              }
            case e => {
              println()
              print("Invalid input.")
              println()
              exportSave()
            }
      case "n" => menuOrExit
      case e =>
        println()
        println("Invalid input.")
        println()
        exportSave
    }
  }

  def levelOrSave: Unit = {
    println("===================================")
    println("1. Level up character")
    println("2. Save character")
    println("3. Return to main menu")
    println("===================================")
    println()
    StdIn.readLine()
    match {
      case "1" => LevelUp(5)
        Save()
      case "2" => Save()
      case "3" => newOrImport
      case e => println("Invalid input.")
        levelOrSave
    }
  }

  def fireball(): Unit = {
    println(s"Fireball: \nCost: 20 Magicka \nThe caster unleashes a ball of fire toward the targeted enemy\nthat deals ${50 * ((attrMagic * 7)*4/100)} damage minus half the target's\nMagicDefense.")
    println()
  }

  def twinDaggers(): Unit = {
      println(s"Twin Daggers: \nCost: 20 Stamina \nThe player attacks with three strikes in rapid succession.\nEach attack deals ${(attrStrength * 5) + (attrDexterity * 2)/2.5} damage minus half the target's\nPhysical Defense.")
      println()
    }

  def cleave(): Unit = {
    println(s"Cleave: \nCost: 20 Stamina \nThe player attacks with a heavy two-handed swing\nthat deals ${50 * ((((attrStrength * 5) + (attrDexterity * 2)) + (attrStrength)*4)/100)} damage minus half the target's\nPhysical Defense.")
    println()
  }

}