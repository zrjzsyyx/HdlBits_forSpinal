package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class Adder extends HdlBitsExercise {
  val io = new Bundle {
    val x   = in UInt(4 bits)
    val y   = in UInt(4 bits)
    val sum = out UInt(5 bits)
  }
  io.sum := io.x +^ io.y
}
