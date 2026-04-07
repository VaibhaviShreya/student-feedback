#!/usr/bin/env bash
set -euo pipefail

# Start backend and frontend for GitHub Codespaces
(cd backend && mvn spring-boot:run) &
(cd frontend && npm run dev -- --host 0.0.0.0) &

wait
