package models

import com.google.cloud.firestore.QueryDocumentSnapshot
import io.circe.Encoder
import io.circe.generic.semiauto.deriveEncoder

import scala.jdk.CollectionConverters.*

case class Movie(name: String, year: String)

object Movie {

  def serializeForFirestore(movie: Movie) = {
    val docData = new java.util.HashMap[String, Object]();
    docData.put("name", movie.name)
    docData.put("year", movie.year)
    docData
  }

  def deserialize(input: QueryDocumentSnapshot): Movie = {
    val dataMap = input.getData.asScala
    Movie(dataMap.getOrElse("name", default = "Name").toString,
      dataMap.getOrElse("year", default = "Year").toString)
  }
}