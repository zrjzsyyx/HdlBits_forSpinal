package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

/** One BCD digit (0-9) with enable and terminal count. */
class DecadeDigit extends Component {
  val io = new Bundle {
    val enable = in Bool()
    val q      = out UInt(4 bits)
    val tc     = out Bool()
  }
  val counter = Reg(UInt(4 bits)) init(0)
  when(io.enable) {
    counter := counter + 1
    when(counter === 9) { counter := 0 }
  }
  io.tc := io.enable && counter === 9
  io.q  := counter
}

/** 4-digit BCD counter with enable chain (synchronous reset). */
class CounterBCD extends HdlBitsExercise {
  val io = new Bundle {
    val ena = out Bits(3 bits)
    val q   = out UInt(16 bits)
  }

  val syncArea = new ClockingArea(ClockDomain(
    clock  = clockDomain.clock,
    reset  = clockDomain.reset,
    config = ClockDomainConfig(resetKind = SYNC)
  )) {
    val ones      = new DecadeDigit()
    val tens      = new DecadeDigit()
    val hundreds  = new DecadeDigit()
    val thousands = new DecadeDigit()

    ones.io.enable := True
    tens.io.enable     := ones.io.tc
    hundreds.io.enable := ones.io.tc && tens.io.tc
    thousands.io.enable := ones.io.tc && tens.io.tc && hundreds.io.tc
  }

  io.q := (syncArea.thousands.io.q ## syncArea.hundreds.io.q ## syncArea.tens.io.q ## syncArea.ones.io.q).asUInt
  io.ena := (syncArea.thousands.io.enable ## syncArea.hundreds.io.enable ## syncArea.tens.io.enable).resized
}
