package services

import models.{Movie, Series}
import org.joda.time.LocalDateTime
import scala.jdk.CollectionConverters.*

import scala.concurrent.Future
import concurrent.ExecutionContext.Implicits.global

object FirebaseService {
  def addSeries(series: Series) = {
    Future {
      Firebase.db.collection("series").document(LocalDateTime.now().toString + series.name).set(Series.serializeForFirestore(series))
        .get()
    }
  }

  def addMovie(movie: Movie) = {
    Future {
      Firebase.db.collection("movies").document(LocalDateTime.now().toString + movie.name).set(Movie.serializeForFirestore(movie))
        .get()
    }
  }

  def getMovies(): Future[Seq[Movie]] = {
    Future {
      Firebase.db.collection("movies").get.get
    }
      .map { querySnapshot =>
        querySnapshot.getDocuments.asScala.map(Movie.deserialize).toSeq
      }
  }

  def getSeries(): Future[Seq[Series]] = {
    Future {
      Firebase.db.collection("series").get.get
    }
      .map { querySnapshot =>
        querySnapshot.getDocuments.asScala.map(Series.deserialize).toSeq
      }
  }
}
