package example

import org.openjdk.jmh.annotations.Benchmark
import org.openjdk.jmh.annotations.Scope
import org.openjdk.jmh.annotations.Setup
import org.openjdk.jmh.annotations.State
import org.openjdk.jmh.annotations.Param

@State(Scope.Thread)
class Main {
  @Param(Array("1000", "100000", "10000000"))
  var size: Int = -1

  private var array: Array[Byte] = null

  @Setup
  def setup(): Unit = {
    array = Array.tabulate(size)(_.toByte)
  }

  @Benchmark
  def apache_thread_safe(): Array[Byte] = {
    val out = new org.apache.commons.io.output.ByteArrayOutputStream()
    out.write(array)
    val in = out.toInputStream
    in.readAllBytes()
  }

  @Benchmark
  def apache_thread_unsafe(): Array[Byte] = {
    val out = new org.apache.commons.io.output.UnsynchronizedByteArrayOutputStream()
    out.write(array)
    val in = out.toInputStream
    in.readAllBytes()
  }

  @Benchmark
  def std(): Array[Byte] = {
    val out = new java.io.ByteArrayOutputStream()
    out.write(array)
    val in = new java.io.ByteArrayInputStream(out.toByteArray)
    in.readAllBytes()
  }
}

