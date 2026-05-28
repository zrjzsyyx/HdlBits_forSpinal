package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class EdgeDetect extends HdlBitsExercise {
  val io = new Bundle {
    val in_sig  = in Bits(8 bits)
    val anyedge = out Bits(8 bits)
  }
  val in_prev = RegNext(io.in_sig)
  io.anyedge := RegNext(io.in_sig ^ in_prev)
}
