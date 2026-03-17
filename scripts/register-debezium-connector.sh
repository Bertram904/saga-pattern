#!/usr/bin/env bash
# Register Debezium PostgreSQL connector (CDC from outbox tables → Kafka).
# Prerequisites: docker compose up (postgres wal_level=logical, kafka, debezium).
# From repo root: bash scripts/register-debezium-connector.sh

set -euo pipefail
CONNECT="${DEBEZIUM_URL:-http://localhost:8084}"
JSON="$(dirname "$0")/../debezium/postgres-outbox-connector.json"

echo "POST ${CONNECT}/connectors"
curl -sf -X POST "${CONNECT}/connectors" \
  -H "Content-Type: application/json" \
  -d @"${JSON}"
echo
echo "Connector registered. Kafka topics: ridehailing.cdc.<schema>.outbox_messages"
