package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class Muxdiff extends HdlBitsExercise {

  val L = in Bool ()
  val r_in = in Bool ()
  val q_in = in Bool ()
  val Q = out Bool ()

  val mux_out = Mux(L, r_in, q_in)
  Q := RegNext(mux_out)
}
