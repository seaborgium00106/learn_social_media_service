# GitHub Monorepo Setup Instructions

## Current Status

✅ Git repository initialized  
✅ Root `.gitignore` created  
✅ Root `README.md` created  
✅ Initial commit made

⚠️ **Issue:** Backend and frontend contain nested `.git` directories from their original repositories. These need to be removed so all files are included in the monorepo.

---

## Steps to Complete Setup

### Step 1: Remove Nested Git Repositories

Run these commands in your terminal from the project root:

```bash
# Remove the nested git repositories
rm -rf backend/.git
rm -rf frontend/.git

# Verify they're gone
ls -la backend/.git   # Should show "No such file or directory"
ls -la frontend/.git  # Should show "No such file or directory"
```

### Step 2: Add All Backend and Frontend Files

```bash
# Stage all backend and frontend files
git add backend/ frontend/

# Check what will be committed (should show many new files)
git status

# You should see something like:
# new file:   backend/pom.xml
# new file:   backend/src/main/java/...
# new file:   frontend/package.json
# new file:   frontend/src/...
# ... and many more files
```

### Step 3: Commit Backend and Frontend Files

```bash
# Commit all the application files
git commit -m "Add backend and frontend source code"
```

### Step 4: Create GitHub Repository

1. **Go to GitHub** and create a new repository:
   - Visit: https://github.com/new
   - Repository name: Choose a name (e.g., `social-network-monorepo`, `fullstack-app`, etc.)
   - Description: "Full-stack social network application with Spring Boot backend and React frontend"
   - Keep it **Public** or **Private** (your choice)
   - **DO NOT** initialize with README, .gitignore, or license (we already have these)
   - Click "Create repository"

2. **Copy the repository URL** (shown on the next screen)
   - Example: `https://github.com/yourusername/your-repo-name.git`

### Step 5: Push to GitHub

```bash
# Add GitHub as remote origin (replace with YOUR repository URL)
git remote add origin https://github.com/yourusername/your-repo-name.git

# Rename branch to main (recommended)
git branch -M main

# Push to GitHub
git push -u origin main
```

### Step 6: Verify on GitHub

1. Go to your GitHub repository URL
2. You should see all the files:
   - ✅ backend/
   - ✅ frontend/
   - ✅ docs/
   - ✅ nginx/
   - ✅ docker-compose.yml
   - ✅ README.md
   - ✅ .gitignore

---

## Alternative: Using SSH (If you have SSH keys set up)

```bash
# Add GitHub as remote origin (SSH version)
git remote add origin git@github.com:yourusername/your-repo-name.git

# Push to GitHub
git push -u origin main
```

---

## Verification Commands

After pushing, verify everything worked:

```bash
# Check remote is configured
git remote -v

# Check branch
git branch

# Check commit history
git log --oneline

# See file count
git ls-files | wc -l
```

---

## Important Notes

### About .env Files
- ✅ `.env.example` is committed (safe, contains no secrets)
- ❌ `.env` is NOT committed (contains actual credentials)
- Always copy `.env.example` to `.env` and fill in real values locally

### Backend and Frontend Subdirectories
The backend and frontend directories now contain all source code:
- `backend/` - Spring Boot application with full source
- `frontend/` - React/TypeScript application with full source

### Individual Repos vs Monorepo
- Your **existing** backend and frontend GitHub repos remain unchanged
- This is a **NEW** monorepo containing both + infrastructure
- You can:
  - Continue developing in individual repos and sync to monorepo
  - OR move all development to the monorepo
  - OR keep them separate for different purposes

---

## Troubleshooting

### "fatal: remote origin already exists"
```bash
# Remove existing remote and re-add
git remote remove origin
git remote add origin https://github.com/yourusername/your-repo-name.git
```

### "backend/frontend still showing as modified"
```bash
# Make sure .git directories are removed
rm -rf backend/.git frontend/.git

# Force add
git add -f backend/ frontend/
```

### "Permission denied (publickey)"
You need to either:
1. Use HTTPS URL with username/password or personal access token
2. Set up SSH keys: https://docs.github.com/en/authentication/connecting-to-github-with-ssh

---

## Next Steps After Push

1. **Update Repository Settings** on GitHub:
   - Add description
   - Add topics/tags (e.g., `spring-boot`, `react`, `docker`, `monorepo`)
   - Set up branch protection rules (optional)

2. **Add Collaborators** (if working with a team):
   - Settings → Collaborators → Add people

3. **Set up CI/CD** (optional):
   - GitHub Actions for automated testing
   - Docker image building
   - Deployment pipelines

4. **Create Initial Issues/Projects** (optional):
   - Track features and bugs
   - Project boards for task management

---

## Quick Reference: Common Git Commands

```bash
# Check status
git status

# Add files
git add .

# Commit changes
git commit -m "Your message"

# Push changes
git push

# Pull latest changes
git pull

# View commit history
git log --oneline

# See what changed
git diff
```

---

**Ready to push?** Follow Steps 1-5 above! 🚀
