package util

import io.circe.{Decoder, Encoder, HCursor}
import io.circe.generic.semiauto.deriveDecoder
import models.{Movie, SerieSeason, Series}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

object JsonHelpers {
  inline given Decoder[Movie] = deriveDecoder

  inline given Decoder[SerieSeason] = deriveDecoder

  inline given Decoder[Series] = deriveDecoder

  inline given Encoder[Movie] = deriveEncoder

  inline given Encoder[SerieSeason] = deriveEncoder

  inline given Encoder[Series] = deriveEncoder
}
