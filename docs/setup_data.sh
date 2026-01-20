#!/bin/bash

# Wrapper script to set up the application data
# Executes scripts in order: users -> posts -> friendships

set -e  # Exit on first error

echo "========================================="
echo "Starting data setup..."
echo "========================================="
echo ""

# Step 1: Create users
echo "Step 1: Creating users..."
echo "-----------------------------------------"
bash user_creation.sh
echo "✓ Users created successfully"
echo ""

# Step 2: Create posts
echo "Step 2: Creating posts..."
echo "-----------------------------------------"
bash post_creation.sh
echo "✓ Posts created successfully"
echo ""

# Step 3: Create friendships
echo "Step 3: Creating friendships..."
echo "-----------------------------------------"
bash create_friends.sh
echo "✓ Friendships created successfully"
echo ""

echo "========================================="
echo "✓ Data setup completed successfully!"
echo "========================================="
