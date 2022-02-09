import cats.effect.*
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}
import models.{Movie, Series}
import org.http4s.{EntityDecoder, HttpRoutes, MediaType}
import org.http4s.dsl.io.*
import org.http4s.implicits.*
import org.http4s.blaze.server.*
import io.circe.syntax.*
import org.http4s.FormDataDecoder.formEntityDecoder
import org.http4s.circe.{jsonEncoder, jsonOf}
import org.http4s.circe.CirceEntityCodec.circeEntityDecoder
import org.http4s.circe.CirceEntityDecoder.circeEntityDecoder
import org.http4s.circe.CirceSensitiveDataEntityDecoder.circeEntityDecoder
import util.JsonHelpers.given_Decoder_Movie
import util.JsonHelpers.given_Decoder_SerieSeason
import util.JsonHelpers.given_Decoder_Series
import util.JsonHelpers.given_Encoder_Movie
import util.JsonHelpers.given_Encoder_SerieSeason
import util.JsonHelpers.given_Encoder_Series
import com.google.api.core.ApiFuture
import com.google.cloud.firestore.DocumentSnapshot
import com.google.cloud.firestore.QueryDocumentSnapshot
import com.google.cloud.firestore.QuerySnapshot
import services.{Firebase, FirebaseService}

import scala.concurrent.Future
import scala.jdk.CollectionConverters.*
import scala.jdk.FutureConverters.*

import org.http4s.circe.CirceEntityCodec.circeEntityEncoder
import org.http4s.circe.CirceEntityEncoder.circeEntityEncoder

import concurrent.ExecutionContext.Implicits.global

object Main extends IOApp {

  val helloWorldService = HttpRoutes.of[IO] {

    case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name.")

    case req@POST -> Root / "movies" =>
      req.as[Movie]
        .flatMap { movieInput =>
          IO.fromFuture {
            IO(FirebaseService.addMovie(movieInput))
          }
            .flatMap(_ => Ok(s"Good choice: ${movieInput.name}."))
        }

    case req@GET -> Root / "movies" =>
      IO.fromFuture {
        IO {
          FirebaseService.getMovies()
        }
      }.flatMap(Ok(_))

    case req@POST -> Root / "series" =>
      req.as[Series]
        .flatMap { seriesInput =>
          IO.fromFuture {
            IO(FirebaseService.addSeries(seriesInput))
          }
            .flatMap(_ => Ok(s"Good choice: ${seriesInput.name}."))
        }

    case req@GET -> Root / "series" =>
      IO.fromFuture {
        IO {
          FirebaseService.getSeries()
        }
      }.flatMap(Ok(_))

  }.orNotFound

  def run(args: List[String]): IO[ExitCode] =
    BlazeServerBuilder[IO]
      .bindHttp(8080, "localhost")
      .withHttpApp(helloWorldService)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
}

