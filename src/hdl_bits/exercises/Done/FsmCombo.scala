package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class FsmCombo extends HdlBitsExercise {
  val io = new Bundle {
    val in_sig     = in Bool()
    val state      = in UInt(2 bits)
    val next_state = out UInt(2 bits)
    val out_sig    = out Bool()
  }

  // Output: only state D (3) produces out=1
  io.out_sig := (io.state === 3)

  // Next state logic
  switch(io.state) {
    is(0) { io.next_state := io.in_sig ? U(1, 2 bits) | U(0, 2 bits) }  // A
    is(1) { io.next_state := io.in_sig ? U(1, 2 bits) | U(2, 2 bits) }  // B
    is(2) { io.next_state := io.in_sig ? U(3, 2 bits) | U(0, 2 bits) }  // C
    is(3) { io.next_state := io.in_sig ? U(1, 2 bits) | U(2, 2 bits) }  // D
  }
}
