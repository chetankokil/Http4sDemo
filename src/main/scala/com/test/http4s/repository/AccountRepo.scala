package com.test.http4s.repository

import cats.effect.IO
import doobie.implicits._
import doobie.util.transactor._
import com.test.http4s.config.Account
import com.test.http4s.model._

trait AccountRepo {
  def createAccount(account: Account): IO[Int]

  def updateAccount(id: String, account: Account): IO[Int]

  def getAccount(id: String): IO[Option[Account]]

  def getAccounts(): IO[List[Account]]
}

class AccountRepoImpl(xa: Transactor[IO]) extends AccountRepo {

  override def createAccount(account: Account): IO[Int] =
    AccountQuery.insert(account).run.transact(xa)

  override def updateAccount(id: String, account: Account) = {
    AccountQuery.update(account.id, account.name).run.transact(xa)
  }

  override def getAccount(id: String) = {
    AccountQuery.searchWithId(id).option.transact(xa)
  }

  override def getAccounts() = {
    AccountQuery.searchWithRange(0, 10).to[List].transact(xa)
  }

}
