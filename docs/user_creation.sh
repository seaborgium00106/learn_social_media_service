#!/bin/bash

USERS=(
  '{"username":"neetika","email":"neetika@example.com"}'
  '{"username":"sahil","email":"sahil@example.com"}'
  '{"username":"john","email":"john@example.com"}'
  '{"username":"ritish","email":"ritish@example.com"}'
  '{"username":"prem","email":"prem@example.com"}'
  '{"username":"om","email":"om@example.com"}'
  '{"username":"lalita","email":"lalita@example.com"}'
)

for user in "${USERS[@]}"; do
  curl -X POST http://localhost:9090/api/v1/users \
    -H "Content-Type: application/json" \
    -d "$user"
  echo ""  # newline for readability
done
