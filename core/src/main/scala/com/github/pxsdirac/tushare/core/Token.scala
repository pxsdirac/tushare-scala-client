package com.github.pxsdirac.tushare.core

import java.io.File
import scala.io.Source

trait Token {
  val value: String
}

object Token {
  def apply(token: String): Token =
    new Token {
      override val value: String = token
    }

  implicit val default: Token = {
    val source = Source.fromFile(
      System.getProperty("user.home").stripSuffix("/") + "/.tushare/token"
    )
    val token = source.mkString
    source.close()
    Token(token)
  }
}
