#!/bin/bash

FRIENDSHIPS=(
  '{"userId":4,"friendId":1}'
  '{"userId":2,"friendId":4}'
  '{"userId":3,"friendId":2}'
  '{"userId":4,"friendId":1}'
)

for friendship in "${FRIENDSHIPS[@]}"; do
  wget --post-data="$friendship" \
    --header='Content-Type: application/json' \
    http://localhost:9090/api/v1/friendships
  echo ""
done
