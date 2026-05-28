package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class Mux256to1 extends HdlBitsExercise {
  val io = new Bundle {
    val in_sig  = in Bits(1024 bits)
    val sel     = in UInt(8 bits)
    val out_sig = out Bits(4 bits)
  }
  io.out_sig := io.in_sig(io.sel * 4, 4 bits)
}
