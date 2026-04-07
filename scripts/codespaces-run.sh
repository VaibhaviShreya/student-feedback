#!/usr/bin/env bash
set -euo pipefail

# Start backend and frontend for GitHub Codespaces
(./scripts/run-backend.sh) &
(cd frontend && npm run dev -- --host 0.0.0.0) &

wait
