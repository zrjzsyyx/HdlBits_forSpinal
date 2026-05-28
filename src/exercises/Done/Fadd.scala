package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class Fadd extends HdlBitsExercise {
  val io = new Bundle {
    val a    = in Bool()
    val b    = in Bool()
    val cin  = in Bool()
    val cout = out Bool()
    val sum  = out Bool()
  }
  io.cout := (io.a & io.b) | (io.a & io.cin) | (io.b & io.cin)
  io.sum  := io.a ^ io.b ^ io.cin
}
