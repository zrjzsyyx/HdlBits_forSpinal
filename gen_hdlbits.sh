#!/bin/bash
set -euo pipefail

PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"
GEN_DIR="$PROJECT_ROOT/gen/hdl_bits"

RED='\033[0;31m'
GREEN='\033[0;32m'
CYAN='\033[0;36m'
NC='\033[0m'

usage() {
    echo "Usage: $0 <exercise> [--lint] [--show]"
    echo ""
    echo "  exercise   Exercise name (e.g., StepOne, Mux2to1)"
    echo "  --lint     Run Verilator lint check after generation"
    echo "  --show     Print generated Verilog to stdout"
    echo ""
    exit 1
}

[ $# -lt 1 ] && usage

EXERCISE="$1"
shift
DO_LINT=false
DO_SHOW=false

for arg in "$@"; do
    case $arg in
        --lint) DO_LINT=true ;;
        --show) DO_SHOW=true ;;
        *) echo -e "${RED}Unknown option: $arg${NC}"; usage ;;
    esac
done

echo -e "${CYAN}=== Generating Verilog for: $EXERCISE ===${NC}"
cd "$PROJECT_ROOT"
sbt "runMain hdl_bits.Gen $EXERCISE"

VERILOG_FILE="$GEN_DIR/top_module.v"

if [ ! -f "$VERILOG_FILE" ]; then
    echo -e "${RED}Error: $VERILOG_FILE not generated${NC}"
    exit 1
fi

echo -e "${GREEN}Generated: $VERILOG_FILE${NC}"

if $DO_LINT; then
    echo -e "${CYAN}=== Running Verilator lint ===${NC}"
    verilator --lint-only -Wall "$VERILOG_FILE" \
        && echo -e "${GREEN}Lint passed${NC}" \
        || echo -e "${RED}Lint failed — check warnings above${NC}"
fi

if $DO_SHOW; then
    echo -e "${CYAN}=== Verilog output ===${NC}"
    cat "$VERILOG_FILE"
fi
