package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

/** BCD counter BlackBox — matches the provided bcdcount module. */
class BcdCount extends BlackBox {
  val io = new Bundle {
    val clk    = in Bool()
    val reset  = in Bool()
    val enable = in Bool()
    val Q      = out UInt(4 bits)
  }
  noIoPrefix()
  setDefinitionName("bcdcount")
  mapClockDomain(clock = io.clk, reset = io.reset)
}

/** 1000 Hz → 1 Hz frequency divider using 3 cascaded BCD counters. */
class Counter extends HdlBitsExercise {
  val io = new Bundle {
    val OneHertz = out Bool()
    val c_enable = out Bits(3 bits)
  }

  val bcd0 = new BcdCount()
  val bcd1 = new BcdCount()
  val bcd2 = new BcdCount()

  // Enable chain
  bcd0.io.enable := True
  bcd1.io.enable := bcd0.io.Q === 9
  bcd2.io.enable := bcd0.io.Q === 9 && bcd1.io.Q === 9

  // Outputs
  io.c_enable(0) := True
  io.c_enable(1) := bcd0.io.Q === 9
  io.c_enable(2) := bcd0.io.Q === 9 && bcd1.io.Q === 9
  io.OneHertz := bcd0.io.Q === 9 && bcd1.io.Q === 9 && bcd2.io.Q === 9
}
