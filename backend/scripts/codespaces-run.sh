#!/usr/bin/env bash
set -euo pipefail

# Wrapper for users currently in /backend directory
cd "$(dirname "$0")/../.."
./scripts/codespaces-run.sh
