package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class Muxdiff2 extends HdlBitsExercise {

  val L = in Bool ()
  val R = in Bool ()
  val E = in Bool ()
  val w = in Bool ()
  val Q = out Bool ()

  val q_reg = Reg(Bool())

  val mid = Mux(E, w, q_reg)
  val mux_out = Mux(L, R, mid)

  q_reg := mux_out

  Q := q_reg
}
