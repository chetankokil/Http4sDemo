package com.test.http4s.routes

import io.circe.generic.auto._
import org.http4s._
import org.http4s.circe.CirceEntityCodec._
import org.http4s.dsl.Http4sDsl
import cats.effect.IO
import com.test.http4s.config.Account
import com.test.http4s.repository.AccountRepo

object AccountRoutes {

  def routes(accountRepo: AccountRepo): HttpRoutes[IO] = {
    val dsl = Http4sDsl[IO]

    import dsl._

    HttpRoutes.of[IO] {
      case _ @GET -> Root / "account" / id =>
        accountRepo.getAccount(id) flatMap {
          case None        => NotFound()
          case Some(value) => Ok(value)
        }
      case req @ POST -> Root / "accounts" =>
        req
          .decode[Account] { acc =>
            accountRepo.createAccount(acc).flatMap(id => Created(id))
          }
          .handleErrorWith(e => BadRequest(e.getMessage))
    }

  }
}
