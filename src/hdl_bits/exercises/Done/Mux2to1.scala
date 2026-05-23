package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class Mux2to1 extends HdlBitsExercise {
  val io = new Bundle {
    val a   = in Bool()
    val b   = in Bool()
    val sel = in Bool()
    val out_sig = out Bool()
  }
  io.out_sig := io.sel ? io.b | io.a
}
