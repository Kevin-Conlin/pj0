package CreationEngine

import java.io.FileNotFoundException
import org.mongodb.scala.{MongoClient, MongoCollection, Observable}
import org.mongodb.scala.bson.codecs.Macros._
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.model.Filters.equal
import scala.concurrent.Await
import scala.concurrent.duration.{Duration, SECONDS}
import scala.io.{Source, StdIn}
import scala.sys.exit

object Engine extends App {

  val codecRegistry = fromRegistries(fromProviders(classOf[Import]), MongoClient.DEFAULT_CODEC_REGISTRY)
  val client = MongoClient()
  val db = client.getDatabase("characterdb").withCodecRegistry(codecRegistry)
  val collection : MongoCollection[Import] = db.getCollection("characters")

  def newCharacter: Unit = {
    println("Enter your new character's name:")
    var newName: String = StdIn.readLine()
    newName = newName.toLowerCase.split(' ').map(_.capitalize).mkString(" ")
    println("Choose a class:")
    println("1. Warrior")
    println("2. Mage")
    println("3. Rogue")
    StdIn.readLine() match{
      case "1" => val name = new newWarrior(newName)
        name.charSheet()
        name.LevelUp(10)
      case "2" => val name = new newMage(newName)
        name.charSheet()
        name.LevelUp(10)
      case "3" => val name = new newRogue(newName)
        name.charSheet()
        name.LevelUp(10)
      case e => {
        println("Invalid input.")
        println("")
        newCharacter
      }
    }
}

  def importChar {
    println("1. Import a locally saved character")
    println("2. Import from MongoDB")
    println("3. Return to main menu")
    val local = StdIn.readLine()
    match {
      case "1" => {
        println("What is the name of the character you wish to import? ")
        val importName = StdIn.readLine()
        val fileName: String = s"${importName.split(" ").map(_.trim).mkString("").toLowerCase}.csv"
        try {
          for (line <- Source.fromFile(fileName).getLines().drop(1)) {
            val cols = line.split(",").map(_.trim)
            val name = new Character(cols(0), cols(1))
            name.attrStrength = cols(2).toInt
            name.attrDexterity = cols(3).toInt
            name.attrMagic = cols(4).toInt
            name.attrSpeech = cols(5).toInt
            name.charSheet()
            menuOrExit()
          }
        } catch {
          case fnf: FileNotFoundException => println("Could not find Character.")
            importChar
        }
      }
      case "2" => {
        try {
          val cols2 = transformCharacter()
          val name = new Character(cols2(0), cols2(1))
          name.attrStrength = cols2(2).toInt
          name.attrDexterity = cols2(3).toInt
          name.attrMagic = cols2(4).toInt
          name.attrSpeech = cols2(5).toInt
          name.charSheet
          Thread.sleep(2000)
          name.Save
        } catch {
          case e => println("Character not found.")
            newOrImport
        }
      }
      case "3" => newOrImport()
      case e => println("Invalid input.")
    }
 }

  def newOrImport(): Unit = {
    println("What would you like to do?")
    println("1. Create a new character")
    println("2. Import an existing character")
    println("3. Exit the character creator")
    StdIn.readLine() match {
      case "1" => {
        newCharacter
      }
      case "2" => {
       importChar
        }
      case "3" => {
        println("Thanks for using the Character Creation Engine!")
        exit(0)
      }
      case e => {
        println("Invalid input.")
        newOrImport()
      }
    }
  }

  def menuOrExit(): Unit = {
    println("Enter 1 to return to the main menu or 2 to exit")
    StdIn.readLine() match {
      case "1" => newOrImport()
      case "2" => {
        println("Thanks for using the Character Creation Engine!")
        exit(0)
        }
      case e => {
        println("Invalid Input.")
        menuOrExit()
      }
      }
    }

  def getCharacter[T](obs: Observable[T]): Seq[T] = {
    Await.result(obs.toFuture(), Duration(10, SECONDS))
  }

  def exportCharacter[T](obs: Observable[T]): Unit = {
    getCharacter(obs).foreach(println(_))
  }

  def transformCharacter[T](): List[String] = {
    println("What is the name of the Character you wish to import? ")
    val importName = StdIn.readLine().toLowerCase().capitalize
    val string = getCharacter(collection.find(equal("Name", importName))).toString.stripSuffix("))")
    val list = string.split(",")
    val list1 = list.dropWhile(list.indexOf(_) < list.indexOf(importName))
    val cols1 = List(list1(0), list1(1), list1(2), list1(3), list1(4), list1(5).stripSuffix(")"))
    return(cols1)
  }

  Thread.sleep(2000)
  println("Welcome to the Character Creation Engine v.1.0!")
  println()
  Thread.sleep(1500)
  newOrImport()
  client.close()

}