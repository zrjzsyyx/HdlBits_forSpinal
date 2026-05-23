package hdl_bits.exercises

import spinal.core._
import hdl_bits.HdlBitsExercise

class _12hClock extends HdlBitsExercise {
  val io = new Bundle {
    val ena = in Bool()
    val pm  = out Bool()
    val ss  = out Bits(8 bits)
    val mm  = out Bits(8 bits)
    val hh  = out Bits(8 bits)
  }

  val syncArea = new ClockingArea(ClockDomain(
    clock  = clockDomain.clock,
    reset  = clockDomain.reset,
    config = ClockDomainConfig(resetKind = SYNC)
  )) {
    // === Seconds: 00-59 BCD ===
    val ss_ones = Reg(UInt(4 bits)) init(0)
    val ss_tens = Reg(UInt(4 bits)) init(0)
    when(io.ena) {
      ss_ones := ss_ones + 1
      when(ss_ones === 9) {
        ss_ones := 0
        ss_tens := ss_tens + 1
        when(ss_tens === 5) { ss_tens := 0 }
      }
    }
    val ss_tc = io.ena && ss_tens === 5 && ss_ones === 9

    // === Minutes: 00-59 BCD ===
    val mm_ones = Reg(UInt(4 bits)) init(0)
    val mm_tens = Reg(UInt(4 bits)) init(0)
    when(ss_tc) {
      mm_ones := mm_ones + 1
      when(mm_ones === 9) {
        mm_ones := 0
        mm_tens := mm_tens + 1
        when(mm_tens === 5) { mm_tens := 0 }
      }
    }
    val mm_tc = ss_tc && mm_tens === 5 && mm_ones === 9

    // === Hours: 01-12 BCD ===
    val hh_ten = Reg(UInt(4 bits)) init(1)
    val hh_one = Reg(UInt(4 bits)) init(2)
    val pm_reg = Reg(Bool()) init(False)

    when(mm_tc) {
      when(hh_ten === 1 && hh_one === 1) {      // 11 → 12, toggle PM
        hh_one := 2
        pm_reg := !pm_reg
      } elsewhen(hh_ten === 1 && hh_one === 2) { // 12 → 01
        hh_ten := 0
        hh_one := 1
      } otherwise {
        hh_one := hh_one + 1
        when(hh_one === 9) {
          hh_one := 0
          hh_ten := hh_ten + 1
        }
      }
    }

    io.ss := (ss_tens ## ss_ones).asBits
    io.mm := (mm_tens ## mm_ones).asBits
    io.hh := (hh_ten ## hh_one).asBits
    io.pm := pm_reg
  }
}
