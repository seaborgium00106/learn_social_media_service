#!/bin/bash

# Monorepo Preparation Script
# This script removes nested git repositories and prepares the monorepo for GitHub

set -e  # Exit on error

echo "=========================================="
echo "Monorepo Preparation Script"
echo "=========================================="
echo ""

# Step 1: Remove nested .git directories
echo "Step 1: Removing nested .git directories..."
if [ -d "backend/.git" ]; then
    echo "  Removing backend/.git..."
    rm -rf backend/.git
    echo "  ✓ backend/.git removed"
else
    echo "  - backend/.git not found (already removed or never existed)"
fi

if [ -d "frontend/.git" ]; then
    echo "  Removing frontend/.git..."
    rm -rf frontend/.git
    echo "  ✓ frontend/.git removed"
else
    echo "  - frontend/.git not found (already removed or never existed)"
fi

echo ""

# Step 2: Verify removal
echo "Step 2: Verifying removal..."
if [ -d "backend/.git" ]; then
    echo "  ✗ ERROR: backend/.git still exists!"
    exit 1
else
    echo "  ✓ backend/.git verified removed"
fi

if [ -d "frontend/.git" ]; then
    echo "  ✗ ERROR: frontend/.git still exists!"
    exit 1
else
    echo "  ✓ frontend/.git verified removed"
fi

echo ""

# Step 3: Check if git is initialized in current directory
echo "Step 3: Checking git repository..."
if [ -d ".git" ]; then
    echo "  ✓ Git repository found in current directory"
else
    echo "  ✗ ERROR: Git repository not found. Run 'git init' first"
    exit 1
fi

echo ""

# Step 4: Add backend and frontend files
echo "Step 4: Adding backend and frontend files to git..."
git add backend/ frontend/

# Count files
BACKEND_FILES=$(git diff --cached --name-only | grep "^backend/" | wc -l)
FRONTEND_FILES=$(git diff --cached --name-only | grep "^frontend/" | wc -l)

echo "  Files staged:"
echo "    - Backend: $BACKEND_FILES files"
echo "    - Frontend: $FRONTEND_FILES files"

echo ""

# Step 5: Show what will be committed
echo "Step 5: Files to be committed:"
echo "========================================"
git status --short | head -30
echo "========================================"
echo ""

# Step 6: Ask for confirmation
echo "Ready to commit? This will add all backend and frontend source files."
echo ""
read -p "Continue with commit? (yes/no): " -r RESPONSE
echo ""

if [[ $RESPONSE =~ ^[Yy][Ee][Ss]$ ]] || [[ $RESPONSE =~ ^[Yy]$ ]]; then
    git commit -m "Add backend and frontend source code to monorepo"
    echo ""
    echo "✓ Commit successful!"
    echo ""
    echo "=========================================="
    echo "Next Steps:"
    echo "=========================================="
    echo "1. Create a new repository on GitHub (don't initialize with any files)"
    echo "2. Run: git remote add origin https://github.com/yourusername/your-repo.git"
    echo "3. Run: git branch -M main"
    echo "4. Run: git push -u origin main"
    echo ""
    echo "For detailed instructions, see: GITHUB_SETUP_INSTRUCTIONS.md"
    echo "=========================================="
else
    echo "✗ Commit cancelled. No changes made."
    echo "You can review the files with 'git status' and run this script again."
fi
