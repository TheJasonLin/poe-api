package services

import com.poe.parser.item.{DBItem, Mod}
import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.bson.codecs.configuration.CodecRegistry
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.{MongoClient, MongoCollection, MongoDatabase}

import scala.concurrent.Future

object Database {
  // setup codec registries
  val codecRegistry: CodecRegistry = fromRegistries(fromProviders(classOf[DBItem]), fromRegistries(fromProviders(classOf[Mod])), DEFAULT_CODEC_REGISTRY)
  // To directly connect to the default server localhost on port 27017
  private val mongoClient: MongoClient = MongoClient()
  private val database: MongoDatabase = mongoClient.getDatabase("poe").withCodecRegistry(codecRegistry)
  private val itemCollection: MongoCollection[DBItem] = database.getCollection("item")

  def get(className: String): Future[Seq[DBItem]] = {
    itemCollection
      .find(equal("className", className))
      .limit(50)
      .toFuture()
  }
}
