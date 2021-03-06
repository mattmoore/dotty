package org.scalajs.testsuite.compiler

import org.junit.Assert.*
import org.junit.Test

import scala.scalajs.js

class RegressionTestScala3 {
  import RegressionTestScala3.*

  @Test def testRegressionDoubleDefinitionOfOuterPointerIssue10177(): Unit = {
    assertEquals(6, new OuterClassIssue10177().foo(5))
  }

  @Test def testRegressionJSClassWithSyntheticStaticMethodsIssue10563(): Unit = {
    val obj1 = new JSClassWithSyntheticStaticMethodsIssue10563(Some(3))
    assertEquals(3, obj1.y)
    assertEquals(8, obj1.foo(5))

    val obj2 = new JSClassWithSyntheticStaticMethodsIssue10563(None)
    assertEquals(-1, obj2.y)
    assertEquals(4, obj2.foo(5))
  }
}

object RegressionTestScala3 {
  class OuterClassIssue10177 { // can also be trait
    trait ParentTrait { // must be trait, can be private
      def concreteMethod(x: Int): Int = x + 1 // must have a concrete method
    }

    private class ChildClass extends ParentTrait // must be class *and* private

    def foo(x: Int): Int = new ChildClass().concreteMethod(x)
  }
}

// This class needs to be at the top-level, not in an object, to reproduce the issue
class JSClassWithSyntheticStaticMethodsIssue10563(x: Option[Int]) extends js.Object {
  val y: Int = x.getOrElse(-1) // lambda in constructor

  def foo(z: Int): Int = x.getOrElse(-1) + z // lambda in method
}
