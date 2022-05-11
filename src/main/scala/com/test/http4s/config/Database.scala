package com.test.http4s.config

import cats.effect._
import cats.implicits._
import doobie._
import doobie.implicits._
import doobie.hikari._
import doobie.util.ExecutionContexts
import cats._
import com.test.http4s.model._

object Database {

  def bootstrap(xa: Transactor[IO]): IO[Int] = {
    AccountQuery.createTable.run.transact(xa)
  }

}
