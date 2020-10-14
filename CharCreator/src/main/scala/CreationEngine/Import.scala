package CreationEngine



import org.bson.types.ObjectId

case class Import(_id: ObjectId, Name: String, Class: String, Strength: String, Dexterity: String, Magic: String, Speech: String){}

  object Import {
    def apply(Name:String, Class:String, Strength:String, Dexterity:String, Magic:String, Speech:String)
      : Import = Import(new ObjectId(), Name, Class, Strength, Dexterity, Magic, Speech);
  }