package models

import com.google.cloud.firestore.QueryDocumentSnapshot
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import models.SerieSeason.serializeForFirestore

import java.util
import scala.jdk.CollectionConverters.*

case class Series(name: String,
                  seasons: List[SerieSeason])

case class SerieSeason(nr: Int, releaseYear: Int)

object Series {

  def serializeForFirestore(series: Series): java.util.HashMap[String, Object] = {
    val docData = new java.util.HashMap[String, Object]();
    docData.put("name", series.name)
    val seriesSerialised = series.seasons.map(SerieSeason.serializeForFirestore).asJava
    docData.put("seasons", seriesSerialised)
    docData
  }

  def deserialize(input: QueryDocumentSnapshot): Series = {
    val dataMap = input.getData.asScala
    val list: util.List[util.HashMap[String, Object]] = dataMap.getOrElse("seasons", default = java.util.List.of()).asInstanceOf[util.List[util.HashMap[String, Object]]]
    val scalaList: List[Map[String, Object]] = list.asScala.map(_.asScala.toMap[String, Object]).toList
    Series(dataMap.getOrElse("name", default = "Name").toString, scalaList.map(SerieSeason.deserializeMap))
  }

  private def deserializeMap(dataMap: Map[String, Object]): Series = {
    Series(dataMap.getOrElse("name", default = "Name").toString, Nil)
  }
}


object SerieSeason {
  
  def serializeForFirestore(serieSeason: SerieSeason): java.util.HashMap[String, Object] = {
    val docData = new java.util.HashMap[String, Object]();
    docData.put("nr", serieSeason.nr.toString)
    docData.put("releaseYear", serieSeason.releaseYear.toString)
    docData
  }

  def deserializeMap(dataMap: Map[String, Object]): SerieSeason = {
    SerieSeason(dataMap.getOrElse("nr", default = 1).toString.toIntOption.getOrElse(1), dataMap.getOrElse("releaseYear", default = 0).toString.toIntOption.getOrElse(0))
  }
}