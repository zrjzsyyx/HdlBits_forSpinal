package hdl_bits

import spinal.core._

/** Base class for all HDL Bits exercises.
  * Automatically sets the Verilog module name to "top_module" and removes the io_ prefix from ports.
  */
abstract class HdlBitsExercise extends Component {
  noIoPrefix()
  setDefinitionName("top_module")
}
