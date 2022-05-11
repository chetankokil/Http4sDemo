package com.test.http4s

import cats.effect._
import org.http4s.server.Router
import com.test.http4s.routes.AccountRoutes
import org.http4s.server.blaze.BlazeServerBuilder
import cats.data.Kleisli
import org.http4s._
import cats.implicits._
import org.http4s.implicits._
import doobie._
import doobie.implicits._
import doobie.hikari._
import com.test.http4s.config.Database
import doobie.util.ExecutionContexts
import com.test.http4s.repository.AccountRepoImpl
import com.test.http4s.repository.AccountRepo

object Main extends IOApp {

  def makeRouter(
      xa: Transactor[IO]
  ): Kleisli[IO, Request[IO], Response[IO]] = {
    Router[IO](
      "api/v1" -> AccountRoutes.routes(new AccountRepoImpl(xa))
    ).orNotFound
  }

  val transactor: Resource[IO, HikariTransactor[IO]] =
    for {
      ce <- ExecutionContexts.fixedThreadPool[IO](32) // our connect EC
      te <- ExecutionContexts.cachedThreadPool[IO]
      xa <- HikariTransactor.newHikariTransactor[IO](
        "org.h2.Driver", // driver classname
        "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1", // connect URL
        "sa", // username
        "", // password
        ce, // await connection here,
        Blocker.liftExecutionContext(te)
      )
    } yield xa

  def serverStream(xa: Transactor[IO]) = {
    BlazeServerBuilder[IO]
      .withHttpApp(makeRouter(xa))
      .bindHttp(8080, "localhost")
      .serve
  }

  override def run(args: List[String]): IO[ExitCode] =
    transactor.use { xa =>
      for {
        _ <- Database.bootstrap(xa)
        exitCode <- serverStream(xa).compile.drain.as(ExitCode.Success)
      } yield exitCode

    }

}
