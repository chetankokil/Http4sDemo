package com.test.http4s.config

import cats.effect._
import io.circe.config.parser
import io.circe.generic.auto._

case class ServerConfig(port: Int, host: String)

case class DBConfig(url: String, user: String, password: String, poolSize: Int)

case class Config(server: ServerConfig, dbConfig: DBConfig)

case class Account(id: String, name: String, timestamp: Long)

object Config {

  def load(): IO[Config] = {
    for {
      dbConfig <- parser.decodePathF[IO, DBConfig]("db")
      serverConfig <- parser.decodePathF[IO, ServerConfig]("server")
    } yield Config(serverConfig, dbConfig)
  }

}
