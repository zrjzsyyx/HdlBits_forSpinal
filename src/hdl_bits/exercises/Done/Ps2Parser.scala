package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class Ps2Parser extends HdlBitsExercise {
  val io = new Bundle {
    val clk = in Bool ()
    val in_sig = in UInt (8 bits)
    val reset = in Bool ()
    val done      = out Bool ()
    val out_bytes = out Bits (24 bits)
  }

  val syncArea = new ClockingArea(
    ClockDomain(
      clock = io.clk,
      reset = io.reset,
      config = ClockDomainConfig(resetKind = SYNC)
    )
  ) {
    val state = Reg(UInt(2 bits)) init (0)
    val done_reg = Reg(Bool()) init (False)
    val byte1 = Reg(UInt(8 bits))
    val byte2 = Reg(UInt(8 bits))
    val byte3 = Reg(UInt(8 bits))

    io.done      := done_reg
    io.out_bytes := byte1 ## byte2 ## byte3

    done_reg := (state === 2)

    switch(state) {
      is(0) {
        when(io.in_sig(3)) {
          state  := 1
          byte1  := io.in_sig
        }
      }
      is(1) {
        state := 2
        byte2 := io.in_sig
      }
      is(2) {
        state := 0
        byte3 := io.in_sig
      }
    }
  }
}
