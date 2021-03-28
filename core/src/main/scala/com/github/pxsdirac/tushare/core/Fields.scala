package com.github.pxsdirac.tushare.core

import shapeless.ops.hlist.ToTraversable
import shapeless.ops.record.Keys
import shapeless.{HList, LabelledGeneric}

trait Fields[T] {
  def fields: Seq[String]
}

object Fields {
  implicit def shapelessImpl[T, Repr <: HList, KeysRepr <: HList](implicit
      gen: LabelledGeneric.Aux[T, Repr],
      keys: Keys.Aux[Repr, KeysRepr],
      traversable: ToTraversable.Aux[KeysRepr, List, Symbol]
  ): Fields[T] =
    new Fields[T] {
      def fields = {
        keys().toList.map(_.name)
      }
    }

}
